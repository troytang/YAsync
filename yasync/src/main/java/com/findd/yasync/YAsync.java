package com.findd.yasync;

import android.util.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by troy_tang on 2014/11/4.
 */
public class YAsync {

    private static final String LOG_TAG = "YAsync";

    private static int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    static {
        Log.i(LOG_TAG, "CPU ： " + CPU_COUNT);
    }

    /*********************************** 基本线程池（无容量限制） *******************************/
    /**
     * 有N处理器，便长期保持N个活跃线程。
     */
    private static final int CORE_POOL_SIZE = CPU_COUNT;
    private static final int MAXIMUM_POOL_SIZE = Integer.MAX_VALUE;
    private static final int KEEP_ALIVE = 10;
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "YAsync #" + mCount.getAndIncrement());
        }
    };
    private static final BlockingQueue<Runnable> sPoolWorkQueue = new SynchronousQueue<Runnable>();
    /**
     * An {@link Executor} that can be used to execute tasks in parallel.
     * 核心线程数为{@link #CORE_POOL_SIZE}，不限制并发总线程数!
     * 这就使得任务总能得到执行，且高效执行少量（<={@link #CORE_POOL_SIZE}）异步任务。
     * 线程完成任务后保持{@link #KEEP_ALIVE}秒销毁，这段时间内可重用以应付短时间内较大量并发，提升性能。
     * 它实际控制并执行线程任务。
     */
    public static final ThreadPoolExecutor mCachedSerialExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE,
            MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);

    /*********************************** 线程并发控制器 *******************************/
    /**
     * 并发量控制: 根据cpu能力控制一段时间内并发数量，并发过量大时采用Lru方式移除旧的异步任务，默认采用LIFO策略调度线程运作，开发者可选调度策略有LIFO、FIFO。
     */
    public static final Executor mLruSerialExecutor = new SmartSerialExecutor();

    /**
     * 对外统一接口
     * @param runnable YAsyncRunner实例
     */
    public static void execute(Runnable runnable){
        mLruSerialExecutor.execute(runnable);
    }

    /**
     * 它大大改善Android自带异步任务框架的处理能力和速度。
     * 默认地，它使用LIFO（后进先出）策略来调度线程，可将最新的任务快速执行，当然你自己可以换为FIFO调度策略。
     * 这有助于用户当前任务优先完成（比如加载图片时，很容易做到当前屏幕上的图片优先加载）。
     *
     * @author troy_tang
     * 2014-11-6 11:03:56
     */
    private static class SmartSerialExecutor implements Executor {
        /**
         * 这里使用{@link ArrayDequeCompat}当栈比{@link Stack}性能高
         */
        private ArrayDequeCompat<Runnable> mQueue = new ArrayDequeCompat<Runnable>(serialMaxCount);
        private ScheduleStrategy mStrategy = ScheduleStrategy.LIFO;

        private enum ScheduleStrategy {
            /**
             * 队列中最后加入的任务最先执行
             */
            LIFO,
            /**
             * 队列中最先加入的任务最先执行
             */
            FIFO;
        }
        /**
         * 一次同时并发的数量，根据处理器数量调节
         *
         * <p>cpu count   :  1    2    3    4    8    16    32
         * <p>once(base*2):  1    2    3    4    8    16    32
         *
         * <p>一个时间段内最多并发线程个数：
         * 双核手机：2
         * 四核手机：4
         * ...
         * 计算公式如下：
         */
        private static int serialOneTime;
        /**
         * 并发最大数量，当投入的任务过多大于此值时，根据Lru规则，将最老的任务移除（将得不到执行）
         * <p>cpu count   :  1    2    3    4    8    16    32
         * <p>base(cpu+3) :  4    5    6    7    11   19    35
         * <p>max(base*16):  64   80   96   112  176  304   560
         */
        private static int serialMaxCount;
        private int cpuCount = CPU_COUNT;

        private void reSettings(int cpuCount) {
            this.cpuCount = cpuCount;
            serialOneTime = cpuCount;
            serialMaxCount = (cpuCount + 3) * 16;
        }

        public SmartSerialExecutor() {
            reSettings(CPU_COUNT);
        }

        @Override
        public synchronized void execute(final Runnable command) {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    command.run();
                    next();
                }
            };
            if (mCachedSerialExecutor.getActiveCount() < serialOneTime) {
                // 小于单次并发量直接运行
                mCachedSerialExecutor.execute(r);
            } else {
                // 如果大于并发上限，那么移除最老的任务
                if (mQueue.size() >= serialMaxCount) {
                    mQueue.pollFirst();
                }
                // 新任务放在队尾
                mQueue.offerLast(r);

                // 动态获取目前cpu处理器数目,并调整设置。
                // int proCount = Runtime.getRuntime().availableProcessors();
                // if (proCount != cpuCount) {
                // cpuCount = proCount;
                // reSettings(proCount);
                // }
            }

        }

        public synchronized void next() {
            Runnable mActive;
            switch (mStrategy) {
                case LIFO :
                    mActive = mQueue.pollLast();
                    break;
                case FIFO :
                    mActive = mQueue.pollFirst();
                    break;
                default :
                    mActive = mQueue.pollLast();
                    break;
            }
            if (mActive != null) mCachedSerialExecutor.execute(mActive);
        }

    }

}

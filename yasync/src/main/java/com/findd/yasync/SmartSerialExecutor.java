package com.findd.yasync;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 它大大改善Android自带异步任务框架的处理能力和速度。
 * 默认地，它使用LIFO（后进先出）策略来调度线程，可将最新的任务快速执行，当然你自己可以换为FIFO调度策略。
 * 这有助于用户当前任务优先完成（比如加载图片时，很容易做到当前屏幕上的图片优先加载）。
 *
 * Created by Troy on 15/4/20.
 */
public class SmartSerialExecutor implements Executor {
    /**
     * 这里使用{@link ArrayDequeCompat}当栈比{@link Stack}性能高
     */
    private ArrayDequeCompat<Runnable> mQueue;
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
    private int cpuCount;
    /**
     * 具有线程管理策略的线程池
     */
    private ThreadPoolExecutor threadPoolExecutor;

    private void reSettings(int cpuCount) {
        this.cpuCount = cpuCount;
        serialOneTime = cpuCount;
        serialMaxCount = (cpuCount + 3) * 16;
        mQueue = new ArrayDequeCompat<>(serialMaxCount);
    }

    public SmartSerialExecutor(int cpuCount, ThreadPoolExecutor threadPoolExecutor) {
        this.cpuCount = cpuCount;
        this.threadPoolExecutor = threadPoolExecutor;
        reSettings(cpuCount);
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
        if (threadPoolExecutor.getActiveCount() < serialOneTime) {
            // 小于单次并发量直接运行
            threadPoolExecutor.execute(r);
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
        if (mActive != null) threadPoolExecutor.execute(mActive);
    }

}

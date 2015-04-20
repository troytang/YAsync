package com.findd.yasync;

import android.annotation.TargetApi;
import android.os.Build;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Troy on 15/4/20.
 */
class SmartParallelExecutor implements Executor {

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
    private static int parallelOneTime;
    /**
     * 并发最大数量，当投入的任务过多大于此值时，根据Lru规则，将最老的任务移除（将得不到执行）
     * <p>cpu count   :  1    2    3    4    8    16    32
     * <p>base(cpu+3) :  4    5    6    7    11   19    35
     * <p>max(base*16):  64   80   96   112  176  304   560
     */
    private static int parallelMaxCount;
    private int cpuCount;
    /**
     * 具有线程管理策略的线程池
     */
    private ThreadPoolExecutor threadPoolExecutor;

    private void reSettings(int cpuCount) {
        this.cpuCount = cpuCount;
        parallelOneTime = cpuCount;
        parallelMaxCount = (cpuCount + 3) * 16;
        mQueue = new ArrayDequeCompat<>(parallelMaxCount);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public SmartParallelExecutor(int cupCount, ThreadPoolExecutor threadPoolExecutor) {
        this.cpuCount = cupCount;
        this.threadPoolExecutor = threadPoolExecutor;
        reSettings(cupCount);
        this.threadPoolExecutor.allowCoreThreadTimeOut(true);
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

    public synchronized void clear() {
        if (null != mQueue) {
            mQueue.clear();
        }
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
        if (threadPoolExecutor.getActiveCount() < parallelOneTime) {
            // 小于单次并发量直接运行
            threadPoolExecutor.execute(r);
        } else {
            // 如果大于并发上限，那么移除最老的任务
            if (mQueue.size() >= parallelMaxCount) {
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

}

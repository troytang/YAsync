package com.findd.yasync;

import android.annotation.TargetApi;
import android.os.Build;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import static com.findd.yasync.RunnableWrapper.WrapperListener;

/**
 * 自带策略任务调度器
 * 代理类
 * <p/>
 * Created by Troy on 15/4/20.
 */
class SmartExecutor implements Executor {
    /**
     * 这里使用{@link ArrayDequeCompat}当栈比{@link java.util.Stack}性能高
     */
    private ArrayDequeCompat<Runnable> mQueue;
    private ScheduleStrategy mStrategy = ScheduleStrategy.FIFO;

    private enum ScheduleStrategy {
        /**
         * last in first out
         */
        LIFO,
        /**
         * first in first out
         */
        FIFO
    }

    /**
     * 一次同时并发的数量，根据处理器数量调节
     * <p/>
     * <p>cpu count   :  1    2    3    4    8    16    32
     * <p>once(base*2):  1    2    3    4    8    16    32
     * <p/>
     * <p>一个时间段内最多并发线程个数：
     * 双核手机：2
     * 四核手机：4
     * ...
     * 计算公式如下：
     */
    private static int countOneTime;
    /**
     * 并发最大数量，当投入的任务过多大于此值时，根据Lru规则，将最老的任务移除（将得不到执行）
     * <p>cpu count   :  1    2    3    4    8    16    32
     * <p>base(cpu+3) :  4    5    6    7    11   19    35
     * <p>max(base*16):  64   80   96   112  176  304   560
     */
    private static int maxCount;
    private int cpuCount;
    /**
     * 具有线程管理策略的线程池
     */
    private ThreadPoolExecutor threadPoolExecutor;

    private void reSettings(int cpuCount) {
        this.cpuCount = cpuCount;
        countOneTime = cpuCount;
        maxCount = (cpuCount + 3) * 16;
        mQueue = new ArrayDequeCompat<>(maxCount);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public SmartExecutor(int cpuCount, ThreadPoolExecutor threadPoolExecutor) {
        this.cpuCount = cpuCount;
        this.threadPoolExecutor = threadPoolExecutor;
        reSettings(cpuCount);
        this.threadPoolExecutor.allowCoreThreadTimeOut(true);
    }

    public synchronized void add(Runnable command) {
        if (command instanceof RunnableWrapper) {
            switch (mStrategy) {
                case LIFO:
                    mQueue.offerLast(command);
                    break;
                case FIFO:
                    if (((RunnableWrapper) command).isNow()) {
                        mQueue.offerFirst(command);
                    } else {
                        mQueue.offerLast(command);
                    }
                    break;
                default:
                    mQueue.offerLast(command);
                    break;
            }
        } else {
            mQueue.offerLast(command);
        }
    }

    public synchronized void next() {
        Runnable mActive;
        switch (mStrategy) {
            case LIFO:
                mActive = mQueue.pollLast();
                break;
            case FIFO:
                mActive = mQueue.pollFirst();
                break;
            default:
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
        RunnableWrapper wrapper = new RunnableWrapper(command, new WrapperListener() {
            @Override
            public void runCompleted() {
                next();
            }
        });
        if (threadPoolExecutor.getActiveCount() < countOneTime) {
            // 小于单次并发量直接运行
            threadPoolExecutor.execute(wrapper);
        } else {
            // 如果大于并发上限，那么移除最老的任务
            if (mQueue.size() >= maxCount) {
                mQueue.pollFirst();
            }
            // 新任务加入到队列中
            add(wrapper);
        }
    }
}

package com.findd.yasync;

import android.annotation.TargetApi;
import android.os.Build;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import static com.findd.yasync.RunnableWrapper.WrapperListener;

/**
 * Created by Troy on 15/4/20.
 */
class SmartExecutor implements Executor {

    private ArrayDequeCompat<Runnable> mQueue;
    private ScheduleStrategy mStrategy = ScheduleStrategy.LIFO;

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
     * <p/>
     * <p>cpu count   :  1    2    3    4    8    16    32
     * <p>once(base*2):  1    2    3    4    8    16    32
     * <p/>
     */
    private static int countOneTime;
    /**
     * <p>cpu count   :  1    2    3    4    8    16    32
     * <p>base(cpu+3) :  4    5    6    7    11   19    35
     * <p>max(base*16):  64   80   96   112  176  304   560
     */
    private static int maxCount;
    private int cpuCount;
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
            // run
            threadPoolExecutor.execute(wrapper);
        } else {
            // remove old
            if (mQueue.size() >= maxCount) {
                mQueue.pollFirst();
            }
            // add
            add(wrapper);
        }
    }
}

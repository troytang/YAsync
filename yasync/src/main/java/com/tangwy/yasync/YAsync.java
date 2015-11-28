package com.tangwy.yasync;

import android.os.Handler;
import android.os.Looper;
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
        Log.i(LOG_TAG, "CPU : " + CPU_COUNT);
    }

    private static final int CORE_POOL_SIZE = CPU_COUNT;

    private static final int MAXIMUM_POOL_SIZE = Integer.MAX_VALUE;

    private static final int KEEP_ALIVE = 30;

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "YAsync #" + mCount.getAndIncrement());
        }
    };

    private static final BlockingQueue<Runnable> sPoolSerialWorkQueue = new SynchronousQueue<>();

    public static final ThreadPoolExecutor mCachedExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE,
            MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, sPoolSerialWorkQueue, sThreadFactory);

    public static final Executor mLruExecutor = new SmartExecutor(CPU_COUNT, mCachedExecutor);

    public static final ResultDelivery mResultDelivery = new ExecutorDelivery(new Handler(Looper.getMainLooper()));

    public static YAsyncTask run(YAsyncTask runnable) {
        mLruExecutor.execute(runnable);
        return runnable;
    }

    public static void cancelAll() {
        ((SmartExecutor) mLruExecutor).clear();

        mCachedExecutor.shutdown();
    }
}

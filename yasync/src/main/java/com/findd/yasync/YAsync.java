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
 * 异步调度类
 *
 * Created by troy_tang on 2014/11/4.
 */
public class YAsync {

    private static final String LOG_TAG = "YAsync";

    private static int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    static {
        Log.i(LOG_TAG, "CPU ： " + CPU_COUNT);
    }

    /**
     * 有N处理器，便长期保持N个活跃线程。
     */
    private static final int CORE_POOL_SIZE = CPU_COUNT;

    /**
     * 线程池的大小
     */
    private static final int MAXIMUM_POOL_SIZE = Integer.MAX_VALUE;

    /**
     * 空闲线程退出等待时间
     */
    private static final int KEEP_ALIVE = 30;

    /**
     * 线程工厂，所有线程由此对象创建
     */
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "YAsync #" + mCount.getAndIncrement());
        }
    };

    /**
     * 直接提交策略的任务队列
     */
    private static final BlockingQueue<Runnable> sPoolSerialWorkQueue = new SynchronousQueue<>();

    /**
     * 一个 {@link Executor} 实例可以并发执行任务.
     * 核心线程数为{@link #CORE_POOL_SIZE}，不限制并发总线程数!
     * 这就使得任务总能得到执行，且高效执行少量（<={@link #CORE_POOL_SIZE}）异步任务。
     * 线程完成任务后保持{@link #KEEP_ALIVE}秒销毁，这段时间内可重用以应付短时间内较大量并发，提升性能。
     * 它实际控制并执行线程任务。
     */
    public static final ThreadPoolExecutor mCachedExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE,
            MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, sPoolSerialWorkQueue, sThreadFactory);

    /**
     * 并发量控制: 根据cpu能力控制一段时间内并发数量，并发过量大时采用Lru方式移除旧的异步任务，默认采用LIFO策略调度线程运作，可选调度策略有LIFO、FIFO。
     * 此控制器用于直接提交策略
     */
    public static final Executor mLruExecutor = new SmartExecutor(CPU_COUNT, mCachedExecutor);

    /**
     * 对外统一接口
     *
     * @param runnable YAsyncTask实例
     */
    public static void execute(Runnable runnable){
        mLruExecutor.execute(runnable);
    }

    /**
     * 取消所有任务
     */
    public static void cancelAll() {
        // 删除控制器中队列中尚未进去线程池的任务
        ((SmartExecutor) mLruExecutor).clear();

        // 删除线程池中等待运行的任务
        mCachedExecutor.purge();

        // 删除线程池中正在运行的任务
    }
}

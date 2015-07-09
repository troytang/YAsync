package com.findd.yasync;

import android.os.Handler;
import android.os.Looper;

/**
 * 异步任务对象
 *
 * Created by troy_tang on 2014/11/4.
 */
public class YAsyncTask<TaskResult> implements Runnable {

    /** 主线程Handler */
    private static Handler mainHandler = new Handler(Looper.getMainLooper());

    // 异步任务执行体
    private AsyncAction<TaskResult> asyncAction;
    // 异步任务完成后主线程执行体
    private AsyncResult<TaskResult> asyncResult;
    // 异步任务失败原因
    private AysncFail aysncFail;
    // 异步返回的结果
    private TaskResult result;

    /**
     * 构造方法
     */
    public YAsyncTask(){

    }

    /**
     * 设置异步任务
     *
     * @param asyncAction
     * @return
     */
    public YAsyncTask<TaskResult> doInBackground(AsyncAction<TaskResult> asyncAction){
        this.asyncAction = asyncAction;
        return this;
    }

    /**
     * 设置异步任务完成后主线程的任务
     *
     * @param asyncResult
     * @return
     */
    public YAsyncTask<TaskResult> doWhenFinished(AsyncResult<TaskResult> asyncResult){
        this.asyncResult = asyncResult;
        return this;
    }

    /**
     * 设置异步任务失败处理
     *
     * @param aysncFail
     * @return
     */
    public YAsyncTask<TaskResult> doWhenFailed(AysncFail aysncFail) {
        this.aysncFail = aysncFail;
        return this;
    }

    /**
     * 把异步执行体的结果返回到主线程
     *
     * @param success 异步任务是否成功
     * @param ex 异步任务失败时的错误，若成功为null
     */
    public void onResult(final boolean success, final Exception ex) {
        if (null != mainHandler) {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (success && null != asyncResult) {
                        asyncResult.onResult(result);
                    } else if (!success && null != aysncFail){
                        aysncFail.onFailed(ex);
                    }
                }
            });
        }
    }

    @Override
    public void run() {
        if (null != asyncAction) {
            try {
                result = asyncAction.doAsync();
                onResult(true, null);
            } catch (Exception ex) {
                onResult(false, ex);
            }
        }
    }
}

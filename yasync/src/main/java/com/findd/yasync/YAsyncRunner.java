package com.findd.yasync;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.FutureTask;

/**
 * Created by troy_tang on 2014/11/4.
 */
class YAsyncRunner<TaskResult> implements Runnable{

    private static Handler uiHandler = new Handler(Looper.getMainLooper());

    // 异步任务在其他线程做的主要任务
    private AsyncAction actionInBackground;
    // 当异步任务完成了，把返回提交到主线程后所做的工作
    private AsyncResult actionOnMainThread;

    // 异步任务返回的结果
    private TaskResult result;

    /**
     * 初始化一个AsyncTask实例
     */
    public YAsyncRunner() {
    }

    public AsyncAction getActionInBackground() {
        return actionInBackground;
    }

    public AsyncResult getActionOnResult() {
        return actionOnMainThread;
    }

    /**
     * 设置在后台线程执行的任务
     * @param actionInBackground 一个AsyncAction，由YAsyncTask设置
     */
    public void setActionInBackground(AsyncAction actionInBackground) {
        this.actionInBackground = actionInBackground;
    }

    /**
     * 设置后台线程运行玩之后把返回值提交到主线程后所执行的任务
     * @param actionOnMainThread 一个AsyncResultAction，由YAsyncTask设置
     */
    public void setActionOnResult(AsyncResult actionOnMainThread) {
        this.actionOnMainThread = actionOnMainThread;
    }

    /**
     * 把运行结果发送到主线程
     */
    private void onResult() {
        if (actionOnMainThread != null) {
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    actionOnMainThread.onResult(result);
                }
            });
        }
    }

    @Override
    public void run() {
        if(actionInBackground != null) {
            result = (TaskResult) actionInBackground.doAsync();
            onResult();
        }
    }
}

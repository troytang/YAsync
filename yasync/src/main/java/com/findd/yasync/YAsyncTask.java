package com.findd.yasync;

/**
 * 异步任务对象
 *
 * Created by troy_tang on 2014/11/4.
 */
public class YAsyncTask<TaskResult> {

    // 异步任务执行体
    private AsyncAction<TaskResult> asyncAction;
    // 异步任务完成后主线程执行体
    private AsyncResult asyncResult;

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
     * 创建一个异步任务的线程
     *
     * @return
     */
    public YAsyncRunner<TaskResult> create(){
        YAsyncRunner<TaskResult> asyncRunner = new YAsyncRunner<TaskResult>();
        asyncRunner.setActionInBackground(asyncAction);
        asyncRunner.setActionOnResult(asyncResult);
        return asyncRunner;
    }
}

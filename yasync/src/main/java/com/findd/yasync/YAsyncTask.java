package com.findd.yasync;

/**
 * 异步任务对象
 *
 * Created by troy_tang on 2014/11/4.
 */
public class YAsyncTask<TaskResult> {

    private AsyncAction<TaskResult> asyncAction;
    private AsyncResultAction asyncResultAction;

    /**
     * 构造方法
     */
    public YAsyncTask(){

    }

    /**
     *
     * @param asyncAction
     * @return
     */
    public YAsyncTask<TaskResult> doInBackground(AsyncAction<TaskResult> asyncAction){
        this.asyncAction = asyncAction;
        return this;
    }

    /**
     *
     * @param asyncResultAction
     * @return
     */
    public YAsyncTask<TaskResult> doWhenFinished(AsyncResultAction asyncResultAction){
        this.asyncResultAction = asyncResultAction;
        return this;
    }

    public YAsyncRunner<TaskResult> create(){
        YAsyncRunner<TaskResult> asyncRunner = new YAsyncRunner<TaskResult>();
        asyncRunner.setActionInBackground(asyncAction);
        asyncRunner.setActionOnResult(asyncResultAction);
        return asyncRunner;
    }
}

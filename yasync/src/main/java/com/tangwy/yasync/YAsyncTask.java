package com.tangwy.yasync;

import android.os.Handler;
import android.os.Looper;

/**
 *
 * Created by troy_tang on 2014/11/4.
 */
public class YAsyncTask<TaskResult> implements Runnable {

    private static Handler mainHandler = new Handler(Looper.getMainLooper());

    private AsyncAction<TaskResult> asyncAction;
    private AsyncResult<TaskResult> asyncResult;
    private AsyncFail asyncFail;
    private TaskResult result;
    private boolean runNow;

    public YAsyncTask(){

    }

    public YAsyncTask<TaskResult> async(AsyncAction<TaskResult> asyncAction){
        this.asyncAction = asyncAction;
        return this;
    }

    public YAsyncTask<TaskResult> finished(AsyncResult<TaskResult> asyncResult){
        this.asyncResult = asyncResult;
        return this;
    }

    public YAsyncTask<TaskResult> failed(AsyncFail asyncFail) {
        this.asyncFail = asyncFail;
        return this;
    }

    public YAsyncTask<TaskResult> now() {
        this.runNow = true;
        return this;
    }

    public boolean getNow() {
        return runNow;
    }

    public void onResult(final boolean success, final Exception ex) {
        if (null != mainHandler) {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (success && null != asyncResult) {
                        asyncResult.onResult(result);
                    } else if (!success && null != asyncFail){
                        asyncFail.onFailed(ex);
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

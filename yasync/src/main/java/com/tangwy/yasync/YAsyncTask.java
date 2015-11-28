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
    private boolean isCancel;

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

    public void cancel() {
        this.isCancel = true;
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
            boolean success = false;
            Exception exception = null;
            try {
                if (!isCancel) {
                    result = asyncAction.doAsync();
                    success = true;
                }
            } catch (Exception ex) {
                success = false;
                exception = ex;
            } finally {
              if (!isCancel) {
                  onResult(success, exception);
              }
            }
        }
    }
}

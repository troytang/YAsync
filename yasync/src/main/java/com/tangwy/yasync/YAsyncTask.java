package com.tangwy.yasync;

/**
 *
 * Created by troy_tang on 2014/11/4.
 */
public class YAsyncTask<TaskResult> implements Runnable {

    private final ResultDelivery resultDelivery = YAsync.mResultDelivery;

    protected AsyncResult<TaskResult> asyncResult;
    protected AsyncFail asyncFail;
    protected Exception error;
    private AsyncAction<TaskResult> asyncAction;
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

    protected TaskResult getResult() {
        return result;
    }

    protected boolean isCancel() {
        return this.isCancel;
    }

    protected boolean isSuccess() {
        return null == error;
    }

    protected boolean getNow() {
        return runNow;
    }

    protected void onResult() {
        if (null != resultDelivery) {
            resultDelivery.postResult(this);
        } else {
            throw new NullPointerException("YAsyncTask's ResultDelivery is null!");
        }
    }

    @Override
    public void run() {
        if (null != asyncAction) {
            try {
                if (!isCancel) {
                    result = asyncAction.doAsync();
                }
            } catch (Exception ex) {
                error = ex;
            } finally {
              if (!isCancel) {
                  onResult();
              }
            }
        }
    }
}

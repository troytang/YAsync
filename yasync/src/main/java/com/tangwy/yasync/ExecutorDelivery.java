package com.tangwy.yasync;

import android.os.Handler;

import java.util.concurrent.Executor;

/**
 * Created by Troy Tang on 2015-11-28.
 */
public class ExecutorDelivery implements ResultDelivery {

    /** Used for posting responses, typically to the main thread. */
    private Executor mResultPoster;

    ExecutorDelivery(final Handler handler) {
        mResultPoster = new Executor() {
            @Override
            public void execute(Runnable command) {
                handler.post(command);
            }
        };
    }

    @Override
    public void postResult(YAsyncTask asyncTask) {
        mResultPoster.execute(new ResultDeliveryRunnable(asyncTask));
    }

    class ResultDeliveryRunnable implements Runnable {

        private YAsyncTask asyncTask;

        ResultDeliveryRunnable(YAsyncTask asyncTask) {
            this.asyncTask = asyncTask;
        }

        @Override
        public void run() {
            if (asyncTask.isCancel()) {
                return;
            }

            if (asyncTask.isSuccess() && null != asyncTask.asyncResult) {
                asyncTask.asyncResult.onResult(asyncTask.getResult());
            }

            if (!asyncTask.isSuccess() && null != asyncTask.asyncFail) {
                asyncTask.asyncFail.onFailed(asyncTask.error);
            }
        }
    }
}

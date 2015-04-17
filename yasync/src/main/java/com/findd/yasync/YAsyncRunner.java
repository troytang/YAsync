package com.findd.yasync;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;

/**
 * Created by troy_tang on 2014/11/4.
 */
public class YAsyncRunner<TaskResult> implements Runnable{

    private static Handler uiHandler = new Handler(Looper.getMainLooper());

    // Action to do in background
    private AsyncAction actionInBackground;
    // Action to do when the background action ends
    private AsyncResultAction actionOnMainThread;

    // The FutureTask created for the action
    private FutureTask asyncFutureTask;

    // The result of the background action
    private TaskResult result;

    /**
     * Instantiates a new AsyncTask
     */
    public YAsyncRunner() {
    }

    /**
     * Executes the provided code immediately on the UI Thread
     * @param onMainThreadTask Interface that wraps the code to execute
     */
    public static void doOnMainThread(final OnMainThreadTask onMainThreadTask) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                onMainThreadTask.doInUiThread();
            }
        });
    }

    /**
     * Executes the provided code immediately on a background thread
     * @param onBackgroundTask Interface that wraps the code to execute
     */
    public static void doInBackground(final OnBackgroundTask onBackgroundTask) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                onBackgroundTask.doInBackground();
            }
        }).start();
    }

    /**
     * Executes the provided code immediately on a background thread that will be submitted to the
     * provided ExecutorService
     * @param onBackgroundTask Interface that wraps the code to execute
     * @param executor Will queue the provided code
     */
    public static FutureTask doInBackground(final OnBackgroundTask onBackgroundTask, ExecutorService executor) {
        FutureTask task = (FutureTask) executor.submit(new Runnable() {
            @Override
            public void run() {
                onBackgroundTask.doInBackground();
            }
        });

        return task;
    }

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

    public AsyncAction getActionInBackground() {
        return actionInBackground;
    }

    /**
     * Specifies which action to run in background
     * @param actionInBackground the action
     */
    public void setActionInBackground(AsyncAction actionInBackground) {
        this.actionInBackground = actionInBackground;
    }

    public AsyncResultAction getActionOnResult() {
        return actionOnMainThread;
    }

    /**
     * Specifies which action to run when the background action is finished
     * @param actionOnMainThread the action
     */
    public void setActionOnResult(AsyncResultAction actionOnMainThread) {
        this.actionOnMainThread = actionOnMainThread;
    }

    @Override
    public void run() {
        if(actionInBackground != null) {
            result = (TaskResult) actionInBackground.doAsync();
            onResult();
        }
    }
}

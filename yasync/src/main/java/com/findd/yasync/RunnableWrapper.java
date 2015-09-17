package com.findd.yasync;

/**
 * Created by Troy Tang on 2015-9-16.
 */
public class RunnableWrapper implements Runnable {

    private Runnable command;
    private boolean now;
    private WrapperListener listener;

    public RunnableWrapper(Runnable command, WrapperListener listener) {
        if (command instanceof YAsyncTask && ((YAsyncTask) command).getNow()) {
            now = true;
        }
        this.command = command;
        this.listener = listener;
    }

    public boolean isNow() {
        return now;
    }

    @Override
    public void run() {
        command.run();
        listener.runCompleted();
    }

    public interface WrapperListener {
        void runCompleted();
    }
}

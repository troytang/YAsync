package com.findd.yasync;

/**
 * 异步任务失败后回调的方法，把发生的异常返回主线程
 *
 * Created by Troy Tang on 2015/7/9.
 */
public interface AysncFail {

    void onFailed(Exception ex);

}

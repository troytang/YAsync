package com.findd.yasync;

/**
 * 异步任务完成后回调的方法，范型由AsyncAction中定义，由它返回范型实例
 *
 * Created by troy_tang on 2014/11/4.
 */
public interface AsyncResult<Result> {

    void onResult(Result result);

}

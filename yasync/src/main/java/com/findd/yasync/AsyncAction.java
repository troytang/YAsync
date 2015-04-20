package com.findd.yasync;

/**
 * 异步任务执行体，范型可自定义返回类型
 *
 * Created by troy_tang on 2014/11/4.
 */
public interface AsyncAction<Result> {

    public Result doAsync();

}

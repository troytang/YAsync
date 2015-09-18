package com.findd.yasync;

/**
 * Created by troy_tang on 2014/11/4.
 */
public interface AsyncAction<Result> {

    Result doAsync() throws Exception;

}

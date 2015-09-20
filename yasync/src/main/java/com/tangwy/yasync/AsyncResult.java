package com.tangwy.yasync;

/**
 * Created by troy_tang on 2014/11/4.
 */
public interface AsyncResult<Result> {

    void onResult(Result result);

}

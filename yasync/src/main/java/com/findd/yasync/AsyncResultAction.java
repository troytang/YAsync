package com.findd.yasync;

/**
 * Created by troy_tang on 2014/11/4.
 */
public interface AsyncResultAction<ActionResult> {

    public void onResult(ActionResult result);

}

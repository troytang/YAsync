package com.findd.yasyncdemo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.findd.yasync.AsyncAction;
import com.findd.yasync.AsyncResult;
import com.findd.yasync.YAsync;
import com.findd.yasync.YAsyncTask;


/**
 * Created by Troy on 15/4/20.
 */
public class TestActivity extends Activity{

    TextView tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_test);

        tv1 = (TextView) findViewById(R.id.tv1);

        YAsync.execute(new YAsyncTask<String>().doInBackground(new AsyncAction<String>() {
            @Override
            public String doAsync() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "10s gone";
            }
        }).doWhenFinished(new AsyncResult<String>() {
            @Override
            public void onResult(String o) {
                tv1.setText(o);
            }
        }).create());
    }
}

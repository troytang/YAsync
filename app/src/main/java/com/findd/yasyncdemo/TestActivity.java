package com.findd.yasyncdemo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.findd.yasync.AsyncAction;
import com.findd.yasync.AsyncResult;
import com.findd.yasync.AsyncFail;
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

        YAsync.run(new YAsyncTask<String>().async(new AsyncAction<String>() {
            @Override
            public String doAsync() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String testStr = null;
                testStr.length();
                return "10s gone";
            }
        }).finished(new AsyncResult<String>() {
            @Override
            public void onResult(String o) {
                tv1.setText(o);
            }
        }).failed(new AsyncFail() {
            @Override
            public void onFailed(Exception ex) {
                Toast.makeText(TestActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
            }
        }));
    }
}

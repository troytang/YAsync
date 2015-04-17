package com.findd.yasyncdemo;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.findd.yasync.AsyncAction;
import com.findd.yasync.AsyncResultAction;
import com.findd.yasync.YAsync;
import com.findd.yasync.YAsyncTask;


public class MainActivity extends ActionBarActivity {

    TextView tv;
    TextView tv2;
    TextView tv3;
    TextView tv4;
    TextView tv5;
    TextView tv6;
    TextView tv7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView)findViewById(R.id.tv);
        tv2 = (TextView)findViewById(R.id.tv2);
        tv3 = (TextView)findViewById(R.id.tv3);
        tv4 = (TextView)findViewById(R.id.tv4);
        tv5 = (TextView)findViewById(R.id.tv5);
        tv6 = (TextView)findViewById(R.id.tv6);
        tv7 = (TextView)findViewById(R.id.tv7);

//        new YAsyncTask<String>().doInBackground(new AsyncAction<String>() {
//            @Override
//            public String doAsync() {
//                try {
//                    Thread.sleep(10000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                return "10s gone.";
//            }
//        }).doWhenFinished(new AsyncResultAction<String>() {
//            @Override
//            public void onResult(String o) {
//                tv.setText(o);
//            }
//        }).create().start();

        YAsync.execute(new YAsyncTask<String>().doInBackground(new AsyncAction<String>() {
            @Override
            public String doAsync() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "10s gone.";
            }
        }).doWhenFinished(new AsyncResultAction<String>() {
            @Override
            public void onResult(String o) {
                tv.setText(o);
            }
        }).create());

        YAsync.execute(new YAsyncTask<String>().doInBackground(new AsyncAction<String>() {
            @Override
            public String doAsync() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "10s gone.";
            }
        }).doWhenFinished(new AsyncResultAction<String>() {
            @Override
            public void onResult(String o) {
                tv2.setText(o);
            }
        }).create());

        YAsync.execute(new YAsyncTask<String>().doInBackground(new AsyncAction<String>() {
            @Override
            public String doAsync() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "10s gone.";
            }
        }).doWhenFinished(new AsyncResultAction<String>() {
            @Override
            public void onResult(String o) {
                tv3.setText(o);
            }
        }).create());

        YAsync.execute(new YAsyncTask<String>().doInBackground(new AsyncAction<String>() {
            @Override
            public String doAsync() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "10s gone.";
            }
        }).doWhenFinished(new AsyncResultAction<String>() {
            @Override
            public void onResult(String o) {
                tv4.setText(o);
            }
        }).create());

        YAsync.execute(new YAsyncTask<String>().doInBackground(new AsyncAction<String>() {
            @Override
            public String doAsync() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "10s gone.";
            }
        }).doWhenFinished(new AsyncResultAction<String>() {
            @Override
            public void onResult(String o) {
                tv5.setText(o);
            }
        }).create());

        YAsync.execute(new YAsyncTask<String>().doInBackground(new AsyncAction<String>() {
            @Override
            public String doAsync() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "10s gone.";
            }
        }).doWhenFinished(new AsyncResultAction<String>() {
            @Override
            public void onResult(String o) {
                tv6.setText(o);
            }
        }).create());

        YAsync.execute(new YAsyncTask<String>().doInBackground(new AsyncAction<String>() {
            @Override
            public String doAsync() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "10s gone.";
            }
        }).doWhenFinished(new AsyncResultAction<String>() {
            @Override
            public void onResult(String o) {
                tv7.setText(o);
            }
        }).create());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

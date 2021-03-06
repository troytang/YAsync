package com.tangwy.yasyncdemo;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tangwy.yasync.AsyncAction;
import com.tangwy.yasync.AsyncResult;
import com.tangwy.yasync.YAsync;
import com.tangwy.yasync.YAsyncTask;


public class MainActivity extends ActionBarActivity {

    View root;

    TextView tv;
    TextView tv2;
    TextView tv3;
    TextView tv4;
    TextView tv5;
    TextView tv6;
    TextView tv7;
    TextView tv8;
    TextView tv9;
    TextView tv10;
    TextView tv11;

    Button btnStop;
    Button btnStart;
    Button btnChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        root = findViewById(R.id.root);
        tv = (TextView)findViewById(R.id.tv);
        tv2 = (TextView)findViewById(R.id.tv2);
        tv3 = (TextView)findViewById(R.id.tv3);
        tv4 = (TextView)findViewById(R.id.tv4);
        tv5 = (TextView)findViewById(R.id.tv5);
        tv6 = (TextView)findViewById(R.id.tv6);
        tv7 = (TextView)findViewById(R.id.tv7);
        tv8 = (TextView)findViewById(R.id.tv8);
        tv9 = (TextView) findViewById(R.id.tv9);
        tv10 = (TextView) findViewById(R.id.tv10);
        tv11 = (TextView) findViewById(R.id.tv11);
        btnStart = (Button)findViewById(R.id.btn_start);
        btnStop = (Button)findViewById(R.id.btn_stop);
        btnChange = (Button)findViewById(R.id.btn_change);

        YAsync.run(new YAsyncTask<String>().async(new AsyncAction<String>() {
            @Override
            public String doAsync() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "10s gone.";
            }
        }).finished(new AsyncResult<String>() {
            @Override
            public void onResult(String o) {
                tv.setText(o);
            }
        }));

        YAsync.run(new YAsyncTask<String>().async(new AsyncAction<String>() {
            @Override
            public String doAsync() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "10s gone.";
            }
        }).finished(new AsyncResult<String>() {
            @Override
            public void onResult(String o) {
                tv2.setText(o);
            }
        }));

        YAsync.run(new YAsyncTask<String>().async(new AsyncAction<String>() {
            @Override
            public String doAsync() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "10s gone.";
            }
        }).finished(new AsyncResult<String>() {
            @Override
            public void onResult(String o) {
                tv3.setText(o);
            }
        }));

        YAsync.run(new YAsyncTask<String>().async(new AsyncAction<String>() {
            @Override
            public String doAsync() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "10s gone.";
            }
        }).finished(new AsyncResult<String>() {
            @Override
            public void onResult(String o) {
                tv4.setText(o);
            }
        }));

        YAsync.run(new YAsyncTask<String>().async(new AsyncAction<String>() {
            @Override
            public String doAsync() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "10s gone.";
            }
        }).finished(new AsyncResult<String>() {
            @Override
            public void onResult(String o) {
                tv5.setText(o);
            }
        }));

        YAsync.run(new YAsyncTask<String>().async(new AsyncAction<String>() {
            @Override
            public String doAsync() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "10s gone.";
            }
        }).finished(new AsyncResult<String>() {
            @Override
            public void onResult(String o) {
                tv6.setText(o);
            }
        }));

        YAsync.run(new YAsyncTask<String>().async(new AsyncAction<String>() {
            @Override
            public String doAsync() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "10s gone.";
            }
        }).finished(new AsyncResult<String>() {
            @Override
            public void onResult(String o) {
                tv7.setText(o);
            }
        }));

        YAsync.run(new YAsyncTask<String>().async(new AsyncAction<String>() {
            @Override
            public String doAsync() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "10s gone.";
            }
        }).finished(new AsyncResult<String>() {
            @Override
            public void onResult(String o) {
                tv8.setText(o);
            }
        }));

        YAsync.run(new YAsyncTask<String>().async(new AsyncAction<String>() {
            @Override
            public String doAsync() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "10s gone.";
            }
        }).finished(new AsyncResult<String>() {
            @Override
            public void onResult(String s) {
                tv10.setText(s);
            }
        }).now());

        YAsync.run(new YAsyncTask<String>().async(new AsyncAction<String>() {
            @Override
            public String doAsync() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "10s gone.";
            }
        }).finished(new AsyncResult<String>() {
            @Override
            public void onResult(String o) {
                tv11.setText(o);
            }
        }).now());

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YAsync.cancelAll();
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YAsync.run(new YAsyncTask<String>().async(new AsyncAction<String>() {
                    @Override
                    public String doAsync() {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return "10s gone.";
                    }
                }).finished(new AsyncResult<String>() {
                    @Override
                    public void onResult(String o) {
                        tv9.setText(o);
                    }
                }));
            }
        });

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TestActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
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

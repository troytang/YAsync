# README #

这是一个基于Android平台的异步任务框架。

### 使用方法 ###

```
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
```

### 优点 ###

* 基于Thread和Handler，真正实现并发异步而非串行异步。
* 使用线程池，优化性能。
* 可自定义任务的调度策略。

### License ###

```
Copyright (C) 2012-2014 troy tang

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
```
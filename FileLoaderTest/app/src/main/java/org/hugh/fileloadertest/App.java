package org.hugh.fileloadertest;

import android.app.Application;

import org.hugh.loader.manager.LoadManager;

/**
 * @author hugh
 * @since 2018/2/1 11:01
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LoadManager.MAX_CONCURRENCY_COUNT = 3;//指定最多3个下载任务同时执行
    }
}

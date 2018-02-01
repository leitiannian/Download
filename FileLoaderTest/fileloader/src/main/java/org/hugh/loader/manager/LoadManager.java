package org.hugh.loader.manager;

import android.content.Context;
import android.content.Intent;

import org.hugh.loader.bean.LoadInfo;
import org.hugh.loader.bean.LoadRequest;
import org.hugh.loader.service.LoadService;

import java.io.File;
import java.util.ArrayList;

/**
 * @author hugh
 * @since 2017/11/21 14:28
 */
@SuppressWarnings("unused")
public class LoadManager {
    private static LoadManager sManager;
    private static ArrayList<LoadRequest> requests = new ArrayList<>();

    private LoadManager() {

    }

    public static LoadManager getInstance() {
        if (null == sManager) {
            synchronized (LoadManager.class) {
                if (null == sManager) {
                    sManager = new LoadManager();
                }
            }
        }
        return sManager;
    }

    /**
     * 执行所有任务
     *
     * @param context 运行环境
     */
    public synchronized void execute(Context context) {
        if (requests.isEmpty()) {
            return;
        }
        Intent intent = new Intent(context, LoadService.class);
        intent.putExtra(LoadService.REQUESTS, requests);
        context.startService(intent);
        requests.clear();
    }

    /**
     * 向队列里添加一条 下载 任务
     *
     * @param url  下载地址
     * @param file 下载的本地文件
     * @return 建造者模式，用于链式调用
     */
    public LoadManager addLoad(String url, File file) {
        return addLoad(url, file, null);
    }

    /**
     * 向队列里添加一条 下载 任务，并且指定接收信息的广播action
     *
     * @param url    下载地址
     * @param file   下载的本地文件
     * @param action 接收信息的广播action
     * @return 建造者模式，用于链式调用
     */
    public LoadManager addLoad(String url, File file, String action) {
        requests.add(newRequest(url, file, action, LoadRequest.LOAD_REQUEST));
        return this;
    }

    /**
     * 向队列里添加一条 暂停 任务
     *
     * @param url  下载地址
     * @param file 下载的本地文件
     * @return 建造者模式，用于链式调用
     */
    public LoadManager addPause(String url, File file) {
        addPause(url, file, null);
        return this;
    }

    /**
     * 向队列里添加一条 暂停 任务，并且指定接收信息的广播action
     *
     * @param url    下载地址
     * @param file   下载的本地文件
     * @param action 接收信息的广播action
     * @return 建造者模式，用于链式调用
     */
    public LoadManager addPause(String url, File file, String action) {
        requests.add(newRequest(url, file, action, LoadRequest.PAUSE_REQUEST));
        return this;
    }

    private LoadRequest newRequest(String url, File file, String action, int dictate) {
        LoadRequest request = new LoadRequest();
        request.dictate = dictate;
        request.downloadInfo = new LoadInfo(url, file, action);
        return request;
    }

}

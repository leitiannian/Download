package org.hugh.loader.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.hugh.loader.bean.LoadInfo;
import org.hugh.loader.bean.LoadRequest;
import org.hugh.loader.manager.LoadExecutor;
import org.hugh.loader.task.LoadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.hugh.loader.Constant.STATUS_COMPLETE;


/**
 * download service
 *
 * @author hugh
 * @since 2017/11/21 14:28
 */

public class LoadService extends Service {
    public static final String REQUESTS = "requests";
    public static boolean canRequest = true;

    private LoadExecutor mExecutor = new LoadExecutor(3, 3,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());//executor
    private Map<String, LoadTask> mTasks = new HashMap<>();//store task.

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (canRequest) {
            canRequest = false;
            if (null != intent && intent.hasExtra(REQUESTS)) {
                try {
                    @SuppressWarnings("unchecked")
                    ArrayList<LoadRequest> requests = (ArrayList<LoadRequest>) intent.getSerializableExtra(REQUESTS);
                    if (null != requests) {
                        for (LoadRequest request : requests) {//execute the task.
                            if (null != request) {
                                executeDownload(request);
                            }
                        }
                    }
                } catch (Exception e) {
                    //do nothing
                }
            }
            canRequest = true;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * execute task.
     *
     * @param request download request.
     */
    private synchronized void executeDownload(LoadRequest request) {
        LoadInfo loadInfo = request.downloadInfo;

        //first,find the task by this id from the store list.
        LoadTask task = mTasks.get(loadInfo.getId());

        if (null == task) {//if there is no task by this id from the store list,create a task.
            if (request.dictate == LoadRequest.LOAD_REQUEST) {
                task = new LoadTask(this, loadInfo);
                mTasks.put(loadInfo.getId(), task);
            }
        } else {
            if (task.getStatus() == STATUS_COMPLETE && !loadInfo.file.exists()) {//if has download is finished,but you deleted the file.
                mTasks.remove(loadInfo.getId());
                executeDownload(request);
                return;
            }
        }

        if (null != task) {
            if (request.dictate == LoadRequest.LOAD_REQUEST) {
                mExecutor.executeTask(task);
            } else {
                task.pause();
            }
        }
    }

}
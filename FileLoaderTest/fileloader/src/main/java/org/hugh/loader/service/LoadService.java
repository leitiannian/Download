package org.hugh.loader.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.hugh.loader.bean.LoadFile;
import org.hugh.loader.bean.LoadInfo;
import org.hugh.loader.bean.LoadRequest;
import org.hugh.loader.database.DBHolder;
import org.hugh.loader.manager.LoadExecutor;
import org.hugh.loader.task.LoadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.hugh.loader.Constant.DOWNLOAD_EXTRA;
import static org.hugh.loader.Constant.STATUS_COMPLETE;
import static org.hugh.loader.Constant.STATUS_LOADING;
import static org.hugh.loader.Constant.STATUS_PAUSE;
import static org.hugh.loader.Constant.STATUS_PREPARE;


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
        DBHolder holder = new DBHolder(this);
        LoadFile loadFile = holder.getFile(loadInfo.getId());

        if (null == task) {//if there is no task by this id from the store list,create a task.
            //the file is downloading when quit the application.
            if (null != loadFile) {
                if (loadFile.downloadStatus == STATUS_LOADING || loadFile.downloadStatus == STATUS_PREPARE) {//correct the download state.
                    holder.updateState(loadFile.id, STATUS_PAUSE);
                } else if (loadFile.downloadStatus == STATUS_COMPLETE) {
                    if (loadInfo.file.exists()) {//the download has finished.
                        if (!TextUtils.isEmpty(loadInfo.action)) {
                            Intent intent = new Intent();
                            intent.setAction(loadInfo.action);
                            intent.putExtra(DOWNLOAD_EXTRA, loadFile);
                            sendBroadcast(intent);
                        }
                        return;
                    } else {
                        holder.deleteLoad(loadFile.id);
                    }
                }
            }
            //create a download task.
            if (request.dictate == LoadRequest.LOAD_REQUEST) {
                task = new LoadTask(this, holder, loadInfo);
                mTasks.put(loadInfo.getId(), task);
            }
        } else {
            if (task.getStatus() == STATUS_COMPLETE || task.getStatus() == STATUS_LOADING) {
                if (!loadInfo.file.exists()) {
                    task.pause();
                    mTasks.remove(loadInfo.getId());
                    executeDownload(request);
                    return;
                }
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
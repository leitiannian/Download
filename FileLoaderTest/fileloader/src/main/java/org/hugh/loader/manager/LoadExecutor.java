package org.hugh.loader.manager;


import android.content.Intent;

import org.hugh.loader.task.LoadTask;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.hugh.loader.Constant.DOWNLOAD_EXTRA;
import static org.hugh.loader.Constant.STATUS_FAIL;
import static org.hugh.loader.Constant.STATUS_PAUSE;
import static org.hugh.loader.Constant.STATUS_WAIT;

/**
 * @author hugh
 * @since 2017/11/21 14:28
 */

public class LoadExecutor extends ThreadPoolExecutor {
    public LoadExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public void executeTask(LoadTask task) {
        int status = task.getStatus();
        if (status == STATUS_PAUSE || status == STATUS_FAIL) {
            task.loadFile.downloadStatus = STATUS_WAIT;
            Intent intent = new Intent();
            intent.setAction(task.info.action);
            intent.putExtra(DOWNLOAD_EXTRA, task.loadFile);
            task.context.sendBroadcast(intent);
            execute(task);
        }
    }
}

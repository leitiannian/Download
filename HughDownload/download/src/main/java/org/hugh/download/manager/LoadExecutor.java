package org.hugh.download.manager;


import org.hugh.download.task.LoadTask;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.hugh.download.Constant.STATUS_FAIL;
import static org.hugh.download.Constant.STATUS_PAUSE;

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
            execute(task);
        }
    }
}

package org.hugh.loader.task;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.IntRange;

import org.hugh.loader.bean.LoadFile;
import org.hugh.loader.bean.LoadInfo;
import org.hugh.loader.database.DBHolder;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.hugh.loader.Constant.DOWNLOAD_EXTRA;
import static org.hugh.loader.Constant.STATUS_COMPLETE;
import static org.hugh.loader.Constant.STATUS_FAIL;
import static org.hugh.loader.Constant.STATUS_LOADING;
import static org.hugh.loader.Constant.STATUS_PAUSE;
import static org.hugh.loader.Constant.STATUS_PREPARE;

/**
 * @author hugh
 * @since 2017/11/21 14:28
 */

public class LoadTask implements Runnable {

    private Context context;
    private DBHolder holder;
    private LoadInfo info;
    private LoadFile loadFile;

    private boolean isPause;

    public LoadTask(Context context, LoadInfo info) {
        this.context = context;
        this.holder = new DBHolder(context);
        this.info = info;
    }

    @Override
    public void run() {
        call();
    }

    private void call() {
        //init downloadFile information.
        loadFile = new LoadFile();
        loadFile.id = info.getId();
        loadFile.downloadUrl = info.url;
        loadFile.filePath = info.file.getAbsolutePath();
        loadFile.downloadStatus = STATUS_PREPARE;

        RandomAccessFile accessFile = null;
        HttpURLConnection http = null;
        InputStream inStream = null;

        //init intent.
        Intent intent = new Intent();
        intent.setAction(info.action);

        try {
            URL sizeUrl = new URL(info.url);
            HttpURLConnection sizeHttp = (HttpURLConnection) sizeUrl.openConnection();
            sizeHttp.setRequestMethod("GET");
            sizeHttp.connect();
            long size = sizeHttp.getContentLength();
            sizeHttp.disconnect();

            if (size <= 0) {
                if (info.file.exists()) {
                    info.file.delete();
                }
                holder.deleteLoad(info.getId());
                return;
            }

            LoadFile fileInfo = holder.getFile(info.getId());
            long mark = 0;
            if (null != fileInfo && info.file.exists()) {
                mark = fileInfo.downloadMark;
                if (mark == 0) {
                    info.file.delete();
                }
            }

            accessFile = new RandomAccessFile(info.file, "rwd");

            loadFile.size = size;

            intent.putExtra(DOWNLOAD_EXTRA, loadFile);

            context.sendBroadcast(intent);
            URL url = new URL(info.url);
            http = (HttpURLConnection) url.openConnection();
            http.setConnectTimeout(10000);
            http.setRequestProperty("Connection", "Keep-Alive");
            http.setReadTimeout(10000);
            http.setRequestProperty("Range", "bytes=" + mark + "-");
            http.connect();

            inStream = http.getInputStream();
            byte[] buffer = new byte[1024 * 8];
            int offset;

            accessFile.seek(mark);
            long millis = SystemClock.uptimeMillis();
            while ((offset = inStream.read(buffer)) != -1) {
                if (isPause) {
                    loadFile.downloadStatus = STATUS_PAUSE;
                    isPause = false;
                    holder.saveFile(loadFile);
                    context.sendBroadcast(intent);
                    http.disconnect();
                    accessFile.close();
                    inStream.close();
                    return;
                }
                accessFile.write(buffer, 0, offset);
                mark += offset;
                loadFile.downloadMark = mark;
                loadFile.downloadStatus = STATUS_LOADING;
                if (SystemClock.uptimeMillis() - millis >= 1000) {
                    millis = SystemClock.uptimeMillis();
                    holder.saveFile(loadFile);
                    context.sendBroadcast(intent);
                }
            }
            loadFile.downloadStatus = STATUS_COMPLETE;
            holder.saveFile(loadFile);
            context.sendBroadcast(intent);
        } catch (Exception e) {
            loadFile.downloadStatus = STATUS_FAIL;
            holder.saveFile(loadFile);
            context.sendBroadcast(intent);
            e.printStackTrace();
        } finally {
            try {
                if (null != accessFile) {
                    accessFile.close();
                }
                if (null != inStream) {
                    inStream.close();
                }
                if (null != http) {
                    http.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void pause() {
        isPause = true;
    }

    @IntRange(from = STATUS_PREPARE, to = STATUS_FAIL)
    public int getStatus() {
        if (null != loadFile) {
            return loadFile.downloadStatus;
        }
        return STATUS_FAIL;
    }

}
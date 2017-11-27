package org.hugh.loader.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.hugh.loader.bean.LoadFile;

import java.io.File;
import java.util.ArrayList;

/**
 * @author hugh
 * @since 2017/11/21 14:28
 */

public class DBHolder {
    public Context context;
    private SQLiteDatabase mDatabase;

    public DBHolder(Context context) {
        this.context = context;
        mDatabase = new DBOpenHelper(context).getWritableDatabase();
    }

    public void saveFile(LoadFile downloadFile) {
        if (null == downloadFile) {
            return;
        }
        ContentValues values = new ContentValues();
        values.put("id", downloadFile.id);
        values.put("downloadUrl", downloadFile.downloadUrl);
        values.put("filePath", downloadFile.filePath);
        values.put("size", downloadFile.size);
        values.put("downloadMark", downloadFile.downloadMark);
        values.put("downloadStatus", downloadFile.downloadStatus);
        if (has(downloadFile.id)) {
            mDatabase.update(DBOpenHelper.TABLE_DOWNLOAD, values, "id = ?", new String[]{downloadFile.id});
        } else {
            mDatabase.insert(DBOpenHelper.TABLE_DOWNLOAD, null, values);
        }
    }

    /**
     * get the download file information.
     *
     * @param id id of the loadFile.
     * @return LoadFile.
     */
    public LoadFile getFile(String id) {
        Cursor cursor = mDatabase.query(DBOpenHelper.TABLE_DOWNLOAD, null,
                " id = ? ", new String[]{id}, null, null, null);
        LoadFile downloadFile = null;
        while (cursor.moveToNext()) {
            downloadFile = new LoadFile();
            downloadFile.id = cursor.getString(cursor.getColumnIndex("id"));
            downloadFile.downloadUrl = cursor.getString(cursor.getColumnIndex("downloadUrl"));
            downloadFile.filePath = cursor.getString(cursor.getColumnIndex("filePath"));
            downloadFile.size = cursor.getLong(cursor.getColumnIndex("size"));
            downloadFile.downloadMark = cursor.getLong(cursor.getColumnIndex("downloadMark"));
            downloadFile.downloadStatus = cursor.getInt(cursor.getColumnIndex("downloadStatus"));
            File file = new File(downloadFile.filePath);
            if (!file.exists()) {
                deleteLoad(id);
                return null;
            }
        }
        cursor.close();
        return downloadFile;
    }

    /**
     * delete the download file information by this id.
     *
     * @param id id of the loadFile.
     */
    public void deleteLoad(String id) {
        if (has(id)) {
            mDatabase.delete(DBOpenHelper.TABLE_DOWNLOAD, "id = ?", new String[]{id});
        }
    }

    private boolean has(String id) {
        Cursor cursor = mDatabase.query(DBOpenHelper.TABLE_DOWNLOAD, null,
                " id = ? ", new String[]{id}, null, null, null);
        boolean has = cursor.moveToNext();
        cursor.close();
        return has;
    }

    public ArrayList<LoadFile> getFiles(String url) {
        ArrayList<LoadFile> downloadFiles = new ArrayList<>();
        Cursor cursor = mDatabase.query(DBOpenHelper.TABLE_DOWNLOAD, null,
                " downloadUrl = ? ", new String[]{url}, null, null, null);
        while (cursor.moveToNext()) {
            LoadFile downloadFile = new LoadFile();
            downloadFile.id = cursor.getString(cursor.getColumnIndex("id"));
            downloadFile.downloadUrl = cursor.getString(cursor.getColumnIndex("downloadUrl"));
            downloadFile.filePath = cursor.getString(cursor.getColumnIndex("filePath"));
            downloadFile.size = cursor.getLong(cursor.getColumnIndex("size"));
            downloadFile.downloadMark = cursor.getLong(cursor.getColumnIndex("downloadMark"));
            downloadFile.downloadStatus = cursor.getInt(cursor.getColumnIndex("downloadStatus"));
            File file = new File(downloadFile.filePath);
            if (file.exists()) {
                downloadFiles.add(downloadFile);
            }
        }
        cursor.close();
        return downloadFiles;
    }
}

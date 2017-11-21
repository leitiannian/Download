package org.hugh.download.database;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author hugh
 * @since 2017/11/21 14:28
 */

public class DBOpenHelper extends SQLiteOpenHelper {
    static final String TABLE_DOWNLOAD = "hugh_download";// 用来缓存多线程信息的表
    private static final String DATABASE = "hugh_download.db";

    DBOpenHelper(Context context) {
        super(context, DATABASE, null, getVersionCode(context));
    }

    /**
     * 获取版本code
     */
    private static int getVersionCode(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String download = "create table if not exists " + TABLE_DOWNLOAD
                + "(id varchar(500),downloadUrl varchar(100),filePath varchar(100),size integer,downloadMark integer,downloadStatus integer)";
        db.execSQL(download);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_DOWNLOAD);
        onCreate(db);
    }
}

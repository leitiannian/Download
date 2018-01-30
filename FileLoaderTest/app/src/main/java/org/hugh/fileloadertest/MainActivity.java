package org.hugh.fileloadertest;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.hugh.loader.Constant;
import org.hugh.loader.bean.LoadFile;
import org.hugh.loader.manager.LoadManager;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSION_FOR_QQ = 0x101;
    private static final int REQUEST_PERMISSION_FOR_WX = 0x102;
    private static final int STATUS_WAIT = 0;
    private static final int STATUS_LOADING = 1;
    private static final int STATUS_PAUSE = 2;
    private static final int STATUS_PREPARE = 3;
    private static final int STATUS_COMPLETE = 4;
    private LoadManager manager;
    private TextView mWXDownloadInfo;
    private TextView mQQDownloadInfo;
    private ProgressBar mWXPro;
    private ProgressBar mQQPro;
    private Button mWXBtn;
    private Button mQQBtn;
    private int mQQStatus;
    private int mWXStatus;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (null != intent) {
                switch (intent.getAction()) {
                    case "action_wx_download"://下载微信apk
                        LoadFile wxFile = (LoadFile) intent.getSerializableExtra(Constant.DOWNLOAD_EXTRA);
                        if (wxFile.downloadStatus == Constant.STATUS_WAIT) {
                            mWXBtn.setText("等待下载");
                            mWXBtn.setTag(STATUS_WAIT);
                        } else if (wxFile.downloadStatus == Constant.STATUS_PREPARE) {
                            mWXBtn.setText("正在准备下载");
                            mWXBtn.setTag(STATUS_PREPARE);
                        } else if (wxFile.downloadStatus == Constant.STATUS_LOADING) {
                            mWXBtn.setText("暂停");
                            mWXBtn.setTag(STATUS_LOADING);
                        } else if (wxFile.downloadStatus == Constant.STATUS_COMPLETE) {
                            mWXBtn.setText("完成");
                            mWXBtn.setTag(STATUS_COMPLETE);
                        } else {
                            mWXBtn.setText("下载");
                            mWXBtn.setTag(STATUS_PAUSE);
                        }
                        float wxPro = (float) (wxFile.downloadMark * 1.0 / wxFile.size);
                        int wxProgress = (int) (wxPro * 100);
                        float wxDownloadSize = wxFile.downloadMark / 1024.0f / 1024;
                        float wxSize = wxFile.size / 1024.0f / 1024;
                        mWXDownloadInfo.setText("微信已下载\n" + wxDownloadSize + "M/" + wxSize + "M\n" + "( " + wxProgress + "% )");
                        mWXPro.setProgress(wxProgress);
                        break;
                    case "action_qq_download"://下载QQ apk
                        LoadFile qqFile = (LoadFile) intent.getSerializableExtra(Constant.DOWNLOAD_EXTRA);
                        if (qqFile.downloadStatus == Constant.STATUS_WAIT) {
                            mQQBtn.setText("等待下载");
                            mQQBtn.setTag(STATUS_WAIT);
                        } else if (qqFile.downloadStatus == Constant.STATUS_PREPARE) {
                            mQQBtn.setText("正在准备下载");
                            mQQBtn.setTag(STATUS_PREPARE);
                        } else if (qqFile.downloadStatus == Constant.STATUS_LOADING) {
                            mQQBtn.setText("暂停");
                            mQQBtn.setTag(STATUS_LOADING);
                        } else if (qqFile.downloadStatus == Constant.STATUS_COMPLETE) {
                            mQQBtn.setText("完成");
                            mQQBtn.setTag(STATUS_COMPLETE);
                        } else {
                            mQQBtn.setText("下载");
                            mQQBtn.setTag(STATUS_PAUSE);
                        }
                        float qqPro = (float) (qqFile.downloadMark * 1.0 / qqFile.size);
                        int qqProgress = (int) (qqPro * 100);
                        float qqDownloadSize = qqFile.downloadMark / 1024.0f / 1024;
                        float qqSize = qqFile.size / 1024.0f / 1024;
                        mQQDownloadInfo.setText("QQ已下载\n" + qqDownloadSize + "M/" + qqSize + "M\n" + "( " + qqProgress + "% )");
                        mQQPro.setProgress(qqProgress);
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter filter = new IntentFilter();
        filter.addAction("action_wy_download");
        filter.addAction("action_wx_download");
        filter.addAction("action_qq_download");
        registerReceiver(receiver, filter);

        mWXPro = (ProgressBar) findViewById(R.id.wx_pro);
        mQQPro = (ProgressBar) findViewById(R.id.qq_pro);

        mWXDownloadInfo = (TextView) findViewById(R.id.wx);
        mQQDownloadInfo = (TextView) findViewById(R.id.qq);

        mWXBtn = (Button) findViewById(R.id.wx_btn);
        mWXBtn.setTag(mWXStatus = STATUS_PAUSE);
        mQQBtn = (Button) findViewById(R.id.qq_btn);
        mQQBtn.setTag(mQQStatus = STATUS_PAUSE);

        manager = LoadManager.getInstance();

        //微信
        mWXBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWXStatus = (int) view.getTag();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    boolean readPermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
                    boolean writePermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
                    boolean cameraPermission = checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
                    ArrayList<String> permissions = new ArrayList<>();
                    boolean shouldCheck = false;
                    if (!readPermission) {
                        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                        shouldCheck = true;
                    }
                    if (!writePermission) {
                        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        shouldCheck = true;
                    }
                    if (!cameraPermission) {
                        permissions.add(Manifest.permission.CAMERA);
                        shouldCheck = true;
                    }
                    if (shouldCheck) {
                        String[] per = new String[permissions.size()];
                        permissions.toArray(per);
                        requestPermissions(per, REQUEST_PERMISSION_FOR_WX);
                        return;
                    }
                }
                executeWX();
            }
        });

        //QQ
        mQQBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mQQStatus = (int) view.getTag();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    boolean readPermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
                    boolean writePermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
                    boolean cameraPermission = checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
                    ArrayList<String> permissions = new ArrayList<>();
                    boolean shouldCheck = false;
                    if (!readPermission) {
                        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                        shouldCheck = true;
                    }
                    if (!writePermission) {
                        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        shouldCheck = true;
                    }
                    if (!cameraPermission) {
                        permissions.add(Manifest.permission.CAMERA);
                        shouldCheck = true;
                    }
                    if (shouldCheck) {
                        String[] per = new String[permissions.size()];
                        permissions.toArray(per);
                        requestPermissions(per, REQUEST_PERMISSION_FOR_QQ);
                        return;
                    }
                }
                executeQQ();
            }
        });
    }

    private void executeWX() {
        String WXUrl = "http://gdown.baidu.com/data/wisegame/db931ac9ff9e61a9/weixin_1140.apk";
        File WXFile = new File(getDir(), "微信.apk");
        if (mWXStatus == STATUS_PAUSE) {
            manager.addLoad(WXUrl, WXFile, "action_wx_download")
                    .execute(MainActivity.this);
        } else {
            manager.addPause(WXUrl, WXFile)
                    .execute(MainActivity.this);
        }
    }

    private void executeQQ() {
        String QQUrl = "http://gdown.baidu.com/data/wisegame/f28ba370126f3605/QQ_744.apk";
        File QQFile = new File(getDir(), "QQ.apk");
        if (mQQStatus == STATUS_PAUSE) {
            manager.addLoad(QQUrl, QQFile, "action_qq_download")
                    .execute(MainActivity.this);
        } else {
            manager.addPause(QQUrl, QQFile)
                    .execute(MainActivity.this);
        }
    }

    private File getDir() {
        File dir = new File(getExternalCacheDir(), "下载");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION_FOR_QQ:
                boolean hasPermission2QQ = true;
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        hasPermission2QQ = false;
                    }
                }
                if (!hasPermission2QQ) {
                    Toast.makeText(this, "下载文件需要读写权限", Toast.LENGTH_SHORT).show();
                } else {
                    executeQQ();
                }
                break;
            case REQUEST_PERMISSION_FOR_WX:
                boolean hasPermission2wx = true;
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        hasPermission2wx = false;
                    }
                }
                if (!hasPermission2wx) {
                    Toast.makeText(this, "下载文件需要读写权限", Toast.LENGTH_SHORT).show();
                } else {
                    executeWX();
                }
                break;
        }
    }
}

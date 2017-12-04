package org.hugh.fileloadertest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.hugh.loader.Constant;
import org.hugh.loader.bean.LoadFile;
import org.hugh.loader.database.DBHolder;
import org.hugh.loader.manager.LoadManager;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private TextView mWYDownloadInfo;
    private TextView mWXDownloadInfo;
    private TextView mQQDownloadInfo;

    private ProgressBar mWYPro;
    private ProgressBar mWXPro;
    private ProgressBar mQQPro;

    private Button mWYBtn;
    private Button mWXBtn;
    private Button mQQBtn;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (null != intent) {
                switch (intent.getAction()) {
                    case "action_wy_download"://下载网易云apk
                        LoadFile loadFile1 = (LoadFile) intent.getSerializableExtra(Constant.DOWNLOAD_EXTRA);
                        float wyPro = (float) (loadFile1.downloadMark * 1.0 / loadFile1.size);
                        int wyProgress = (int) (wyPro * 100);
                        float wyDownloadSize = loadFile1.downloadMark / 1024.0f / 1024;
                        float wySize = loadFile1.size / 1024.0f / 1024;
                        if (loadFile1.downloadStatus == Constant.STATUS_PREPARE) {
                            mWYBtn.setText("正在准备下载");
                        } else if (loadFile1.downloadStatus == Constant.STATUS_LOADING) {
                            mWYBtn.setText("暂停");
                        } else if (loadFile1.downloadStatus == Constant.STATUS_COMPLETE) {
                            mWYBtn.setText("完成");
                        } else {
                            mWYBtn.setText("下载");
                        }
                        mWYDownloadInfo.setText("网易云apk已下载" + wyDownloadSize + "M/" + wySize + "M\n" + "( " + wyProgress + "% )");
                        mWYPro.setProgress(wyProgress);
                        break;
                    case "action_wx_download"://下载微信apk
                        LoadFile wxFile = (LoadFile) intent.getSerializableExtra(Constant.DOWNLOAD_EXTRA);
                        if (wxFile.downloadStatus == Constant.STATUS_PREPARE) {
                            mWXBtn.setText("正在准备下载");
                        } else if (wxFile.downloadStatus == Constant.STATUS_LOADING) {
                            mWXBtn.setText("暂停");
                        } else if (wxFile.downloadStatus == Constant.STATUS_COMPLETE) {
                            mWXBtn.setText("完成");
                        } else {
                            mWXBtn.setText("下载");
                        }
                        float wxPro = (float) (wxFile.downloadMark * 1.0 / wxFile.size);
                        int wxProgress = (int) (wxPro * 100);
                        float wxDownloadSize = wxFile.downloadMark / 1024.0f / 1024;
                        float wxSize = wxFile.size / 1024.0f / 1024;
                        mWXDownloadInfo.setText("微信已下载" + wxDownloadSize + "M/" + wxSize + "M\n" + "( " + wxProgress + "% )");
                        mWXPro.setProgress(wxProgress);
                        break;
                    case "action_qq_download"://下载QQ apk
                        LoadFile qqFile = (LoadFile) intent.getSerializableExtra(Constant.DOWNLOAD_EXTRA);
                        if (qqFile.downloadStatus == Constant.STATUS_PREPARE) {
                            mQQBtn.setText("正在准备下载");
                        } else if (qqFile.downloadStatus == Constant.STATUS_LOADING) {
                            mQQBtn.setText("暂停");
                        } else if (qqFile.downloadStatus == Constant.STATUS_COMPLETE) {
                            mQQBtn.setText("完成");
                        } else {
                            mQQBtn.setText("下载");
                        }
                        float qqPro = (float) (qqFile.downloadMark * 1.0 / qqFile.size);
                        int qqProgress = (int) (qqPro * 100);
                        float qqDownloadSize = qqFile.downloadMark / 1024.0f / 1024;
                        float qqSize = qqFile.size / 1024.0f / 1024;
                        mQQDownloadInfo.setText("QQ已下载" + qqDownloadSize + "M/" + qqSize + "M\n" + "( " + qqProgress + "% )");
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

        mWYPro = (ProgressBar) findViewById(R.id.wy_pro);
        mWXPro = (ProgressBar) findViewById(R.id.wx_pro);
        mQQPro = (ProgressBar) findViewById(R.id.qq_pro);

        mWYDownloadInfo = (TextView) findViewById(R.id.wy);
        mWXDownloadInfo = (TextView) findViewById(R.id.wx);
        mQQDownloadInfo = (TextView) findViewById(R.id.qq);

        mWYBtn = (Button) findViewById(R.id.wy_btn);
        mWXBtn = (Button) findViewById(R.id.wx_btn);
        mQQBtn = (Button) findViewById(R.id.qq_btn);

        final LoadManager manager = LoadManager.getInstance();

        final DBHolder holder = new DBHolder(this);

        //网易云
        final String WYUrl = "http://gdown.baidu.com/data/wisegame/e44001b8cb260aa5/wangyiyunyinle_103.apk";
        final File WYFile = new File(getDir(), "网易云.apk");
        mWYBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadFile loadFile = holder.getFile(WYUrl + WYFile.getAbsolutePath());
                if (null == loadFile) {
                    manager.addLoad(WYUrl, WYFile, "action_wy_download")
                            .execute(MainActivity.this);
                    return;
                }
                if (loadFile.downloadStatus == Constant.STATUS_PAUSE || loadFile.downloadStatus == Constant.STATUS_FAIL) {
                    manager.addLoad(WYUrl, WYFile, "action_wy_download")
                            .execute(MainActivity.this);
                } else if (loadFile.downloadStatus == Constant.STATUS_COMPLETE) {
                    float downloadSize = loadFile.downloadMark / 1024.0f / 1024;
                    float size = loadFile.size / 1024.0f / 1024;
                    mWYDownloadInfo.setText("网易云apk已下载" + downloadSize + "M/" + size + "M\n" + "( " + 100 + "% )");
                    mWYPro.setProgress(100);
                }
                if (loadFile.downloadStatus == Constant.STATUS_LOADING) {
                    manager.addPause(WYUrl, WYFile)
                            .execute(MainActivity.this);
                } else if (loadFile.downloadStatus == Constant.STATUS_COMPLETE) {
                    float downloadSize = loadFile.downloadMark / 1024.0f / 1024;
                    float size = loadFile.size / 1024.0f / 1024;
                    mWYDownloadInfo.setText("网易云apk已下载" + downloadSize + "M/" + size + "M\n" + "( " + 100 + "% )");
                    mWYPro.setProgress(100);
                } else {
                    manager.addLoad(WYUrl, WYFile, "action_wy_download")
                            .execute(MainActivity.this);
                }
            }
        });

        //微信
        final String WXUrl = "http://gdown.baidu.com/data/wisegame/db931ac9ff9e61a9/weixin_1140.apk";
        final File WXFile = new File(getDir(), "微信.apk");
        mWXBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadFile loadFile = holder.getFile(WXUrl + WXFile.getAbsolutePath());
                if (null == loadFile) {
                    manager.addLoad(WXUrl, WXFile, "action_wx_download")
                            .execute(MainActivity.this);
                    return;
                }
                if (loadFile.downloadStatus == Constant.STATUS_LOADING) {
                    manager.addPause(WXUrl, WXFile)
                            .execute(MainActivity.this);
                } else if (loadFile.downloadStatus == Constant.STATUS_COMPLETE) {
                    float downloadSize = loadFile.downloadMark / 1024.0f / 1024;
                    float size = loadFile.size / 1024.0f / 1024;
                    mWXDownloadInfo.setText("微信已下载" + downloadSize + "M/" + size + "M\n" + "( " + 100 + "% )");
                    mWXPro.setProgress(100);
                } else {
                    manager.addLoad(WXUrl, WXFile, "action_wx_download")
                            .execute(MainActivity.this);
                }
            }
        });

        //QQ
        final String QQUrl = "http://gdown.baidu.com/data/wisegame/f28ba370126f3605/QQ_744.apk";
        final File QQFile = new File(getDir(), "QQ.apk");
        mQQBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadFile loadFile = holder.getFile(QQUrl + QQFile.getAbsolutePath());
                if (null == loadFile) {
                    manager.addLoad(QQUrl, QQFile, "action_qq_download")
                            .execute(MainActivity.this);
                    return;
                }
                if (loadFile.downloadStatus == Constant.STATUS_LOADING) {
                    manager.addPause(QQUrl, QQFile)
                            .execute(MainActivity.this);
                } else if (loadFile.downloadStatus == Constant.STATUS_COMPLETE) {
                    float downloadSize = loadFile.downloadMark / 1024.0f / 1024;
                    float size = loadFile.size / 1024.0f / 1024;
                    mQQDownloadInfo.setText("QQ已下载" + downloadSize + "M/" + size + "M\n" + "( " + 100 + "% )");
                    mQQPro.setProgress(100);
                } else {
                    manager.addLoad(QQUrl, QQFile, "action_qq_download")
                            .execute(MainActivity.this);
                }
            }
        });
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
}

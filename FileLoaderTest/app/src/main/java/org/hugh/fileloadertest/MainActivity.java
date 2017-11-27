package org.hugh.fileloadertest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
    private TextView downloadText1;
    private TextView downloadText2;
    private TextView downloadText3;

    private ProgressBar progressBar1;
    private ProgressBar progressBar2;
    private ProgressBar progressBar3;

    private Button button1;
    private Button button2;
    private Button button3;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("hugh", "onReceive");
            if (null != intent) {
                Log.d("hugh", "getAction" + intent.getAction());
                switch (intent.getAction()) {
                    case "action_file1":
                        LoadFile loadFile1 = (LoadFile) intent.getSerializableExtra(Constant.DOWNLOAD_EXTRA);
                        Log.d("hugh", "loadFile1 " + loadFile1.size);
                        if (loadFile1.downloadStatus == Constant.STATUS_LOADING) {
                            button1.setText("暂停");
                        } else if (loadFile1.downloadStatus == Constant.STATUS_COMPLETE) {
                            button1.setText("完成");
                        } else {
                            button1.setText("下载");
                        }
                        float p1 = (float) (loadFile1.downloadMark * 1.0 / loadFile1.size);
                        int pro1 = (int) (p1 * 100);
                        downloadText1.setText("图片1已下载" + "( " + pro1 + "% )");
                        progressBar1.setProgress(pro1);
                        Log.d("hugh", "路径" + loadFile1.filePath);
                        break;
                    case "action_file2":
                        LoadFile loadFile2 = (LoadFile) intent.getSerializableExtra(Constant.DOWNLOAD_EXTRA);
                        if (loadFile2.downloadStatus == Constant.STATUS_LOADING) {
                            button2.setText("暂停");
                        } else if (loadFile2.downloadStatus == Constant.STATUS_COMPLETE) {
                            button2.setText("完成");
                        } else {
                            button2.setText("下载");
                        }
                        float p2 = (float) (loadFile2.downloadMark * 1.0 / loadFile2.size);
                        int pro2 = (int) (p2 * 100);
                        downloadText2.setText("图片2已下载" + "( " + pro2 + "% )");
                        progressBar2.setProgress(pro2);
                        break;
                    case "action_file3":
                        LoadFile loadFile3 = (LoadFile) intent.getSerializableExtra(Constant.DOWNLOAD_EXTRA);
                        if (loadFile3.downloadStatus == Constant.STATUS_LOADING) {
                            button3.setText("暂停");
                        } else if (loadFile3.downloadStatus == Constant.STATUS_COMPLETE) {
                            button3.setText("完成");
                        } else {
                            button3.setText("下载");
                        }
                        float p3 = (float) (loadFile3.downloadMark * 1.0 / loadFile3.size);
                        int pro3 = (int) (p3 * 100);
                        downloadText3.setText("图片3已下载" + "( " + pro3 + "% )");
                        progressBar3.setProgress(pro3);
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
        filter.addAction("action_file1");
        filter.addAction("action_file2");
        filter.addAction("action_file3");
        registerReceiver(receiver, filter);

        progressBar1 = (ProgressBar) findViewById(R.id.progress1);
        progressBar2 = (ProgressBar) findViewById(R.id.progress2);
        progressBar3 = (ProgressBar) findViewById(R.id.progress3);

        downloadText1 = (TextView) findViewById(R.id.text1);
        downloadText2 = (TextView) findViewById(R.id.text2);
        downloadText3 = (TextView) findViewById(R.id.text3);

        button1 = (Button) findViewById(R.id.download1);
        button2 = (Button) findViewById(R.id.download2);
        button3 = (Button) findViewById(R.id.download3);

        final LoadManager manager = LoadManager.getInstance();

        final DBHolder holder = new DBHolder(this);

        //文件1
        final String url1 = "http://gdown.baidu.com/data/wisegame/e44001b8cb260aa5/wangyiyunyinle_103.apk";
        final File file1 = new File(getDir(), "网易云.apk");

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadFile loadFile = holder.getFile(url1 + file1.getAbsolutePath());

                if (null == loadFile) {
                    manager.addLoad(url1, file1, "action_file1")
                            .execute(MainActivity.this);
                    return;
                }
                if (loadFile.downloadStatus == Constant.STATUS_LOADING) {

                    manager.addPause(url1, file1)
                            .execute(MainActivity.this);

                } else if (loadFile.downloadStatus == Constant.STATUS_COMPLETE) {

                    downloadText1.setText("网易云已下载" + "( " + 100 + "% )");
                    progressBar1.setProgress(100);

                } else {

                    manager.addLoad(url1, file1, "action_file1")
                            .execute(MainActivity.this);

                }
            }
        });

        //文件2
        final String url2 = "http://b.hiphotos.baidu.com/zhidao/pic/item/a8014c086e061d9583ac971e79f40ad162d9ca2a.jpg";
        final File file2 = new File(getDir(), "图片1.jpg");
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadFile loadFile = holder.getFile(url2 + file2.getAbsolutePath());

                if (null == loadFile) {
                    manager.addLoad(url2, file2, "action_file2")
                            .execute(MainActivity.this);
                    return;
                }

                if (loadFile.downloadStatus == Constant.STATUS_LOADING) {

                    manager.addPause(url2, file2)
                            .execute(MainActivity.this);

                } else if (loadFile.downloadStatus == Constant.STATUS_COMPLETE) {

                    downloadText2.setText("图片1已下载" + "( " + 100 + "% )");
                    progressBar2.setProgress(100);

                } else {
                    manager.addLoad(url2, file2, "action_file2")
                            .execute(MainActivity.this);
                }
            }
        });

        //文件3
        final String url3 = "http://down.699pic.com/photo/00053/2794.jpg?_upt=0adf52761511780579&_upd=532794.jpg";
        final File file3 = new File(getDir(), "大图.jpg");
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadFile loadFile = holder.getFile(url3 + file3.getAbsolutePath());

                if (null == loadFile) {
                    manager.addLoad(url3, file3, "action_file3")
                            .execute(MainActivity.this);
                    return;
                }

                if (loadFile.downloadStatus == Constant.STATUS_LOADING) {

                    manager.addPause(url3, file3)
                            .execute(MainActivity.this);

                } else if (loadFile.downloadStatus == Constant.STATUS_COMPLETE) {

                    downloadText3.setText("大图已下载" + "( " + 100 + "% )");
                    progressBar3.setProgress(100);

                } else {
                    manager.addLoad(url3, file3, "action_file3")
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

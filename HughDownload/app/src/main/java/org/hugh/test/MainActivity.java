package org.hugh.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.hugh.download.Constant;
import org.hugh.download.bean.LoadFile;
import org.hugh.download.manager.LoadManager;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private TextView textView;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (TextUtils.equals(intent.getAction(), "123") && intent.hasExtra(Constant.DOWNLOAD_EXTRA)) {
                LoadFile loadFile = (LoadFile) intent.getSerializableExtra(Constant.DOWNLOAD_EXTRA);
                if (null != loadFile) {
                    Log.d("hugh", "下载广播接收：大小: " + loadFile.size + "  下载：" + loadFile.downloadMark);
                    textView.setText(loadFile.downloadMark + "/" + loadFile.size);
                } else {
                    Log.d("hugh", "下载广播接收：失败");
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final LoadManager manager = LoadManager.getInstance();
        textView = (TextView) findViewById(R.id.text);

        IntentFilter filter = new IntentFilter();
        filter.addAction("123");
        registerReceiver(receiver, filter);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = getDownloadFile("坦坦尼克号.jpg");
                manager.addLoad("https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1511255056&di=02ba8b6d5a040b43c33ebe92105bdfad&src=http://scimg.jb51.net/allimg/160721/103-160H1102450364.jpg"
                        , file, "123")
                        .execute(MainActivity.this);

            }
        });
    }

    private File getDownloadFile(String fileName) {
        File file = new File(getExternalCacheDir(), fileName);
        return file;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}

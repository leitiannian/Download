## 导入项目
```gradle
compile 'org.hugh.loader:fileloader:1.1.0'
```
## 效果图
![断点下载的使用效果](https://github.com/hy-freedom/pic/raw/master/Download.gif)

## 说明
> * 代码非常简单。
> * 主要提供[LoadManager](https://github.com/hy-freedom/Download/blob/master/FileLoaderTest/fileloader/src/main/java/org/hugh/loader/manager/LoadManager.java)给外部管理下载任务。
> * 支持多任务的断点下载。
> * 下载过程的信息接收使用广播发送，在执行下载的时候可以指定广播的action（可以在静态注册的广播中接收下载信息并且自己编写Notification在通知栏展示下载进度给用户）。
> * 可以指定最多几个下载同时执行。

## 代码使用
> 在项目的Application中配置“最多n个下载任务同时执行”，其他的下载任务则在队列之中。
```java
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LoadManager.MAX_CONCURRENCY_COUNT = 3;//指定最多3个下载任务同时执行
    }
}
```
> 在执行下载之前，先要获取LoadManager实例：
```java
LoadManager manager = LoadManager.getInstance();
```
> 添加下载任务(下载任务以“url + file.getAbsolutePath()”作为唯一标识id)：
```java
manager.addLoad(url, file);//url为下载地址，file为本地下载文件用于接收网络文件
       .addLoad(url2, file2, action);//action为及时接收下载信息的广播action
```
> 添加暂停任务：
```java
manager.addPause(url, file);//url为下载地址，file为本地下载文件用于接收网络文件
       .addPause(url2, file2, action);//action为及时接收下载信息的广播action
```
> 执行下载任务队列：
```java
manager.execute(context);
```

```java
//你也可以一起执行
manager.addLoad(url, file);
       .addLoad(url2, file2, action2)
       .addPause(url3, file3);
       .addPause(url4, file4, action3)
       .execute(context);
```
> 广播接收：
```java
private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (null != intent && null != intent.getAction()) {
                switch (intent.getAction()) {
                    case LOAD_ACTION:
                        LoadFile loadFile = (LoadFile) intent.getSerializableExtra(Constant.DOWNLOAD_EXTRA);

                        int status = loadFile.downloadStatus;//文件的下载状态
                        long fileSize = loadFile.size;//文件总大小
                        long loadedSize = loadFile.downloadMark;//已下载了多大
                        String id = loadFile.id;//数据库中此文件的id
                        String url = loadFile.downloadUrl;//文件的网络url
                        String path = loadFile.filePath;//文件下载的手机本地路径

                        switch (status) {
                            case Constant.STATUS_WAIT://等待下载

                                break;
                            case Constant.STATUS_PREPARE://正在准备下载

                                break;
                            case Constant.STATUS_LOADING://正在下载中...

                                break;
                            case Constant.STATUS_COMPLETE://下载完成

                                break;
                            default://暂停中

                                break;
                        }
                        break;
                }
            }
        }
    };
```

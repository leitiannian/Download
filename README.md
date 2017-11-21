# 导入项目
	compile 'org.hugh.loader:download:1.0.0'

# 使用代码
### 说明：外部只需要使用 LoadManager 就可以创建、执行下载任务。
#### 1、 获取 LoadManager 实例
```java
LoadManager manager = LoadManager.getInstance();
```
#### 2、 addLoad方法：添加一条“下载”任务。任务以“url+文件绝对路径”作为唯一标识。
```java
//添加一条下载任务
manager.addLoad(url,file);

//添加多条下载任务
manager.addLoad(url1,file1)
	.addLoad(url2,file2)
	.addLoad(url3,file3);
```
#### 3、 addPause方法：添加一条“暂停”任务。
```java
//添加一条暂停任务
manager.addPause(url,file);

//添加多条暂停任务
manager.addPause(url1,file1)
	.addPause(url2,file2)
	.addPause(url3,file3);
```

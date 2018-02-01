## 导入项目
```gradle
compile 'org.hugh.loader:fileloader:1.0.3'
```
## 效果图
![断点下载的使用效果](https://github.com/hy-freedom/pic/raw/master/WarnDialog.gif)

## 说明
#### 工具只针对最简单的下载文件，以及暂停继续等处理。


## 使用代码
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
#### 4、 execute方法：执行所有任务，调用execute之后会清空当前请求列表，下次就需要重新addLoad及addPause。
```java
//通过execute方法，将之前添加的“下载”任务及“暂停”任务，一起批量处理。
manager.execute(context);

//例如
manager.addLoad(url1,file1)
	.addLoad(url2,file2)
	.addPause(url3,file3)
	.execute(context);
	
//上面代码执行的是“下载url1的网络文件到file1中，下载url2的网络文件到file2中，暂停url3的下载任务”。

```

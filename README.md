# 导入项目
	compile 'org.hugh.loader:download:1.0.0'

# 使用代码
### 说明：外部只需要使用LoadManager就可以创建、执行下载任务。

#### 方法1、获取LoadManager实例
```java
LoadManager manager = LoadManager.getInstance();
```
#### 方法2、通过LoadManager添加下载任务。任务以“url+文件绝对路径”作为唯一标识
```java
manager.addLoad(url,file);
```
#### 方法3、通过LoadManager添加暂停任务
```java
manager.addPause(url,file);
```

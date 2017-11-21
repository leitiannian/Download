# 导入项目
	compile 'org.hugh.loader:download:1.0.0'

# 使用代码
### 说明：外部只需要使用 LoadManager 就可以创建、执行下载任务。
#### 1、获取 LoadManager 实例
```java
LoadManager manager = LoadManager.getInstance();
```
#### 2、addLoad方法：添加一条“下载”任务。任务以“url+文件绝对路径”作为唯一标识。
```java
manager.addLoad(url,file);
```
#### 3、addPause方法：添加一条“暂停”任务。
```java
manager.addPause(url,file);
```

# 导入项目
	compile 'org.hugh.dialog:warndialog:1.0.0'
	
# 效果图
![WarnDialog的使用效果](https://github.com/hy-freedom/pic/raw/master/WarnDialog.gif)

# 使用代码
### 说明：WarnDialog采用类似安卓原生AlertDialog的建造者模式。
#### 方法
```java
setCancelable(boolean cancelable)//点击外面是否可以消失弹框（包括手机返回按键）
setCanceledOnTouchOutside(boolean canceledOnTouchOutside)//点击外面是否可以消失弹框
setRadius(float radius)//设置弹框圆角（同时设置四个圆角）
setRadius(float leftTopRadius, float rightTopRadius, float leftBottomRadius, float rightBottomRadius)//为四个圆角设置不同的弧度
setWidthRadio(float widthRadio)//设置弹框的宽占屏幕宽的百分之多少（0-1） 比如设置0.5，则弹框宽相当于屏幕宽的一半
setIcon(@DrawableRes int iconRes)//给弹框设置标题栏的小图标
setTitle(String title)//设置标题
setMessage(String message)//设置弹框消息内容
setMessageGravity(int gravity)//设置弹框消息内容显示位置（默认居中，文本靠左显示）
setYes(String yes, ClickObserver yesObserver)//设置右边按钮，并设置监听（默认无监听）
setNo(String no, ClickObserver noObserver)//设置左边按钮，并设置监听（默认监听，点击dialog消失）
setTitleTextColor(@ColorInt int titleTextColor)//设置Title文字颜色
setTitleBackgroundColor(@ColorInt int titleBackgroundColor)//设置Title背景颜色
setMessageTextColor(@ColorInt int messageTextColor)//设置消息内容字体颜色
setMessageBackgroundColor(@ColorInt int messageBackgroundColor)//设置消息内容背景颜色
setYesButtonColor(@ColorInt int color, @ColorInt int pressColor)//设置右侧按钮的字体颜色（第二个参数为按压后的颜色）
setYesButtonBackgroundColor(@ColorInt int backgroundColor, @ColorInt int pressBackgroundColor)//设置右侧按钮的背景颜色（第二个参数为按压后的颜色）
setNoButtonColor(@ColorInt int color, @ColorInt int pressColor)//设置左侧按钮的字体颜色（第二个参数为按压后的颜色）
setNoButtonBackgroundColor(@ColorInt int backgroundColor, @ColorInt int pressBackgroundColor)//设置左侧按钮的背景颜色（第二个参数为按压后的颜色）
```

##### 可以在Application中设置默认配置
比如想要全局的WarnDialog的Title文字颜色为蓝色，按钮文字的大小为13sp，确定按钮的按压颜色为红色。就可以使用下面配置：
```java
WarnDialog.defaultConfig.setYesButtonPressColor(Color.RED)
.setBtnSize(13)
.setTitleTextColor(Color.BLUE);
```

##### 例子：上面效果图的双按钮的弹框
```java
new WarnDialog.Builder(MainActivity.this)
                        .setMessage("这是一个双按钮的弹框！")
                        .setNo("取消")
                        .setYes("哦，好吧")
                        .setYesButtonColor(Color.BLUE, Color.BLACK)
                        .setYesButtonBackgroundColor(Color.GRAY, Color.CYAN)
                        .setNoButtonColor(Color.GREEN, Color.GRAY)
                        .setTitle("提示")
                        .show();

# FloatWord
单词记忆工具，在win桌面上浮动单词。每隔一段时间自动切换单词，这样不知不觉中就记住了一些单词了，解放程序员记单词的时间。
主要功能：
- 添加单词、删除单词、查看所有单词列表
- 设置单词切换时间

![图片演示](https://github.com/shadon178/FloatWord/blob/master/img/floatword.PNG)

启动方式：
直接运行cn.sz.pxd.floatword.WordFrame类即可。
启动参数为word.csv文件路径，该文件就在项目根目录下。

原理：
主要使用csv文件保存单词信息，每隔一段时间自动切换单词，并播放单词音频文件。

## 单词音频文件
单词音频文件是从网络上下载，然后存储在本地的resources/sound目录下。

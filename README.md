# AI新闻助手(AI-News-Assistant)
## 一、简介
基于 Java17 + Spring Ai + Ollama 的AI新闻助手,能够生成大模型简写概括后的新闻概要，并一键发送到各个平台（目前只对接了微信公众号）。
## 二、部署要求
1. Java17+
2. Ollama
## 三、快速启动
1、下载项目代码

2、修改配置文件 application.yml 中的model

![image](./document/image/model-config.png)

请求 ChatController.java 中的接口就能使用。

3、一键生成内容并发生送到微信公众号

需要要配置 application.yml 中微信公众号的参数

接口：WxOfficialAccountController.java 中的 /addDraft 


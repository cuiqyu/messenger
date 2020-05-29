# 项目介绍
本项目主要是用来提供给用户发送短信验证吗的restful接口的项目，短信第三方平台对接的`阿里云短信平台`（使用阿里云提供的SDK的方式对接）。
1. 项目采用`springboot`框架结构搭建，添加了全局`统一异常处理`和`统一返回值处理`（可自行开启和关闭）。使用`AOP切面的方式`来记录接口的请求与返回信息的日志。
2. 利用`Guava Cache`缓存框架来实现一些需要临时缓存和自动过期策略的内容。
3. 使用(maven-assembly)定制化打包插件，方便项目的整体打包与部署，编写`server.sh`启动脚本方便的处理项目的启动。

## 使用方式
1. 如需正常发送短信，需要提供阿里云短信所需的基本参数信息，如（`accessKeyId`、`accessKeySecret`、`signName`、`templateCode`），修改 `resources/config/sms_config.yml`下的配置。
2. 项目通过`maven`打包后直接将生成的`messenger.zip`包上传到服务器并解压，修改`server.sh`的可执行权限`chmod u+x server.sh`后，通过命令`./server.sh start`启动、`./server.sh stop`停止、`./server.sh restart`重启。
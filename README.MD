![banner](https://capsule-render.vercel.app/api?type=waving&height=300&color=gradient&text=NeraBBS&desc=一款%20Material%20Degisn%203%20风格的云原生微服务论坛&descAlignY=65&textBg=false)

# NeraBBS

一款 Material Degisn 3 风格的云原生微服务论坛。

## 特性

- 基于 Java 21 + Vue 3 开发，前后端分离
- Docker Compose + Kubernetes 部署支持
- 支持 Passkey 通行密钥认证
- 支持楼中楼评论、热度排行榜、帖子搜索等功能

## 项目结构

- **/binary-tools** 工具文件集
- **/common** 通用逻辑模块
- **/config** 后端配置文件
- **/dev** 开发环境配置
- **/frontend** 前端模块
- **/gateway** 应用程序网关模块
- **/kubernetes-manifests** Kubernetes 清单文件
- **/lib-grpc** grpc protobuf 定义模块
- **/middleware-config** 分布式配置中心服务模块
- **/service-auth** 用户鉴权服务模块
- **/service-comment** 评论服务模块
- **/service-oss** 对象存储服务模块
- **/service-post** 论坛主题服务模块
- **/service-search** 搜索、热度排行榜、点击量统计服务模块
- **/service-user-profile** 用户个人资料服务模块
- **/service-user** 用户服务模块
- **/service-webauthn** Webauthn（Passkey）服务模块


## 快速开始

[NeraBBS Docs](https://github.com/shaokeyibb/NeraBBS-Docs/blob/master/guide/getting-started.md)

## 详细了解本项目

[DeepWiki](https://deepwiki.com/shaokeyibb/nerabbs)

## 开源协议

本项目基于 [GPLv3](LICENSE) 协议分发。

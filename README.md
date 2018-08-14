
## 1. 简介
这是一个简单的OAuth2.0的服务器，实现了OAuth 2.0的四种通用授权方式，可以用于应用的统一单点登录、权限控制。

项目提供了相应的客户端、web filter、网关组件，实现应用的分布式和集中式权限控制，并给出各种场景下的使用样例，包括，

- 纯前端项目（jquery）
- 后端web服务
- 前后端分离项目
- 等等

方便应用开发快速对接。

## 2. 为什么有这个项目

OAuth 2.0作为一个业界的授权代理模式，已经有广泛的应用。其在业界有很多较好的实现，无论是spring security/shiro，还是keycloak/gravitee/mitreid connect等等，都是非常不错并值得借鉴的授权应用框架。但是，我们希望在如下方面能够有更好的功能支持，

- 简单的技术框架，更好的扩展点（identity provider/token issue/security control），支持高可用、高并发场景
- 可以应用于纯前端项目、纯后端项目、后端Web项目、前后端分离项目，提供最佳实践
- 提供简单的java客户端、spring filter、网关组件，实现应用的分布式权限控制和集中式权限控制
- 颁发Jwt Token，里面的信息内容可以自定义填充和解析，支持单向解密

这也是这个项目的源起。

更多授权应用框架的功能对比见下面。

## 3. 和其它授权应用框架的比较

|  | keycloak | gravitee | spring security  | mitreid connect | 本项目 |
| ---:  | ---  |  --- | ---   | :---  | :---  |
| 授权方式 | oidc + oauth2 + saml + exchange | oidc + oauth2 | oauth2 | oidc + oauth2 | oauth2 |
| Token的颁发实现 |  自定义  | Spring Authorization Server + 自定义oidc实现 | Spring Authorization Server | Spring Authorization Server + 自定义token颁发 |  自定义token颁发  |
| Token类别 |  JWT  | JWT | UUID | JWT |  JWT |
| Token单向解密 |  支持 | 不支持 | 不支持 | 不支持 | 支持（待实现） |
| 权限控制  | java web filter | java web filter | spring filter chain | spring filter chain | spring filter chain |
| 认证源 |   ldap + db + social(oauth2)   |  ldap + db + social(oauth2)  |  ldap + db + social(oauth2)  | 无 |  ldap + db  |
| 扩展插件机制  |  java spi   | 自定义spi | spring aop + filter configurer | 无 | spring bean |
| 客户端支持  | 前端+后端(丰富的adapter) | 无 | spring security - resource server |  自定义 | 自定义 |
| 是否有域控 | 有（realm）  |  有（domain） | 无  | 无  | 有（domain） |
| 主要应用场景 | 单点登录（adapter），用户访问控制  |  api访问控制 | 单纯的oauth2  | 单纯的oidc+oauth2  | 单点登录和api访问控制 |


表格中，
- OIDC是OpenID Connect的简称，由OpenID Foundation发布的一个基于OAuth 2.0上的认证开放标准，其包含一个核心的规范和多个可选的规范实现。
- oauth2是指OAuth 2.0的授权框架，其主要定义了四种授权方式的规范实现。
- Spring Authorization Server是指Spring Security OAuth2.0提供的OAuth2.0服务器实现。

## 4. 环境准备
请使用JDK8 + Maven3 + Node 6.9.1 + Mysql。

## 5. 项目代码结构和构建

本项目采用前后端分离技术，
- 前端使用vue + vue router + vuex + #lement UI
- 后端使用spring boot web

### 5.1 项目代码结构

```
- README.md
- LICENSE
+ oauth-core （公共类库）
+ oauth-server OAuth2后端服务
+ oauth-front  OAuth2前端服务
+ oauth-client 客户端、提供Java Client和Spring Fitler等对接类库，方便应用接入
  + oauth-java-client java客户端，用于访问OAuth Api接口，实现获取和刷新授权令牌等操作
  + oauth-spring-web-filter 分布式权限控制（filter）
  + oauth-spring-boot-autoconfigure 实现相关组件的spring boot自动配置，主要包括java client和 web filter。
  + oauth-spring-boot-websupport 提供通用的后端controller接口，方便token的获取、刷新、吊销等操作
  + oauth-gateway 通过Spring Zuul实现简单的集中式权限控制网关（待实现）
+ sample 演示项目
  + demo-front-jquery 纯前端项目，使用OAuth2.0 implicit Grant方式，演示登录功能
  + demo-front-vue 前端使用vue框架，后端使用express服务器，使用OAuth2.0 Authorization Code Grant方式，演示用户登录功能
  + demo-spring-boot-web 使用spring web开发一个web服务，后端提供静态页面，使用OAuth2.0 Resource Owner Credential Grant方式，演示用户登录功能
  + demo-front-vue-spring-boot-web (authorization code + spring web) 前后端分离，前端使用vue框架，后端使用spring boot开发web服务，使用OAuth2.0 Authorization Code Grant授权方式，演示用户登录功能
  + demo-web-service (client credential + spring web) 纯后端项目，使用OAuth2.0 Client Credential Grant授权方式，演示后端服务之间的api接口调用
    + resource-server 资源服务器
    + resource-client 访问资源服务器的一个应用
+ docs 项目文档
  - arch design 项目架构等相关PPT演示文档
```

### 5.2 项目构建

整个项目主要分前后端的构建，下面给出简单的步骤，更详细的构建步骤请参考各个项目的README文件。

#### 5.2.1 前端构建

1. 使用nodejs 6.9.1进行构建
2. 构建命令

``` bash
npm run build
```

3. 开发运行命令

``` bash
npm run build
```

#### 5.2.2 后端构建

1. 使用JDk 8 + Maven 3.3.9 + MySQL
2. 构建命令

``` bash
mvn clean package
```

3. 运行命令

``` bash
java -jar oauth-0.0.1-SNAPSHOT.jar
```

4. 设置项目版本

``` bash
mvn versions:set -DnewVersion=1.0.1-SNAPSHOT
```

## 6. 一分钟快速入门 Getting Started
TBD

## 7. 演示使用

在演示之前，请启动OAuth应用前后端web服务。

演示项目结构如下，

| 样例项目 | 前端框架 | 后端框架 | OAuth2.0授权方式 | 前端服务端口 | 后端服务端口 |
| --- | --- | --- | --- | --- | --- |
| demo-front-jquery | 纯前端 | 无 | Implicit Grant | 8600 | 无 |
| demo-front-vue | 前端vue框架 + express服务器 | 无 | Authorization Code Grant | 8400 | 无 |
| demo-spring-boot-web | 无 | 后端spring框架 + 后端静态登录页面 | Resource Owner Credential Grant | 无 | 8100 |
| demo-front-vue-spring-boot-web| 前端vue框架 + express服务器|后端spring框架 |Authorization Code Grant | 8888 | 8080 |
| demo-web-service  | 无 | 后端spring框架 + 后端静态页面 | Client Credential Grant | 无 | 8200/8300 |

更详细的演示说明，请进入各个演示项目，阅读各项目里的README文件进行下一步的演示。

## 8. 注意事项

在使用Java 9来运行OAuth2后端服务，发现会出现如下报错信息，
```
Caused by: java.lang.ClassNotFoundException: javax.xml.bind.JAXBException
        at java.base/java.net.URLClassLoader.findClass(Unknown Source) ~[na:na]
        at java.base/java.lang.ClassLoader.loadClass(Unknown Source) ~[na:na]
        at org.springframework.boot.loader.LaunchedURLClassLoader.loadClass(LaunchedURLClassLoader.java:94) ~[oauth-server-1.0-SNAPSHOT.jar:1.0-SNAPSHOT]
        at java.base/java.lang.ClassLoader.loadClass(Unknown Source) ~[na:na]
        ... 34 common frames omitted
```
为了避免该问题，请先使用Java 8运行当前项目。

## 9. 联系 Contact
我们的邮箱地址：peipeihh@qq.com，欢迎来信联系。

## 10. 开源许可协议 License
Apache License 2.0

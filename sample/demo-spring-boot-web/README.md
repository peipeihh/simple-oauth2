
## 1. 简介

这是一个演示项目，使用spring boot web搭建一个web服务，后端提供静态登录页面，用户在当前web服务应用进行登录（不跳转到oauth server）。

整个授权对接oauth server所提供的oauth 2.0 resource owner grant授权方法，实现用户登录功能。这种授权方式一般适用于高可信的web应用中。

## 2. 项目的结构和构建

```
- pom.xml Maven项目构建文档
- README.md 使用说明文档
+ src/main/java
  + com.pphh.oauth.sample
    - App 演示程序
+ src/main/resources
  - application.properties
  + static
    - index.html 首页
    - login.html 登录页面
    - logout.html 注销登录
```

#### 2.1 项目的构建

构建命令：mvn compile package
启动命令：java -jar ./target/xxx.jar

#### 2.2 项目的配置

在项目中pom.xml文件添加了如下依赖，

```
<dependency>
    <groupId>com.pphh.demo</groupId>
    <artifactId>oauth-spring-boot-autoconfigure</artifactId>
    <version>${auth.version}</version>
</dependency>
```

并在src/main/resources/application添加了如下配置，

```
# oauth client settings
oauth.client.id = demo
oauth.client.secret = 50dN92

# oauth spring filter settings
oauth.server.url = http://localhost:8090
oauth.spring.filter.token.store.type = cookie
oauth.spring.filter.token.name = oauth-token
oauth.spring.filter.skip.url = /login.html,/logout.html,/index.html,/api/login,/api/logout
```

上述配置将启用oauth client和filter的spring自动化装配。

## 3. 演示

1. 准备工作
   - 根据oauth-server项目的README文档，启动simple oauth后端web服务。
   - 根据oauth-front项目的README文档，启动simple oauth前端web服务。
   - 若以项目缺省配置，simple oauth前端和后端分别启动在如下两个地址，
   ```
   前端服务 http://localhost
   后端服务 http://localhost:8090
   ```
   - 本演示项目将根据上面的两个地址进行授权登录跳转。

2. 注册应用
   - 请登录oauth前端页面，应该能看到auth server已经注册一个缺省的demo应用。
     * 应用查看列表：[http://localhost/#/dev/myclient](http://localhost/#/dev/myclient)

   - 若没有发现demo应用，可以手动注册应用，
     * client id = demo
     * 重定向返回地址 = .* （其含义为simple oauth接受demo client指定的任何返回地址）
     * 注册成功后，更新配置中的client id/secret

3. 启动演示应用
   - 启动命令：java -jar ./target/demo-spring-boot-web-1.0-SNAPSHOT.jar
   - 若启动成功，应用服务将运行在：http://localhost:9005
   - 打开浏览器，访问首页地址[http://localhost:9005](http://localhost:9005)
   - 在首页点击“获取用户信息”按钮，将弹出“无法获取用户信息，请先登录”的提示框。
   - 点击登录页面的链接，跳转到登录页面，输入用户名和密码，若一切正常，则将弹出“登录成功”的提示框。若出现问题，则会提示“登录失败”的信息。
   - 回到首页，重新点击“获取用户信息”按钮，将弹出“当前登录用户是test”的提示框。
   - 点击注销登录页面的链接，跳转到注销登录页面。点击“登出”按钮，将弹出“已成功登出”的提示框。
   - 回到首页，再次点击“获取用户信息”按钮，将提示用户未登录信息。

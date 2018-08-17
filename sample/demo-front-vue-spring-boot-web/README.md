
## 1. 简介

这是一个演示项目，前后端分离架构设计，使用spring boot搭建后端web服务，使用vue前端技术栈开发前端静态页面，对接oauth server所提供的oauth 2.0 authorization code grant授权方式实现用户登录功能。

## 2. 项目的结构和构建

```
- pom.xml Maven项目构建文档
- README.md 使用说明文档
+ src/main/java
  + com.pphh.oauth.sample
    - Application 后端演示应用服务
    + src/main/resources
      - application.properties 应用配置，包括授权服务器地址、过滤器类型、特殊url、client id/secret等
+ front-vue 前端项目
  - package.json NPM构建配置文件
  - webpack.config.js 前端webpack打包配置
  + src
    - index.html 前端应用主页面
    - main.js 前端应用入口
    + api 对后端API的接口调用
    + assets 静态资源文件
    + components VUE组件
    + pages 页面组件
    + router 前端路由
    + store 数据模型层
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
# oauth spring client settings
oauth.server.url = http://localhost

# oauth spring filter settings
oauth.spring.filter.type = all-check-by-skip
oauth.spring.filter.token.store.type = header
oauth.spring.filter.token.name = jwt-token
oauth.spring.filter.special.urls = GET&.*

# oauth spring support settings
oauth.client.id = demo
oauth.client.callback = http://localhost:9006/#/login
oauth.client.authorization = Basic ZGVtbzo1MGROOTI=
```

上述配置将启用oauth client和filter的spring自动化装配，同时启用spring web support（提供token的获取、刷新、注销）。

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

3. 启动后端服务
   - 启动命令：java -jar ./target/demo-front-vue-spring-boot-web-1.0-SNAPSHOT.jar
   - 若启动成功，应用服务将运行在：http://localhost:9007

3. 启动前端服务
   - 进入到目录中front-vue中
   - 执行前端应用运行命令：npm run dev
   - 打开浏览器，访问前端应用地址：http://localhost:9006
   - 点击登录按钮，将跳转到simple oauth的授权界面：http://localhost/#/authorize，点击同意按钮。
     * 注：若simple oauth没有登录，则需要先登录，再点击同意授权按钮，登录账号缺省为admin/admin，详情请查看simple auth项目readme文件。
   - 若一切正常，simple oauth将跳转回当前前端演示应用地址，并完成登录，显示登录账号。

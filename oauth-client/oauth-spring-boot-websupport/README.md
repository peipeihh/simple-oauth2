

## 1. 简介
   对于OAuth接入方，后端服务需要提供获取、刷新、吊销token的API接口，这是一个共同需求，本项目为spring boot项目提供后端的接口实现，方便用户的使用。

## 2. 引入依赖
   对于maven项目，在pom.xml项目中添加了如下依赖类库，
   ```
   <dependency>
      <groupId>com.pphh.demo</groupId>
      <artifactId>oauth-spring-boot-websupport</artifactId>
      <version>1.0-SNAPSHOT</version>
   </dependency>
   ```

## 3. 使用说明

### 3.1 自动化配置oauth client
    在项目的配置文件（src/main/resources/application.properties）中，添加如下配置项，
    ```
    oauth.server.url = http://localhost
    ```
    url指向授权服务API地址。

### 3.2 自动化配置client注册信息

    应用首先要到OAuth授权服务器注册，注册后将获取到如下信息，
    - client id 应用ID
    - client authorization 应用授权
    - call back 应用回调地址

    在项目的配置文件（src/main/resources/application.properties）中，添加如下配置项，
    ```
    oauth.client.callback = http://localhost:8888/#/login
    oauth.client.id = demo
    oauth.client.authorization = Basic ZGVtbzo1MGROOTI=
    ```
    上述是应用获取、刷新、吊销token所需要的配置信息。

### 3.3 如何使用
    请参考使用样例sample/demo-front-vue-spring-boot-web，更深入了解使用方法。

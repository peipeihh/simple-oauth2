
## 1. 简介

这是一个演示项目，里面包括两个子项目，

- resource client 资源客户端，向资源服务发起访问调用，获取资源信息
- resource server 资源服务器

两个子项目都提供web服务访问，其中资源服务器的访问接口受到权限控制，而客户端先向oauth server获取授权，拿到访问令牌后能够成功访问资源服务器接口。

资源客户端对接Simple OAuth所提供的oauth 2.0 client credentials grant授权方式，获取授权和访问令牌。

## 2. 项目的结构和构建

```
- pom.xml Maven项目构建文档
- README.md 使用说明文档
+ resource-client 资源客户端应用
  - pom.xml
  + src/main/java
    - ResourceClient
  + src/main/resources
    - application.properties
+ resource-server 资源服务器，受到授权保护
  - pom.xml
  + src/main/java
    - ResourceServer
  + src/main/resources
    - application.properties
```

#### 2.1 项目的构建

构建命令：mvn compile package
启动命令：java -jar ./target/xxx.jar

#### 2.2 资源服务器的配置

在资源服务器项目中，resource-server/pom.xml文件添加了如下依赖，

```
<dependency>
    <groupId>com.pphh.demo</groupId>
    <artifactId>oauth-spring-boot-autoconfigure</artifactId>
    <version>${auth.version}</version>
</dependency>
```

并在resource-server/src/main/resources/application添加了如下过滤器配置，

```
# oauth spring filter settings - remote api
oauth.server.url = http://localhost:8090

# oauth spring filter settings
oauth.spring.filter.type = all-check-by-skip
oauth.spring.filter.token.store.type = header
oauth.spring.filter.token.name = resource-token
oauth.spring.filter.special.urls = /login
```

上述配置，使得除了/login，其它访问路径都将受到auth filter的保护。

#### 2.3 资源客户端的配置

在资源客户端项目中，resource-client/pom.xml文件添加了如下依赖，

```
<dependency>
    <groupId>com.pphh.demo</groupId>
    <artifactId>oauth-spring-boot-autoconfigure</artifactId>
    <version>${auth.version}</version>
</dependency>
```

并在resource-client/src/main/resources/application添加了如下配置，

```
# oauth spring filter settings - remote api
oauth.server.url = http://localhost:8090

# oauth client settings
oauth.client.id = demo
oauth.client.secret = 50dN92
```

上述配置声明了客户端的client id/secret，使得客户端可以通过client id/secret向oauth.server.url申请授权码和访问令牌。

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

3. 启动资源服务器
   - 启动命令：java -jar ./resource-server/target/resource-server-1.0-SNAPSHOT.jar
   - 若启动成功，资源服务器将运行在：http://localhost:9003
   - 打开浏览器，访问[http://localhost:9003/login](http://localhost:9003/login)，将能正常获取接口信息
   - 打开浏览器，访问[http://localhost:9003/hello](http://localhost:9003/hello)，将无法访问接口，收到400的错误响应。这是由于hello接口已经受到auth filter保护，需要请求中带有合法的访问令牌才能正常访问该接口。

4. 启动资源客户端
   - 启动命令：java -jar ./resource-client/target/resource-client-1.0-SNAPSHOT.jar
   - 若启动成功，资源客户端将运行在：http://localhost:9004
   - 打开浏览器，访问[http://localhost:9004/hello](http://localhost:9004/hello)，将能正常获取接口信息。该接口将会先向授权服务器获取访问令牌，然后访问resource server服务器受保护的hello接口，并将响应信息返回。

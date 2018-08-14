

## 1. 简介
   本项目实现oauth client和oauth filter在spring boot项目中的自动化配置，方便用户的使用。

## 2. 引入依赖
   对于maven项目，在pom.xml项目中添加了如下依赖类库，
   ```
   <dependency>
      <groupId>com.pphh.demo</groupId>
      <artifactId>oauth-spring-boot-autoconfigure</artifactId>
      <version>1.0-SNAPSHOT</version>
   </dependency>
   ```

## 3. 使用说明

### 3.1 自动化配置oauth client
    在项目的配置文件（src/main/resources/application.properties）中，添加如下配置项，
    ```
    oauth.server.url = http://localhost
    ```

    然后在spring boot项目中，可以通过自动装配的方式获取oauth client实例。
    ```
    @Autowired
    private OAuth2EndpointApi oAuthApi;
    ```

    接下来就可以使用oAuthApi来访问授权服务API。

### 3.2 自动化配置oauth filter

    在项目的配置文件（src/main/resources/application.properties）中，添加如下配置项，
    ```
    oauth.spring.filter.type = all-check-by-skip
    oauth.spring.filter.token.store.type = header
    oauth.spring.filter.token.name = oauth-token
    oauth.spring.filter.special.urls =
    oauth.spring.filter.audit.userinfo = audit-userinfo
    ```

    各个配置含义和使用说明如下，
    - oauth.spring.filter.type 必配项，过滤器类型，有all-skip-by-check和all-check-by-skip两种类型，配合特殊urls一起工作。
      * all-skip-by-check 跳过所有请求，但检查指定的特殊urls
      * all-check-by-skip 检查所有请求，但跳过特殊指定的urls
    - oauth.spring.filter.token.store.type 令牌在请求中的存储类型，有header和cookie两种类型，缺省为header
    - oauth.spring.filter.token.name 令牌在header/cookie的标识名，缺省为oauth-token
    - oauth.spring.filter.special.urls 特殊urls，多个URL可以通过逗号分开

    特殊urls的使用样例，
    - .*  所有请求
    - /test.* 以test开头命名的所有请求
    - GET\&.* 所有GET请求
    - GET\&.\*,PUT\&.\* 所有GET和PUT请求

    上述配置将对不同场景下oauth filter使用需求。其中，若下面两个配置项为空的话，则过滤器不会初始化。
    - oauth.spring.filter.type 过滤器类型
    - oauth.server.url 远程授权服务地址

    过滤器检查请求中的令牌，将有如下行为，
    - 若令牌为空，否则返回400（BAD_REQUEST）的错误响应
    - 若令牌不合法或者已失效，则返回401（UNAUTHORIZED）的错误响应
    - 若令牌合法有效，则对请求放行


### 3.3 如何使用
    请参考各个使用样例（sample目录下），更深入了解使用方法。

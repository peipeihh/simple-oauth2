

## 1. 项目构建

   本项目为后端spring boot项目，技术栈为spring mvc + spring boot + spring hibernate jpa + mysql。

   构建命令：mvn clean compile

## 2. 数据库配置

   请执行数据库配置脚本，
   - src/main/resource/init.sql

   该脚本将初始化所需的表，并添加如下信息，
   - 缺省账号：admin/admin
   - 演示所需的client：demo app( id = demo, authorization = Basic ZGVtbzo1MGROOTI=)

## 3. 启动应用

   启动命令：java -jar oauth-server-1.0-SNAPSHOT.jar

   若应用启动成功，在浏览器中打开如下访问地址，
   - http://localhost:8090/health
   可以看到如下信息，
   ```
   {
       "status": "UP",
       "diskSpace": {
           "status": "UP",
           "total": 314572795904,
           "free": 158274048000,
           "threshold": 10485760
       },
       "db": {
           "status": "UP",
           "database": "MySQL",
           "hello": 1
       }
   }
  ```

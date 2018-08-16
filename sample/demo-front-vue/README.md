
## 1. 简介

这是一个演示项目，使用前端vue框架（vue 2.0 + vue.router + vuex），对接Simple OAuth所提供的oauth 2.0 authorization grant授权方式，实现用户登录功能。

## 2. 项目的结构和构建

```
- README.md 使用说明文档
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

#### 2.1 安装node并配置npm源

登录node官方网站安装node 3.10.8+。

配置npm源为淘宝源

```
npm set registry "https://registry.npm.taobao.org/"
```

配置后可以通过npm config list查看。

#### 2.2 前端项目构建运行命令

构建文件：./package.json

下载依赖包：npm install，执行成功后将会把express依赖包下载到/node_modules目录。

构建命令：npm run build，构建成功后将会生成./dist目录。

运行命令：npm run dev，运行成功后可以通过[http://localhost:9002](http://localhost:9002)访问应用

注：应用的启动端口在./webpack.config.js文件中devServer.port选项所配置。

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
     * 应用查看地址：(http://localhost/#/dev/myclient)[http://localhost/#/dev/myclient]

   - 若没有发现demo应用，可以手动注册应用，
     * client id = demo
     * 重定向返回地址 = .* （其含义为simple oauth接受demo client指定的任何返回地址）

3. 启动当前演示项目
   - 执行前端应用运行命令：npm run dev
   - 打开浏览器，访问前端应用地址：http://localhost:9002
   - 点击登录按钮，将跳转到simple oauth的授权界面：http://localhost/#/authorize，点击同意按钮。
     * 注：若simple oauth没有登录，则需要先登录，再点击同意授权按钮，登录账号缺省为admin/admin，详情请查看simple auth项目readme文件。
   - 若一切正常，simple oauth将跳转回当前前端演示应用地址，并完成登录，显示登录账号。

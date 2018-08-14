
## 1. 项目构建
   本项目为前端node项目，技术栈为vue 2.0 + vue.router + vuex + bootstrap-vue/element + axios + element ui + echarts。

### 1.1 安装node并配置npm源
    登录node官方网站安装node 3.10.8+。

    配置npm源为淘宝源，
    - npm set registry "https://registry.npm.taobao.org/"

    配置后可以通过npm config list查看。

### 1.2 前端项目构建
    构建文件：./package.json

    构建命令：npm install

## 2. 启动前端
   命令：npm run dev

   启动配置在./webpack.config.js文件中的devServer.host和devServer.port选项，默认启动在80端口。

## 3. 访问前端并登录
   启动完毕后，在浏览器中打开如下访问地址，
   - http://localhost
   然后使用初始账号admin/admin来登录。

   注：登录功能需要后端服务启动，请见oauth-server的README文件。

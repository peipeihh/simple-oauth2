<template>
    <div class="login-wrapper">
        <div class="login-form">
            <div class="login-form-title">AUTH登录</div>
            <div class="login-form-input">

                <el-form v-if=" formChoice === 'LOGIN' " :model="loginForm" :rules="rules" ref="loginForm"
                         label-width="0px"
                         class="demo-ruleForm">
                    <el-form-item prop="username">
                        <el-input v-model="loginForm.username" placeholder="请输入域账号"></el-input>
                    </el-form-item>
                    <el-form-item prop="password">
                        <el-input type="password" v-model="loginForm.password" placeholder="请输入密码"
                                  @keyup.enter.native="submitForm('loginForm')"></el-input>
                    </el-form-item>
                    <div class="login-btn">
                        <el-button type="primary" @click="submitForm('loginForm')">登录</el-button>
                    </div>
                    <!--<div class="login-tips">
                        <p class="tips-left" @click="switchFormTo('REGISTER_ACCOUNT')">注册新用户</p>
                        <p class="tips-right" @click="switchFormTo('FETCH_ACCOUNT')">找回我的密码</p>
                    </div>-->
                </el-form>

                <el-form v-if=" formChoice === 'REGISTER_ACCOUNT' " :model="registerAccountForm" :rules="rules"
                         ref="registerAccountForm" label-width="0px" class="demo-ruleForm">
                    <el-form-item prop="mail">
                        <el-input v-model="registerAccountForm.mail" placeholder="请输入您的邮箱地址"></el-input>
                    </el-form-item>
                    <div class="login-btn">
                        <el-button type="primary" @click="submitForm('registerAccountForm')">注册</el-button>
                    </div>
                    <div class="login-tips">
                        <p class="tips" @click="switchFormTo('LOGIN')">我已经注册完毕，返回登录</p>
                    </div>
                </el-form>

                <el-form v-if=" formChoice === 'FETCH_ACCOUNT' " :model="fetchAccountForm" :rules="rules"
                         ref="fetchAccountForm" label-width="0px" class="demo-ruleForm">
                    <el-form-item prop="mail">
                        <el-input v-model="fetchAccountForm.mail" placeholder="请输入您的邮箱地址"></el-input>
                    </el-form-item>
                    <div class="login-btn">
                        <el-button type="primary" @click="submitForm('fetchAccountForm')">找回密码</el-button>
                    </div>
                    <div class="login-tips">
                        <p class="tips" @click="switchFormTo('LOGIN')">我已经找回密码，返回登录</p>
                    </div>
                </el-form>

            </div>
        </div>
        <vue-particles
                id="particles-js"
                color="#dedede"
                :particleOpacity="0.1"
                :particlesNumber="80"
                shapeType="circle"
                :particleSize="4"
                linesColor="#dedede"
                :linesWidth="1"
                :lineLinked="true"
                :lineOpacity="0.05"
                :linesDistance="150"
                :moveSpeed="2"
                :hoverEffect="true"
                hoverMode="grab"
                :clickEffect="false"
                clickMode="push"
        >
        </vue-particles>


    </div>
</template>

<script>
    import {mapGetters, mapActions} from 'vuex'

    export default {
        data: function () {
            return {
                formChoice: "LOGIN", // 用户登录："LOGIN", 用户注册："REGISTER_ACCOUNT", 找回密码："FETCH_ACCOUNT"
                loginForm: {
                    username: '',
                    password: ''
                },
                registerAccountForm: {
                    mail: ''
                },
                fetchAccountForm: {
                    mail: ''
                },
                rules: {
                    username: [
                        {required: true, message: '请输入用户名', trigger: 'blur'}
                    ],
                    password: [
                        {required: true, message: '请输入密码', trigger: 'blur'}
                    ],
                    mail: [
                        {required: true, message: '请输入邮箱地址', trigger: 'blur'}
                    ]
                }
            }
        },
        computed: {
            ...mapGetters({
                isLogin: 'getLoginState',
                lastVisit: 'getLastVisit',
                promptMessage: 'getPromptMessage'
            })
        },
        methods: {
            ...mapActions(['login']),
            switchFormTo(formName){
                this.formChoice = formName;
            },
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        if (this.formChoice === "LOGIN") {
                            let data = {
                                "username": this.loginForm.username,
                                "userpwd": this.loginForm.password
                            };
                            this.$store.dispatch('login', data);
                        } else if (this.formChoice === "REGISTER_ACCOUNT") {
                            let data = {
                                "usermail": this.registerAccountForm.mail
                            };
                            this.$store.dispatch('registerAccount', data);
                        } else if (this.formChoice === "FETCH_ACCOUNT") {
                            let data = {
                                "usermail": this.fetchAccountForm.mail
                            };
                            this.$store.dispatch('fetchAccount', data);
                        }
                    } else {
                        return false;
                    }
                });
            },
            onInterval: function () {
                if (this.isLogin) {
                    clearInterval(this.internalTimer);
                    this.$message.success("登录成功");

                    // 跳转回用户最近一次访问的地址
                    let lastVisit = this.lastVisit;
                    if (lastVisit != null && lastVisit != "null") {
                        this.$store.dispatch('saveLastVisit', null);
                        this.$router.push(lastVisit);
                    } else {
                        this.$router.push('/');
                    }
                }
            }
        },
        created () {
            this.$store.dispatch('initLoginInfo');
            this.$store.dispatch('fetchLastVisit');
        },
        mounted: function () {
            this.internalTimer = setInterval(this.onInterval.bind(this), 500);
        },
        beforeDestroy: function () {
            clearInterval(this.internalTimer);
        },
        watch: {
            promptMessage: function (newMessage) {
                if (newMessage.code != null) {
                    if (newMessage.code >= 0) {
                        this.$message.success(newMessage.details);
                    } else {
                        this.$message.error(newMessage.details);
                    }
                }
            }
        }
    }
</script>

<style scoped>

    .login-wrapper {
        position: relative;
        height: 100%;
        width: 100%;
        background-color: #353535;
    }

    #particles-js {
        background-size: cover;
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
    }

    .login-form {
        position: relative;
        top: 250px;
        width: 500px;
        margin: 0 auto;
        text-align: center;
        z-index: 999;
    }

    .login-form-title {
        color: white;
        font-size: 30px;
        padding-bottom: 30px;
    }

    .login-form-input {
        position: absolute;
        width: 400px;
        padding: 50px;
        border-radius: 5px;
        /*background-color: #d7d7d7;*/
    }

    .login-btn {
    }

    .login-btn button {
        width: 100%;
        height: 36px;
    }

    .login-tips p {
        font-size: 13px;
        margin-bottom: 0px;
        color: grey;
        cursor: pointer;
    }

    .tips-left {
        float: left;
    }

    .tips-right {
        float: right;
    }

    .tips-left:hover, .tips-right:hover, .tips:hover {
        color: deepskyblue;
    }

</style>
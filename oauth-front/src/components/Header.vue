<template>
    <div class="header">
        <div class="title" @click="returnHome"><i class="el-icon-star-off logo"></i>{{title}}</div>
        <div class="user-info">
            <el-dropdown @command="handleCommand">
                <span class="el-dropdown-link">
                    <img v-if="username != null" class="user-logo" src="../assets/img/dog.jpg">
                    {{(username != null) ? username : "您好，请登录"}}
                </span>
                <el-dropdown-menu slot="dropdown" class="dropdown-menu">
                    <el-dropdown-item command="logout" v-if='isLogin'>退出</el-dropdown-item>
                    <el-dropdown-item command="login" v-else>登录</el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>
        </div>
    </div>
</template>

<script>
    import {mapGetters, mapActions} from 'vuex'

    export default {
        props: {
            title: {
                type: String,
                required: true,
                value: "标题"
            }
        },
        data: function () {
            return {
            }
        },
        computed: {
            ...mapGetters({
                isLogin: 'getLoginState',
                isExpired: 'getExpireState',
                username: 'getUserName'
            })
        },
        watch: {
            isExpired: function () {
                if (this.isLogin && this.isExpired) {
                    this.instance = this.$notify({
                        title: '',
                        message: '您的当前会话已过期，请重新登录。',
                        type: 'warning',
                        offset: 60
                    });
                }
            }
        },
        methods: {
            ...mapActions(['initLoginInfo', 'logout', 'checkExpired']),
            handleCommand(command) {
                if (command == 'logout') {
                    this.logout();
                } else if (command == 'login') {
                    this.login();
                }
            },
            returnHome() {
                this.$router.push('/');
            },
            onInterval: function() {
                // 检查登录状态是否过期
                this.$store.dispatch('checkExpired');
            },
            logout() {
                if (this.instance) {
                    this.instance.close();
                }
                this.$store.dispatch('logout');
                this.$store.dispatch('saveLastVisit', this.$route.fullPath);
                this.$router.push('/login');
                this.$message.success("登出成功");
            },
            login() {
                this.$store.dispatch('saveLastVisit', this.$route.fullPath);
                this.$router.push('/login');
            }
        },
        created () {
            this.$store.dispatch('initLoginInfo');
        },
        beforeMount: function () {
            //若发现没有登录，提示用户登录
            if (!this.isLogin) {
                this.$message.warning("您好，请先登录。");
            }
        },
        mounted: function () {
            this.internalTimer = setInterval(this.onInterval.bind(this), 500);
        },
        beforeDestroy: function () {
            clearInterval(this.internalTimer);
        }
    };
</script>

<style scoped>

    .user-info {
        float: right;
        padding-right: 50px;
        font-size: 16px;
        color: #fff;
    }

    .user-info .el-dropdown-link {
        position: relative;
        display: inline-block;
        padding-left: 50px;
        color: #fff;
        cursor: pointer;
        vertical-align: middle;
    }

    .user-info .user-logo {
        position: absolute;
        left: 0;
        top: 15px;
        width: 40px;
        height: 40px;
        border-radius: 50%;
    }

    .el-dropdown-menu__item {
        text-align: center;
    }

    .dropdown-menu {
        min-width: 100px;
    }

</style>
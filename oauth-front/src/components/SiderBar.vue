<template>
    <div class="sidebar">
        <el-menu :default-openeds="['admin', 'user', 'dev']" class="el-menu-vertical-demo" router>
            <el-submenu index="admin" v-if="userRoles!=null && userRoles.includes('admin')">
                <template slot="title"><i class="el-icon-menu"></i>管理员</template>
                <el-menu-item index="/admin/users"><i class="el-icon-caret-right"></i>用户列表</el-menu-item>
                <el-menu-item index="/admin/clients"><i class="el-icon-caret-right"></i>Client列表</el-menu-item>
                <!--<el-menu-item index="/admin/approvedsites"><i class="el-icon-caret-right"></i>验证通过的登录列表</el-menu-item>-->
                <el-menu-item index="/admin/tokens"><i class="el-icon-caret-right"></i>颁发的Token列表</el-menu-item>
                <el-menu-item index="/admin/auditLogs"><i class="el-icon-caret-right"></i>操作日志列表</el-menu-item>
            </el-submenu>
            <el-submenu index="user">
                <template slot="title"><i class="el-icon-menu"></i>用户</template>
                <!--<el-menu-item index="/user/approvedsites"><i class="el-icon-caret-right"></i>我验证通过的登录</el-menu-item>-->
                <el-menu-item index="/user/tokens"><i class="el-icon-caret-right"></i>我颁发的Token</el-menu-item>
            </el-submenu>
            <el-submenu index="dev">
                <template slot="title"><i class="el-icon-menu"></i>应用开发</template>
                <el-menu-item index="/dev/regclient"><i class="el-icon-caret-right"></i>注册Client</el-menu-item>
                <el-menu-item index="/dev/myclient"><i class="el-icon-caret-right"></i>我的Client</el-menu-item>
            </el-submenu>
            <el-submenu index="useraccount" v-if="userRoles!=null && userRoles.includes('local')">
                <template slot="title"><i class="el-icon-menu"></i>用户信息</template>
                <el-menu-item index="/useraccount/basic"><i class="el-icon-caret-right"></i>基本信息</el-menu-item>
                <el-menu-item index="/useraccount/changepwd"><i class="el-icon-caret-right"></i>更改密码</el-menu-item>
            </el-submenu>
        </el-menu>
    </div>
</template>
<script>
    import {mapGetters} from 'vuex'
    export default {
        computed: {
            ...mapGetters({
                userRoles: 'getUserRoles'
            })
        }
    }
</script>
<style scoped>

    .sidebar {
        display: block;
        position: absolute;
        width: 250px;
        left: 0;
        top: 70px;
        bottom: 0;
        background: #2E363F;
    }

    .sidebar > ul {
        height: 100%;
    }

    .sidebar li i {
        font-size: 16px;
        vertical-align: baseline;
    }

</style>

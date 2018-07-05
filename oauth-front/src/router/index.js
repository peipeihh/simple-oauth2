import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router);

import BlankPage from '../pages/Blank.vue'

import Layout from '../pages/Layout.vue'
import Login from '../pages/Login.vue'

import AdminPage from '../pages/admin/Index.vue'
import ClientList from '../pages/admin/ClientList.vue'
import TokenList from '../pages/admin/TokenList.vue'
import UserList from '../pages/admin/UserList.vue'
import ApprovedSiteList from '../pages/admin/ApprovedSiteList.vue'
import AuditLogList from '../pages/admin/AuditLogList.vue'

import UserPage from '../pages/user/Index.vue'
import ApprovedSite from '../pages/user/ApprovedSite.vue'
import MyToken from '../pages/user/MyToken.vue'

import DevPage from '../pages/dev/Index.vue'
import ClientRegister from '../pages/dev/Register.vue'
import MyClient from '../pages/dev/MyClient.vue'

import UserAccountPage from '../pages/useraccount/Index.vue'
import UserBasic from '../pages/useraccount/UserBasic.vue'
import UserChangePwd from '../pages/useraccount/ChangePwd.vue'

import LayoutAuth from '../pages/LayoutAuth.vue'
import AuthorizePage from '../pages/oauth2/Authorize.vue'

export default new Router({
    mode: 'hash', // mode option: 1. hash (default), 2. history
    routes: [{
        path: '',
        name: 'base',
        component: Layout,
        children: [{
            path: 'admin',
            name: 'admin',
            component: AdminPage,
            children: [{
                path: 'clients',
                name: 'clients',
                component: ClientList
            }, {
                path: 'users',
                name: 'users',
                component: UserList
            }, {
                path: 'approvedsites',
                name: 'approvedsites',
                component: ApprovedSiteList
            }, {
                path: 'tokens',
                name: 'tokens',
                component: TokenList
            }, {
                path: 'auditLogs',
                name: 'auditLogs',
                component: AuditLogList
            }]
        }, {
            path: 'user',
            name: 'user',
            component: UserPage,
            children: [{
                path: 'approvedsites',
                name: 'approvedsite',
                component: ApprovedSite
            }, {
                path: 'tokens',
                name: 'mytoken',
                component: MyToken
            }]
        }, {
            path: 'dev',
            name: 'dev',
            component: DevPage,
            children: [{
                path: 'regclient',
                name: 'regclient',
                component: ClientRegister
            }, {
                path: 'myclient',
                name: 'myclient',
                component: MyClient
            }]
        }, {
            path: 'useraccount',
            name: 'useraccount',
            component: UserAccountPage,
            children: [{
                path: 'basic',
                name: 'basic',
                component: UserBasic
            }, {
                path: 'changepwd',
                name: 'changepwd',
                component: UserChangePwd
            }]
        }]
    }, {
        path: '/404',
        name: '404',
        component: Layout
    }, {
        path: '/login',
        name: 'Login',
        component: Login,
    }, {
        path: '/authorize',
        component: LayoutAuth,
        children: [{
            path: '',
            name: 'Authorize',
            component: AuthorizePage
        }]
    }],
    linkActiveClass: 'active'
})

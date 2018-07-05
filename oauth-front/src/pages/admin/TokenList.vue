<template>
    <div class="content-panel">
        <div>
            <el-row class="nav-bar">
                <el-col>
                    <el-breadcrumb separator="/">
                        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
                        <el-breadcrumb-item>管理员</el-breadcrumb-item>
                        <el-breadcrumb-item>颁发的Token列表</el-breadcrumb-item>
                    </el-breadcrumb>
                </el-col>
            </el-row>

            <el-row style="margin-bottom: 7px">
                <el-select v-model="query.userId"
                           placeholder="输入用户名"
                           filterable
                           remote
                           :remote-method="handleUserAutoPrompt"
                           class="query-input">
                    <el-option v-for="item in users"
                               :key="item.id"
                               :label="item.name"
                               :value="item.id">
                    </el-option>
                </el-select>
                <el-select v-model="query.clientId"
                           placeholder="输入Client名"
                           filterable
                           remote
                           :remote-method="handleClientAutoPrompt"
                           class="query-input">
                    <el-option v-for="item in clients"
                               :key="item.id"
                               :label="item.clientId"
                               :value="item.clientId">
                    </el-option>
                </el-select>
                <el-button @click="resetQueryForm">重置</el-button>
                <el-button type="primary" @click="refreshTokenList">查询</el-button>
            </el-row>
        </div>

        <div>
            <el-tabs v-model="activeTab" @tab-click="handleTabClick">
                <el-tab-pane label="Access Token" name="AccessToken"></el-tab-pane>
                <el-tab-pane label="Refresh Token" name="RefreshToken"></el-tab-pane>
                <el-tab-pane label="Auth Code" name="AuthCode"></el-tab-pane>
            </el-tabs>
            <el-table :data="tokens" style="width: 100%" border fit>
                <el-table-column label="ID" prop="id" align="center" width="80" sortable></el-table-column>
                <el-table-column label="用户名" prop="userName" align="center"></el-table-column>
                <el-table-column label="Client名" prop="clientName" align="center"></el-table-column>
                <el-table-column v-if="this.activeTab == 'AuthCode'" label="Code" prop="code"
                                 align="center" width="600"></el-table-column>
                <el-table-column v-else label="Token" prop="value" align="center" width="600"></el-table-column>
                <el-table-column label="创建时间" prop="insertTime" align="center"
                                 :formatter="dateFormatter" sortable></el-table-column>
                <el-table-column label="失效时间" prop="expiration" align="center"
                                 :formatter="dateFormatter" sortable></el-table-column>
                <el-table-column label="操作" align="center" width="140">
                    <template scope="props">
                        <el-button @click="handleDelete(props.row)" type="danger" size="small">注销
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>

        <div align='center' style="margin-top: 10px">
            <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="currentPage"
                    :page-sizes="[10, 20, 30, 50]"
                    :page-size="pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total">
            </el-pagination>
        </div>
    </div>
</template>
<script>

    import {mapGetters, mapActions} from 'vuex';
    import dateUtil from '../../utils/dateUtil';

    export default {
        data: function () {
            return {
                query: {
                    userId: '',
                    clientId: ''
                },
                currentPage: 1,
                pageSize: 10,
                activeTab: "AccessToken"
            }
        },
        created: function () {
            this.refreshTokenList();
            this.$store.dispatch('fetchUsersByPage', {
                page: 0,
                size: 10,
                name: ''
            });
            this.$store.dispatch('fetchClientsByPage', {
                clientId: '',
                ownerId: '',
                page: 0,
                size: 10
            });
        },
        computed: {
            ...mapGetters({
                accessTokens: 'getAccessTokens',
                refreshTokens: 'getRefreshTokens',
                authCodes: 'getAuthCodes',
                total: 'getTokenCount',
                users: 'getUserList',
                clients: 'getClientList'
            }),
            tokens: function () {
                // 根据当前不同tab选择，返回相应的Tokens
                let tokens = [];
                if (this.activeTab == "AccessToken") {
                    tokens = this.accessTokens;
                } else if (this.activeTab == "RefreshToken") {
                    tokens = this.refreshTokens;
                } else if (this.activeTab == "AuthCode") {
                    tokens = this.authCodes;
                }
                return tokens;
            }
        },
        mounted: function () {

        },
        methods: {
            dateFormatter(row, column, cellValue) {
                return dateUtil.formatDate(cellValue);
            },
            handleTabClick(tab, event) {
                this.refreshTokenList();
            },
            handleDelete(data) {
                let queryParam = {
                    userId: this.query.userId,
                    clientId: this.query.clientId,
                    page: this.currentPage - 1,
                    size: this.pageSize
                };
                this.$confirm('确认注销？').then(() => {
                    if (this.activeTab == "AccessToken") {
                        this.$store.dispatch('revokeAccessToken', {tokenId: data.id, queryParam: queryParam});
                    } else if (this.activeTab == "RefreshToken") {
                        this.$store.dispatch('revokeRefreshToken', {tokenId: data.id, queryParam: queryParam});
                    } else if (this.activeTab == "AuthCode") {
                        this.$store.dispatch('revokeAuthCode', {codeId: data.id, queryParam: queryParam});
                    }
                }).catch(() => {
                });
            },
            handleSizeChange(data) {
                this.pageSize = data;
                this.refreshTokenList();
            },
            handleCurrentChange(data) {
                this.currentPage = data;
                this.refreshTokenList();
            },
            refreshTokenList() {
                let data = {
                    userId: this.query.userId,
                    clientId: this.query.clientId,
                    page: this.currentPage - 1,
                    size: this.pageSize
                };
                if (this.activeTab == "AccessToken") {
                    this.$store.dispatch('fetchAccessTokensByPage', data);
                } else if (this.activeTab == "RefreshToken") {
                    this.$store.dispatch('fetchRefreshTokensByPage', data);
                } else if (this.activeTab == "AuthCode") {
                    this.$store.dispatch('fetchAuthCodesByPage', data);
                }
            },
            handleUserAutoPrompt(data) {
                this.$store.dispatch('fetchUsersByPage', {
                    name: data,
                    page: 0,
                    size: 10
                })
            },
            handleClientAutoPrompt(data) {
                this.$store.dispatch('fetchClientsByPage', {
                    clientId: data,
                    ownerId: '',
                    page: 0,
                    size: 10
                })
            },
            resetQueryForm() {
                this.query.userId = '';
                this.query.clientId = '';
                this.currentPage = 1;
                this.refreshTokenList();
            },
        }
    }

</script>

<style>

</style>
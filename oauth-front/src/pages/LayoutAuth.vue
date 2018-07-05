<template>
    <div id="authorize" class="layout-wrapper">
        <vheader title="OAuth2登录授权"></vheader>
        <div class="main-content-auth">
            <div class="content-wrapper-auth">
                <transition name="move" mode="out-in">
                    <router-view></router-view>
                </transition>
            </div>
            <vfooter></vfooter>
        </div>
    </div>
</template>

<script>
    import vheader from '../components/Header.vue';
    import vfooter from '../components/Footer.vue';
    import {mapGetters} from 'vuex'

    export default {
        components: {
            vheader,
            vfooter
        },
        computed: {
            ...mapGetters({
                promptMessage: 'getPromptMessage'
            })
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
        },
        methods: {
            returnHome() {
                this.$router.push('/');
            }
        }
    }

</script>

<style>

    @import '../assets/common.css';

    .main-content-auth {
        background: none repeat scroll 0 0 #fff;
        position: absolute;
        left: 0px;
        right: 0;
        top: 70px;
        bottom: 0;
        width: auto;
        padding: 40px;
        box-sizing: border-box;
        overflow-y: scroll;
    }

    .content-wrapper-auth {
        min-height: 800px;
    }

</style>
import Vue from 'vue'
import Vuex from 'vuex'
import oauth from './model/oauth'
Vue.use(Vuex);

/**
 * detect current environment and set the debug configuration for vue store
 */
const debug_mode = process.env.NODE_ENV !== 'production';

/**
 * initialize the vuex store with actions/getters/modules
 */
export default new Vuex.Store({
    modules: {
        oauth
    },
    strict: debug_mode
})

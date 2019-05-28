import Vue from 'vue'
import App from './index'
import requests from '../../utils/request'
Vue.prototype.$httpWX = requests

const app = new Vue(App)
app.$mount()

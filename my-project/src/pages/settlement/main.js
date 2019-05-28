import Vue from 'vue'
import App from './settlement'
// import requests from '../../utils/request'
// Vue.prototype.$httpWX = requests

// add this to handle exception
Vue.config.errorHandler = function (err) {
  if (console && console.error) {
    console.error(err)
  }
}

const app = new Vue(App)
app.$mount()

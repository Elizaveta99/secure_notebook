import Vue from 'vue'
import App from './App.vue'

Vue.config.productionTip = false
Vue.prototype.$hostname = "http://localhost:8080"

new Vue({
  render: h => h(App),
}).$mount('#app')

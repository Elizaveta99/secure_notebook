import Vue from 'vue'
import App from './App.vue'

Vue.config.productionTip = false
Vue.prototype.$hostname =  "" //"http://localhost:8080" //for dev

new Vue({
  render: h => h(App),
}).$mount('#app')



<template>
    <div>
      <p>Generate and send to server open RSA key:</p>
      <button type="button" class="btn btn-primary" :disabled="isKeyGot"
              v-on:click="startSession">Start</button>
      <br>
      <p v-if="errorMessage">{{errorMessage}}</p>
      <Loader v-if="isLoad"/>
    </div>
</template>

<script>
import * as forge from '@/forge.min.0.6.12'
import Loader from '@/components/Loader'

export default {
  name: "ButtonStart",
  components: {
    Loader,
  },
  data() {
    return {
      isKeyGot: false,
      isLoad: false,
      errorMessage: '',
    }
  },
  methods: {
    startSession() {
      this.errorMessage = ''
      setTimeout(() => this.isLoad = true, 0) // activate <Loader />

      const keypair = forge.rsa.generateKeyPair({bits: 1024, e: 0x10001});
      const publicKey = {
        rsaE: keypair.publicKey.n.data.join(''),
        rsaN: keypair.publicKey.e.data.join('')
      }
      const privateKey = {
        e: keypair.publicKey.n.data.join(''),
        n: keypair.publicKey.e.data.join(''),
        d: keypair.privateKey.d.data.join('')
      }

      //console.log(publicKey, privateKey)

      fetch(this.$hostname + `/server/openSession?rsaE=${publicKey.rsaE}&rsaN=${publicKey.rsaN}`)
          .then(value => {
            console.log(value)
            fetch(this.$hostname + '/server/createSessionKey')
                .then(data => {
                  console.log(data);
                  return data.json()
                })
                .then(json => {
                  this.$emit('startSession', keypair.privateKey, json.content)
                  this.isKeyGot = true
                  this.isLoad = false
                  console.log('/createSessionKey: ' + json)
                })
                .catch(reason => this.errorMessage = reason)
          });




      //console.log("RSA keypair: ", keypair)

      //stub
      // setTimeout(() => this.isLoad = false, 0)
      // this.$emit('startSession', keypair.privateKey, '416CE8458A0C3DBE0683A52CB8F7E9FE')
    }
  }
}
</script>

<style scoped>

</style>
<template>
  <div>
    <p>Generate and send to server open RSA key:</p>
    <button type="button" class="btn btn-primary" :disabled="isKeyGot"
            v-on:click="startSession">Start
    </button>
    <br>
    <p v-if="errorMessage">{{ errorMessage }}</p>
    <Loader v-if="isLoad"/>
  </div>
</template>

<script>
import * as forge from '@/forge.min.0.6.12'
import Loader from '@/components/Loader'
import axios from 'axios'
import * as rsa from '@/rsa'

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

      //forge.rsa.generateKeyPair({bits: 1024, e: 0x10001});
      const keypair = rsa.generateRsaKeyPair()
      const publicKey = keypair.publicKey
      const privateKey = keypair.privateKey
      // const publicKey = {
      //   rsaE: keypair.publicKey.n.data.join(''),
      //   rsaN: keypair.publicKey.e.data.join('')
      // }
      // const privateKey = {
      //   e: keypair.publicKey.n.data.join(''),
      //   n: keypair.publicKey.e.data.join(''),
      //   d: keypair.privateKey.d.data.join('')
      // }
      // const publicKey = {
      //   rsaE: 5n,
      //   rsaN: 21n
      // }
      // const privateKey = {
      //   e: 5n,
      //   n: 21n,
      //   d: 17n
      // }
      // const AXIOS = axios.create({
      //   baseURL: `http://localhost:8080/server`,
      //   timeout: 1000
      // });

      // console.log(AXIOS.get(`openSession?rsaE=123&rsaN=12345`))

      //console.log(publicKey, privateKey)

///server/openSession?rsaE=${publicKey.e}&rsaN=${publicKey.n}

      // fetch(this.$hostname + `/server/openSession?rsaE=123&rsaN=21`)

      fetch(this.$hostname + `/server/openSession?rsaE=${publicKey.e}&rsaN=${publicKey.n}`, {'mode': 'no-cors'})
          .then(value => {
            console.log(value)
            fetch(this.$hostname + '/server/createSessionKey')
                .then(response => response.json())
                .then(json => {
                  const sessionEncryptedKey = json['content'][0]
                  let sessionDecryptedKey = ''

                  let data = [];
                  for (let i = 0; i < sessionEncryptedKey.length; i++){
                    data.push(sessionEncryptedKey.charCodeAt(i));
                  }
                  //console.log(data)
                  let StringKeyDelete = ''

                  for (let letter of data) {
                    const decryptedChar = rsa.decrypt(letter, privateKey.d, privateKey.n)
                    sessionDecryptedKey += decryptedChar
                    StringKeyDelete += String.fromCharCode(decryptedChar)
                  }
                  console.log('sessionEncryptedKey = ', StringKeyDelete)
                  this.$emit('startSession', keypair.privateKey, StringKeyDelete)
                  this.isKeyGot = true
                  this.isLoad = false
                  console.log('/createSessionKey: ' + json)
                })
                //.catch(reason => this.errorMessage = reason)
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
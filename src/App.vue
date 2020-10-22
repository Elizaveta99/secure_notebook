<template>
    <div id="app" class="container">
        <ButtonStart v-on:startSession="startSession"/>
        <FileQuery v-on:encodeText="encodeText" v-if="sessionKey"/>
        <FileView
            v-if="isEncoded"
            v-bind:filename="filename"
            v-bind:text="decodedText"
                  class="pb-4"
        />
        <br>
        <Loader v-if="isLoading"/>
    </div>
</template>

<script>
    import ButtonStart from "@/components/ButtonStart";
    import FileQuery from "@/components/FileQuery";
    import FileView from "@/components/FileView";
    import * as forge from '@/forge.min.0.6.12'
    import * as rsa from "@/rsa";

    export default {
        name: 'App',
        components: {
            FileView,
            FileQuery,
            ButtonStart,
        },
        data() {
            return {
                privateKey: '',
                sessionKey: '',
                filename: '',
                decodedText: '',
                isEncoded : false,
                isLoading: false,
            }
        },
        methods: {
            startSession(privateKey, sessionKey) {
                this.privateKey = privateKey
                this.sessionKey = sessionKey
            },
            encodeText(filename, encodedText, initializedVector = '0000000000000000') {
                setTimeout(this.isLoading = true, 0)
                console.log('encodedText= ', encodedText)
                this.filename = filename
                const decipher = forge.cipher.createDecipher('AES-CFB', forge.util.createBuffer(this.sessionKey));
                decipher.start({iv: forge.util.createBuffer(initializedVector)});
                decipher.update(forge.util.createBuffer(encodedText));
                decipher.finish();
                this.decodedText = decipher.output.data
                this.isEncoded = true
                console.log('decodedText= ', this.decodedText)
                console.log('decodedText= ', decipher.output.bytes())
                setTimeout(this.isLoading = false, 0)
            },
          reload() {
            setTimeout(this.isLoading = true, 0)
            const keypair = rsa.generateRsaKeyPair()
            const publicKey = keypair.publicKey
            const privateKey = keypair.privateKey
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
                        this.startSession(keypair.privateKey, StringKeyDelete)
                        this.isKeyGot = true
                        this.isLoad = false
                        console.log('/createSessionKey: ' + json)
                      })
                  //.catch(reason => this.errorMessage = reason)
                });

            setTimeout(this.isLoading = false, 0)
          }
        }

    }
</script>

<style>
    #app {
        font-family: Avenir, Helvetica, Arial, sans-serif;
        -webkit-font-smoothing: antialiased;
        -moz-osx-font-smoothing: grayscale;
        text-align: center;
        color: #2c3e50;
        margin-top: 60px;
    }
</style>

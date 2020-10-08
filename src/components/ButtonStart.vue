<template>
    <div>
        <p>Generate and send to server open RSA key:</p>
        <button type="button" class="btn btn-primary" :disabled="isKeyGot"
                v-on:click="startSession">Start</button>
        <br>
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
            }
        },
        methods: {
            startSession() {
                setTimeout(() => this.isLoad = true , 0) // activate <Loader />
                const keypair = forge.rsa.generateKeyPair({bits: 512, e: 0x10001});
                // fetch(this.$hostname + "/rsa-open?publicKey=" + keypair.publicKey)
                //     .then(data => data.json())
                //     .then(json => {
                //         this.$emit('startSession', keypair.privateKey, json.sessionKey)
                //         this.isKeyGot = true
                //         this.isLoad = false
                //     })
                //

                //stub
                setTimeout(() => this.isLoad = false, 0)
                this.$emit('startSession', keypair.privateKey, '416CE8458A0C3DBE0683A52CB8F7E9FE')
            }
        }
    }
</script>

<style scoped>

</style>
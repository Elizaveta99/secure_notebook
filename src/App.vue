<template>
    <div id="app" class="container">
        <ButtonStart v-on:startSession="startSession"/>
        <FileQuery v-on:encodeText="encodeText" v-if="sessionKey"/>
        <FileView v-bind:filename="filename"
                  v-bind:text="decodedText"
                  v-if="isEncoded"
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
            encodeText(filename, initializedVector, encodedText) {
                setTimeout(this.isLoading = true, 0)
                this.filename = filename
                const decipher = forge.cipher.createDecipher('AES-CFB', this.sessionKey);
                decipher.start({iv: initializedVector});
                decipher.update(forge.util.createBuffer(encodedText));
                decipher.finish();
                this.decodedText = decipher.output.data
                this.isEncoded = true
                setTimeout(this.isLoading = false, 0)
            },
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

<template>
    <div>
        <form method="get" @submit="onSubmit">
            <div class="form-group">
                <label for="fileQuery">Enter name of the file to load from server:</label>
                <input class="form-control" type="text" placeholder="filename.txt" id="fileQuery" v-model="filename">
            </div>
            <button type="submit" class="btn btn-primary">Get file</button>
        </form>
    </div>
</template>

<script>
    export default {
        name: "FileQuery",
        data() {
            return {
                filename: "",
            }
        },
        methods: {
            onSubmit(event) {
                event.preventDefault()
                console.log("onSubmit", this.filename)
                this.filename = this.filename.trim()
                if (this.filename) {
                    // fetch(this.$hostname + "/file?name=" + this.filename)
                    //     .then(data => data.json())
                    //     .then(json => this.$emit("encodeText", filename, json.initializedVector, json.encodeText))
                    const initializedVector = ''
                    this.$emit("encodeText", this.filename, initializedVector, "CLMNzDdoV+7k0ndBq+ILlzHMBRy3AJ4TlhVzA1R3Rf17qNuoQ7atKCtzkhp6gNKs3VhoFyDgGA+gdwtqmhZXm8SiaKDM46QaOrOjp8/UC85O9snCZ+zpSbeM/OqqpSgh+q3AMe5gMLdth9iGUJra49cGTeK6FIzT4zLtWussySGO60+7i9yjr5UDs5bdGFyAwZSwDCC/89U6dOhVakyaEEoQZ+c9Z76Hu7kzoPCp+eEQ1PijWNB4+i8klpoHldi11qSjkIAg6FucXcxnR7lz6j0OHu5acFilxO6bNAQ=")
                }
            }
        }
    }
</script>

<style scoped>

</style>
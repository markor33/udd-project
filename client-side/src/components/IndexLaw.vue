<script setup lang="ts">
import {ref} from "vue";
import {useLawStore} from "@/store/law";
import {storeToRefs} from "pinia";

const lawStore = useLawStore()

const { isLoading, hasError } = storeToRefs(lawStore)

const message = ref({
  show: false,
  text: ''
})
const showMessage = (text: string) => {
  message.value.show = true
  message.value.text = text
}

const selectedFile = ref<File | null>(null);

const handleFileChange = (event) => {
  selectedFile.value = event.target.files[0];
};

const index = async () => {
  if (!selectedFile.value)
    return
  await lawStore.index(selectedFile.value)
  showMessage('Law indexed successfully')
  selectedFile.value = null
}

</script>

<template>
  <div class="container mt-5">
    <h2 class="mb-5">Law indexing</h2>
    <div class="file-upload-container">
      <v-file-input label="Contract File input" hide-details @change="handleFileChange($event)"></v-file-input>
      <v-btn class="ml-5" @click="index" color="primary" :disabled="isLoading || !selectedFile">Parse</v-btn>
      <v-progress-circular v-if="isLoading" class="ml-2" indeterminate></v-progress-circular>
    </div>
  </div>
  <v-snackbar v-model="message.show" color="success">
    {{ message.text }}
  </v-snackbar>
  <v-snackbar v-model="hasError" color="red">
    Error occurred, try again
  </v-snackbar>
</template>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  width: 70%;
}

.file-upload-container {
  display: flex;
  align-content: center;
  align-items: center;
}
</style>

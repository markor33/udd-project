<script setup lang="ts">
import { ref } from 'vue';
import {useContractStore} from "@/store/contract";
import {storeToRefs} from "pinia";
import {ParsedContract} from "@/models/contract";
import {is} from "@babel/types";

const contractStore = useContractStore()

const selectedFile = ref<File | null>(null);

const { parsed, isParsed, isLoading, hasError } = storeToRefs(contractStore)

const message = ref({
  show: false,
  text: ''
})
const showMessage = (text: string) => {
  message.value.show = true
  message.value.text = text
}

const handleFileChange = (event) => {
  selectedFile.value = event.target.files[0];
};

const parse = async () => {
  if (!selectedFile.value)
    return
  await contractStore.getParsed(selectedFile.value)
  showMessage('Contract parsed successfully')
}

const reset = () => {
  contractStore.reset()
  selectedFile.value = null
}

const submit = async () => {
  if (!selectedFile.value)
    return
  await contractStore.index(selectedFile.value)
  showMessage('Contract indexed successfully')
  reset()
}

</script>

<template>
  <div class="container mt-5">
    <h2 class="mb-5">Contract indexing</h2>
    <div v-if="!isParsed" class="file-upload-container">
      <v-file-input label="Contract File input" hide-details @change="handleFileChange($event)"></v-file-input>
      <v-btn class="ml-5" @click="parse" color="primary" :disabled="isLoading || !selectedFile">Parse</v-btn>
      <v-progress-circular v-if="isLoading" class="ml-2" indeterminate></v-progress-circular>
    </div>
    <div v-if="isParsed" class="parsed-contract-data">
      <v-text-field v-model="parsed.governmentName" label="Government name"></v-text-field>
      <v-text-field v-model="parsed.levelOfAdministration" label="Level of administration"></v-text-field>
      <div class="d-flex">
        <v-text-field v-model="parsed.street" style="flex: 45%" label="Street"></v-text-field>
        <v-text-field v-model="parsed.city" class="ml-2" style="flex: 45%" label="City"></v-text-field>
        <v-text-field v-model="parsed.number" class="ml-2" style="flex: 10%" label="Number"></v-text-field>
      </div>
      <div class="d-flex">
        <v-text-field v-model="parsed.firstName" label="First name"></v-text-field>
        <v-text-field v-model="parsed.lastName" class="ml-2" label="Last name"></v-text-field>
      </div>
      <div class="d-flex justify-end">
        <v-progress-circular v-if="isLoading" indeterminate></v-progress-circular>
        <v-btn class="ml-3" color="primary" :disabled="isLoading" @click="submit">Submit</v-btn>
        <v-btn class="ml-3" :disabled="isLoading" @click="reset">Reset</v-btn>
      </div>
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

.parsed-contract-data {
  display: flex;
  flex-direction: column;
}
</style>

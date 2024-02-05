<script setup lang="ts">
import { useContractStore } from "@/store/contract";
import { storeToRefs } from "pinia";
import { ContractSearchRecord } from "@/models/contract";
const contractStore = useContractStore()
const { selectedContract } = storeToRefs(contractStore)

const hasHighlights = (contract: ContractSearchRecord) => {
  if (!contract.highlightFields)
    return false
  else return Object.keys(contract.highlightFields).length !== 0;
}

</script>

<template>
  <v-card class="pa-2">
    <h2 class="mb-5">Contract record overview</h2>
    <span><b>Title:</b> {{ selectedContract.content.title }}</span>
    <span class="mt-2"><b>Signed by:</b> {{ selectedContract.content.firstName + ' ' + selectedContract.content.lastName }}</span>
    <span class="mt-2"><b>Government name:</b> {{ selectedContract.content.governmentName }}</span>
    <span class="mt-2"><b>Level of administration:</b> {{ selectedContract.content.levelOfAdministration }}</span>
    <span class="mt-5"><b>Contract document content:</b></span>
    <div class="content-container">
      {{ selectedContract.content.content }}
    </div>
    <div v-if="hasHighlights(selectedContract)" class="mt-5">
      <span><b>Highlighted by:</b></span>
      <div v-for="highlight in selectedContract.highlightFields.content">
        <span v-html="highlight"></span>
      </div>
    </div>
  </v-card>
</template>

<style scoped>
.content-container {
  max-height: 500px;
  overflow-y: auto;
  border: 1px solid #ccc; /* Optional: add border for styling */
  border-radius: 5px;
  padding: 10px;
}
</style>

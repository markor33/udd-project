<script setup lang="ts">
import {useLawStore} from "@/store/law";
import {storeToRefs} from "pinia";

const lawStore = useLawStore()

const { selectedLaw } = storeToRefs(lawStore)

</script>

<template>
  <v-card class="pa-2">
    <h2 class="mb-5">Law record overview</h2>
    <span><b>Title:</b> {{ selectedLaw.content.title }}</span>
    <span class="mt-5"><b>Law document content:</b></span>
    <div class="content-container">
      {{ selectedLaw.content.content }}
    </div>
    <div v-if="selectedLaw.highlightFields" class="mt-5">
      <span><b>Highlighted by:</b></span>
      <div v-for="highlight in selectedLaw.highlightFields.content">
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

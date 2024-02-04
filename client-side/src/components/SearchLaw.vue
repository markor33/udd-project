<script setup lang="ts">

import {useLawStore} from "@/store/law";
import {storeToRefs} from "pinia";
import {ref} from "vue";
import {LawSearchRecord} from "@/models/law";

const lawStore = useLawStore()

const { laws, itemsPerPage, page, totalNumOfRecords, isLoading, query } = storeToRefs(lawStore)

const headers = [
  {
    title: 'Id',
    sortable: false,
    key: 'id',
    value: (law) => law.content.id
  },
  {
    title: 'Title',
    sortable: false,
    key: 'title',
    value: (law) => law.content.title
  },
  {
    title: '',
    key: 'actions'
  }
]

const isOverviewDialogActive = ref(false)
const selectedLaw = ref<LawSearchRecord>({} as LawSearchRecord)

const showOverview = (law: LawSearchRecord) => {
  selectedLaw.value = law
  isOverviewDialogActive.value = true
}

const downloadFile = async (law: LawSearchRecord) => {
  const response = await lawStore.downloadFile(law.content.serverFilename)
  const blob = new Blob([response.data], { type: "application/pdf" });
  const fileName = `${law.content.title}.pdf`

  const downloadLink = document.createElement('a')
  downloadLink.href = URL.createObjectURL(blob)
  downloadLink.download = fileName
  document.body.appendChild(downloadLink)
  downloadLink.click()
  document.body.removeChild(downloadLink)
}

</script>

<template>
  <div class="container mt-5">
    <h2 class="mb-5">Law search</h2>
    <div class="search-query-container d-flex">
      <v-text-field v-model="query" label="Query" hide-details color="primary"></v-text-field>
      <v-btn icon="mdi-reload" class="ml-5" @click="lawStore.reset"></v-btn>
      <v-btn icon="mdi-magnify" color="primary" class="ml-5" @click="lawStore.search"></v-btn>
    </div>
    <v-data-table-server
      v-model:items-per-page="itemsPerPage"
      v-model:page="page"
      :items-length="totalNumOfRecords"
      :items="laws"
      :headers="headers"
      :loading="isLoading"
      @update:options="lawStore.search"
    >
      <template #item.actions="{ item }">
        <div class="text-right">
          <v-icon class="mr-2" @click="showOverview(item)">mdi-eye</v-icon>
          <v-icon @click="downloadFile(item)">mdi-download</v-icon>
        </div>
      </template>
    </v-data-table-server>
  </div>
  <v-dialog v-model="isOverviewDialogActive" width="1200">
    <template #default="{ isActive }">
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
  </v-dialog>
</template>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  width: 70%;
}

.content-container {
  max-height: 500px;
  overflow-y: auto;
  border: 1px solid #ccc; /* Optional: add border for styling */
  border-radius: 5px;
  padding: 10px;
}
</style>

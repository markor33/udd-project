<script setup lang="ts">

import {useLawStore} from "@/store/law";
import {storeToRefs} from "pinia";
import {LawSearchRecord} from "@/models/law";
import LawOverview from "@/components/LawOverview.vue";

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
          <v-dialog width="1200">
            <template v-slot:activator="{ props }">
              <v-icon v-bind="props" icon="mdi-eye" @click="lawStore.setSelectedLaw(item)"> </v-icon>
            </template>
            <template v-slot:default="{ isActive }">
              <LawOverview></LawOverview>
            </template>
          </v-dialog>
          <v-icon class="ml-1" @click="downloadFile(item)">mdi-download</v-icon>
        </div>
      </template>
    </v-data-table-server>
  </div>
</template>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  width: 70%;
}
</style>

<script setup lang="ts">
import {useGlobalDocsStore} from "@/store/globalDocs";
import {storeToRefs} from "pinia";
import {LawSearchRecord} from "@/models/law";
import {GlobalDocSearchRecord} from "@/models/globalDoc";
import ContractOverview from "@/components/ContractOverview.vue";
import LawOverview from "@/components/LawOverview.vue";

const globalDocsStore = useGlobalDocsStore()

const { docs, itemsPerPage, page, totalNumOfRecords, isLoading, query } = storeToRefs(globalDocsStore)

const headers = [
  {
    title: 'Id',
    sortable: false,
    key: 'id',
  },
  {
    title: 'Title',
    sortable: false,
    key: 'title',
  },
  {
    title: 'Doc type',
    sortable: false,
    key: 'index',
  },
  {
    title: '',
    key: 'actions'
  }
]

const downloadFile = async (doc: GlobalDocSearchRecord) => {
  console.log(doc)
  const response = await globalDocsStore.downloadFile(doc.serverFilename)
  const blob = new Blob([response.data], { type: "application/pdf" });
  const fileName = `${doc.title}.pdf`

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
      <v-btn icon="mdi-reload" class="ml-5" @click="globalDocsStore.reset"></v-btn>
      <v-btn icon="mdi-magnify" color="primary" class="ml-5" @click="globalDocsStore.search"></v-btn>
    </div>
    <v-data-table-server
      v-model:items-per-page="itemsPerPage"
      v-model:page="page"
      :items-length="totalNumOfRecords"
      :items="docs"
      :headers="headers"
      :loading="isLoading"
      @update:options="globalDocsStore.search"
    >
      <template #item.actions="{ item }">
        <div class="text-right">
          <v-dialog width="1200">
            <template v-slot:activator="{ props }">
              <v-icon v-bind="props" icon="mdi-eye" @click="globalDocsStore.setSelectedDoc(item)"> </v-icon>
            </template>
            <template v-slot:default="{ isActive }">
              <LawOverview v-if="item.index === 'law'"></LawOverview>
              <ContractOverview v-if="item.index === 'contract'"></ContractOverview>
            </template>
          </v-dialog>
          <!--<v-icon class="mr-2">mdi-eye</v-icon>-->
          <v-icon @click="downloadFile(item)">mdi-download</v-icon>
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

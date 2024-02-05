<script setup lang="ts">

import {ContractSearchRecord} from "@/models/contract";
import {useContractStore} from "@/store/contract";
import {storeToRefs} from "pinia";
import {ref} from "vue";
import {LawSearchRecord} from "@/models/law";
import ContractOverview from "@/components/ContractOverview.vue";

const queryExample = 'example: firstName:marko AND lastName:"Rapic"'

const contractStore = useContractStore()
const { contracts, itemsPerPage, page, totalNumOfRecords, isLoading, query } = storeToRefs(contractStore)

const headers = [
  {
    title: 'Id',
    sortable: false,
    key: 'id',
    value: (contract) => contract.content.id
  },
  {
    title: 'Title',
    sortable: false,
    key: 'title',
    value: (contract) => contract.content.title
  },
  {
    title: 'Government name',
    sortable: false,
    key: 'governmentName',
    value: (contract) => contract.content.governmentName
  },
  {
    title: 'Level of administration',
    sortable: false,
    key: 'levelOfAdministration',
    value: (contract) => contract.content.levelOfAdministration
  },
  {
    title: '',
    key: 'actions'
  }
]

const downloadFile = async (contract: ContractSearchRecord) => {
  const response = await contractStore.downloadFile(contract.content.serverFilename)
  const blob = new Blob([response.data], { type: "application/pdf" });
  const fileName = `${contract.content.title}.pdf`

  const downloadLink = document.createElement('a')
  downloadLink.href = URL.createObjectURL(blob)
  downloadLink.download = fileName
  document.body.appendChild(downloadLink)
  downloadLink.click()
  document.body.removeChild(downloadLink)
}

const fields = [
  {
    title: 'First name',
    key: 'firstName'
  },
  {
    title: 'Last name',
    key: 'lastName'
  },
  {
    title: 'Content',
    key: 'content'
  },
  {
    title: 'Government name',
    key: 'governmentName'
  },
  {
    title: 'Level of administration',
    key: 'levelOfAdministration'
  }
]

const field = ref('')
const value = ref('')
const operand = ref('')

const clearQuery = () => {
  field.value = ''
  value.value = ''
  operand.value = ''
  contractStore.clearExpression()
}

const location = ref('')
const distance = ref(1)

</script>

<template>
  <div class="container mt-5">
    <h2 class="mb-5">Contract search</h2>
    <div class="d-flex align-center">
      <v-select style="flex: 30%" v-model="field" label="Field" :items="fields" item-value="key" color="primary" hide-details></v-select>
      <v-text-field style="flex: 30%" v-model="value" class="ml-2" label="Value" color="primary" hide-details></v-text-field>
      <v-select style="flex: 10%" v-model="operand" class="ml-2" label="Operand" :items="['', 'NOT', 'AND', 'OR']" color="primary" hide-details></v-select>
      <v-btn class="ml-2" @click="contractStore.addCriteria(field, value, operand)" icon="mdi-plus" color="primary" size="small"></v-btn>
      <v-btn class="ml-2" @click="clearQuery()" size="small" icon="mdi-close"></v-btn>
    </div>
    <div class="d-flex align-center mt-2">
      <v-text-field v-model="location" label="Location" hide-details color="primary"></v-text-field>
      <v-text-field v-model="distance" label="Distance" hide-details class="ml-2" color="primary"></v-text-field>
      <v-btn class="ml-2" @click="contractStore.addLocationCriteria(location, distance)" color="primary" icon="mdi-plus" size="small"></v-btn>
    </div>
    <div class="search-query-container d-flex mt-5">
      <v-text-field v-model="query" label="Query" hide-details color="primary" disabled></v-text-field>
      <v-btn icon="mdi-reload" class="ml-5" @click="contractStore.resetSearch()"></v-btn>
      <v-btn icon="mdi-magnify" color="primary" class="ml-5" @click="contractStore.search()"></v-btn>
    </div>
    <v-data-table-server
      v-model:items-per-page="itemsPerPage"
      v-model:page="page"
      :items-length="totalNumOfRecords"
      :items="contracts"
      :headers="headers"
      :loading="isLoading"
      @update:options="contractStore.search"
    >
      <template #item.actions="{ item }">
        <div class="text-right">
          <v-dialog width="1200">
            <template v-slot:activator="{ props }">
              <v-icon v-bind="props" icon="mdi-eye" @click="contractStore.setSelectedContract(item)"> </v-icon>
            </template>
            <template v-slot:default="{ isActive }">
              <ContractOverview></ContractOverview>
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

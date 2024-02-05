import { defineStore } from 'pinia'
import axios from "axios";
import {ContractSearchRecord, ContractSearchResponse, ParsedContract} from "@/models/contract";
import {LawSearchResponse} from "@/models/law";

export const useContractStore = defineStore('contract', {
  state: () => ({
    parsed: {} as ParsedContract,
    contracts: [] as ContractSearchRecord[],
    selectedContract: {} as ContractSearchRecord,
    page: 1,
    itemsPerPage: 10,
    totalNumOfRecords: 0,
    expression: [] as string[],
    isLoading: false,
    hasError: false
  }),
  getters: {
    isParsed: (state) => Object.keys(state.parsed).length !== 0,
    query: (state) => state.expression.join(' ')
  },
  actions: {
    setSelectedContract(contract: ContractSearchRecord) {
      this.selectedContract = contract
    },
    async getParsed(file: File) {
      const formData = new FormData()
      formData.append('file', file)

      try {
        this.isLoading = true
        const response = await axios.post<ParsedContract>('/api/contract/parsed', formData)
        this.parsed = response.data
        this.hasError = false
        this.isLoading = false
      }
      catch (err) {
        console.error(err)
        this.hasError = true
        this.isLoading = false
      }
    },
    reset() {
      this.parsed = {} as ParsedContract
    },
    async index(file: File) {
      const formData = new FormData()
      formData.append('file', file)
      Object.entries(this.parsed).forEach(([key, value]) => formData.append(key, value))

      try {
        this.isLoading = true
        await axios.post('api/contract', formData)
        this.hasError = false
        this.isLoading = false
      }
      catch (err) {
        console.error(err)
        this.hasError = true
        this.isLoading = false
      }
    },
    async search() {
      try {
        this.isLoading = true

        const response = await axios.post<ContractSearchResponse>(
          'api/contract/advanced-search?' +
          new URLSearchParams({
            page: (this.page - 1).toString(),
            size: this.itemsPerPage.toString()
          }),
          {
            expression: this.expression
          })
        this.contracts = response.data.content
        this.totalNumOfRecords = response.data.totalElements

        this.isLoading = false
      }
      catch (err) {
        console.error(err)
        this.hasError = true
        this.isLoading = false
      }
    },
    resetSearch() {
      this.expression = [] as string[]
      this.page = 1
      this.itemsPerPage = 10
      this.search()
    },
    addCriteria(field: string, value: string, operator: string) {
      if (operator)
        this.expression.push(operator)
      this.expression.push(`${field}:${value}`)
    },
    addLocationCriteria(location: string, distance: number) {
      if (this.expression.length !== 0)
        this.expression.push('AND')
      this.expression.push(`location:${location}:${distance}`)
    },
    clearExpression() {
      this.expression = [] as string[]
    },
    async downloadFile(serverFileName: string) {
      return await axios.get<ArrayBuffer>(`api/contract/file/${serverFileName}`, { responseType: 'arraybuffer' })
    }
  }
})

import { defineStore } from 'pinia'
import axios from "axios";
import { ParsedContract } from "@/models/contract";

export const useContractStore = defineStore('contract', {
  state: () => ({
    parsed: {} as ParsedContract,
    isLoading: false,
    hasError: false
  }),
  getters: {
    isParsed: (state) => Object.keys(state.parsed).length !== 0,
  },
  actions: {
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
    }
  }
})

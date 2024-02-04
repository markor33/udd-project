import { defineStore } from 'pinia'
import axios from "axios";

export const useLawStore = defineStore('law', {
  state: () => ({
    isLoading: false,
    hasError: false
  }),
  getters: {

  },
  actions: {
    async index(file: File) {
      const formData = new FormData()
      formData.append('file', file)

      try {
        this.isLoading = true
        await axios.post('api/law', formData)
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

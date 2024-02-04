import { defineStore } from 'pinia'
import axios from "axios";
import {LawSearchRecord, LawSearchResponse} from "@/models/law";

export const useLawStore = defineStore('law', {
  state: () => ({
    laws: [] as LawSearchRecord[],
    page: 1,
    itemsPerPage: 10,
    totalNumOfRecords: 0,
    query: '',
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
    },
    async search() {
      try {
        this.isLoading = true

        const data = (this.query)? { content: this.query } : {}
        const response = await axios.post<LawSearchResponse>(
          'api/law/simple-search?' +
          new URLSearchParams({
            page: (this.page - 1).toString(),
            size: this.itemsPerPage.toString()
          }),
          data)
        this.laws = response.data.content
        this.totalNumOfRecords = response.data.totalElements

        this.isLoading = false
      }
      catch (err) {
        console.error(err)
        this.hasError = true
        this.isLoading = false
      }
    },
    reset() {
      this.query = ''
      this.page = 1
      this.itemsPerPage = 10
      this.search()
    },
    async downloadFile(serverFileName: string) {
      return await axios.get<ArrayBuffer>(`api/law/file/${serverFileName}`, { responseType: 'arraybuffer' })
    }
  }
})

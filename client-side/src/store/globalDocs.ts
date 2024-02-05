import { defineStore } from 'pinia'
import axios from "axios";
import {GlobalDocSearchRecord, GlobalDocSearchResponse} from "@/models/globalDoc";
import {useLawStore} from "@/store/law";
import {useContractStore} from "@/store/contract";
import {LawSearchRecord} from "@/models/law";

export const useGlobalDocsStore = defineStore('globalDocs', {
  state: () => ({
    docs: [] as GlobalDocSearchRecord[],
    page: 1,
    itemsPerPage: 25,
    totalNumOfRecords: 0,
    query: '',
    isLoading: false,
    hasError: false
  }),
  getters: {

  },
  actions: {
    setSelectedDoc(doc: GlobalDocSearchRecord) {
      const lawStore = useLawStore()
      const contractStore = useContractStore()
      const docAny = doc as any
      docAny.content = { ...doc }
      if (doc.index === 'law')
        lawStore.setSelectedLaw(docAny)
      else
        contractStore.setSelectedContract(docAny)
    },
    async search() {
      try {
        this.isLoading = true

        const data = { query: this.query }
        const response = await axios.post<GlobalDocSearchResponse>(
          'api/global/search?' +
          new URLSearchParams({
            page: (this.page - 1).toString(),
            size: this.itemsPerPage.toString()
          }),
          data)
        this.docs = response.data.content
        console.log(this.docs)
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

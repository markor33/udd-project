export interface LawSearchRecord {
  content: {
    id: string,
    title: string,
    content: string
    serverFilename: string
  },
  highlightFields: {
    content: string[]
  }
}

export interface LawSearchResponse {
  content: LawSearchRecord[],
  totalPages: number,
  totalElements: number
}

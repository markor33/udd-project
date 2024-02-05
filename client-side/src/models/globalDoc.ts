export interface GlobalDocSearchRecord {
  id: string,
  index: string,
  title: string,
  firstName: string,
  lastName: string,
  governmentName: string,
  levelOfAdministration: string,
  content: string,
  serverFilename: string,
  highlightFields: {
    content: string[]
  }
}

export interface GlobalDocSearchResponse {
  content: GlobalDocSearchRecord[],
  totalPages: number,
  totalElements: number
}

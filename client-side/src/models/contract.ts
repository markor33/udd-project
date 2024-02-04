export interface ParsedContract {
  firstName: string,
  lastName: string,
  governmentName: string,
  levelOfAdministration: string,
  street: string,
  number: string,
  city: string
}

export interface ContractSearchRecord {
  content: {
    id: string,
    title: string,
    firstName: string,
    lastName: string,
    governmentName: string,
    levelOfAdministration: string,
    content: string,
    serverFilename: string
  },
  highlightFields: {
    content: string[]
  }
}

export interface ContractSearchResponse {
  content: ContractSearchRecord[],
  totalPages: number,
  totalElements: number
}

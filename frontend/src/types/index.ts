// Tipos globais do SCF
// Adicione as interfaces aqui conforme as funcionalidades forem sendo criadas

export interface ApiResponse<T> {
  data: T
  message?: string
}

export interface PageResponse<T> {
  content: T[]
  totalElements: number
  totalPages: number
  size: number
  number: number
}

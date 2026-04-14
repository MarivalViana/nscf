// Tipos globais do SCF

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

// Auth
export interface LoginRequest {
  email: string
  senha: string
}

export interface LoginResponse {
  token: string
  nome: string
  email: string
}

export interface Usuario {
  nome: string
  email: string
}

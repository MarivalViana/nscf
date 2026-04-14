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

// Receitas
export type TipoReceita = 'RECORRENTE' | 'PONTUAL'

export interface ReceitaValorResponse {
  id: number
  valor: number
  mesInicio: number
  anoInicio: number
  mesFim: number | null
  anoFim: number | null
  vigente: boolean
}

export interface ReceitaResponse {
  id: number
  descricao: string
  tipo: TipoReceita
  ativo: boolean
  valores: ReceitaValorResponse[]
}

export interface ReceitaRequest {
  descricao: string
  tipo: TipoReceita
}

export interface ReceitaValorRequest {
  valor: number
  mesInicio: number
  anoInicio: number
  mesFim?: number | null
  anoFim?: number | null
}

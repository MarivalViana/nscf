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

// Categorias
export interface CategoriaResponse {
  id: number
  nome: string
  icone: string
}

export interface CategoriaRequest {
  nome: string
  icone: string
}

// Contas
export interface ContaResponse {
  id: number
  nome: string
  icone: string
}

export interface ContaRequest {
  nome: string
  icone: string
}

// Despesas
export type TipoDespesa = 'AVISTA' | 'PARCELADA' | 'RECORRENTE' | 'ANUAL'

export interface DespesaResponse {
  id: number
  descricao: string
  tipo: TipoDespesa
  valor: number
  qtdParcelas: number | null
  mesInicio: number
  anoInicio: number
  ativo: boolean
  valorParcela: number
  categoria: CategoriaResponse | null
  conta: ContaResponse | null
}

export interface DespesaRequest {
  descricao: string
  tipo: TipoDespesa
  valor: number
  qtdParcelas?: number | null
  mesInicio: number
  anoInicio: number
  categoriaId?: number | null
  contaId?: number | null
}

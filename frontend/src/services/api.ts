import axios from 'axios'
import type {
  LoginRequest, LoginResponse,
  ReceitaRequest, ReceitaResponse,
  ReceitaValorRequest, ReceitaValorResponse,
  DespesaRequest, DespesaResponse,
  CategoriaRequest, CategoriaResponse,
  ContaRequest, ContaResponse
} from '@/types'

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api/v1',
  headers: { 'Content-Type': 'application/json' }
})

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('scf_token')
  if (token) config.headers.Authorization = `Bearer ${token}`
  return config
})

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('scf_token')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

// Auth
export const authService = {
  login: (data: LoginRequest) =>
    api.post<LoginResponse>('/auth/login', data).then((r) => r.data)
}

// Receitas
export const receitaService = {
  listar: () =>
    api.get<ReceitaResponse[]>('/receitas').then((r) => r.data),

  buscarPorId: (id: number) =>
    api.get<ReceitaResponse>(`/receitas/${id}`).then((r) => r.data),

  criar: (data: ReceitaRequest) =>
    api.post<ReceitaResponse>('/receitas', data).then((r) => r.data),

  atualizar: (id: number, data: ReceitaRequest) =>
    api.put<ReceitaResponse>(`/receitas/${id}`, data).then((r) => r.data),

  desativar: (id: number) =>
    api.delete(`/receitas/${id}`),

  adicionarValor: (receitaId: number, data: ReceitaValorRequest) =>
    api.post<ReceitaValorResponse>(`/receitas/${receitaId}/valores`, data).then((r) => r.data),

  atualizarValor: (valorId: number, data: ReceitaValorRequest) =>
    api.put<ReceitaValorResponse>(`/receitas/valores/${valorId}`, data).then((r) => r.data),

  removerValor: (valorId: number) =>
    api.delete(`/receitas/valores/${valorId}`)
}

// Despesas
export const despesaService = {
  listar: () =>
    api.get<DespesaResponse[]>('/despesas').then((r) => r.data),

  buscarPorId: (id: number) =>
    api.get<DespesaResponse>(`/despesas/${id}`).then((r) => r.data),

  criar: (data: DespesaRequest) =>
    api.post<DespesaResponse>('/despesas', data).then((r) => r.data),

  atualizar: (id: number, data: DespesaRequest) =>
    api.put<DespesaResponse>(`/despesas/${id}`, data).then((r) => r.data),

  desativar: (id: number) =>
    api.delete(`/despesas/${id}`)
}

// Categorias
export const categoriaService = {
  listar: () =>
    api.get<CategoriaResponse[]>('/categorias').then((r) => r.data),

  criar: (data: CategoriaRequest) =>
    api.post<CategoriaResponse>('/categorias', data).then((r) => r.data),

  atualizar: (id: number, data: CategoriaRequest) =>
    api.put<CategoriaResponse>(`/categorias/${id}`, data).then((r) => r.data),

  desativar: (id: number) =>
    api.delete(`/categorias/${id}`)
}

// Contas
export const contaService = {
  listar: () =>
    api.get<ContaResponse[]>('/contas').then((r) => r.data),

  criar: (data: ContaRequest) =>
    api.post<ContaResponse>('/contas', data).then((r) => r.data),

  atualizar: (id: number, data: ContaRequest) =>
    api.put<ContaResponse>(`/contas/${id}`, data).then((r) => r.data),

  desativar: (id: number) =>
    api.delete(`/contas/${id}`)
}

export default api

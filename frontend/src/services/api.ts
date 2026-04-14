import axios from 'axios'
import type {
  LoginRequest, LoginResponse,
  ReceitaRequest, ReceitaResponse,
  ReceitaValorRequest, ReceitaValorResponse
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

export default api

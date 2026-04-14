import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { authService } from '@/services/api'
import type { LoginRequest, Usuario } from '@/types'

const TOKEN_KEY = 'scf_token'
const USER_KEY = 'scf_usuario'

export const useAuthStore = defineStore('auth', () => {
  const token = ref<string | null>(localStorage.getItem(TOKEN_KEY))
  const usuario = ref<Usuario | null>(
    JSON.parse(localStorage.getItem(USER_KEY) || 'null')
  )

  const isAuthenticated = computed(() => !!token.value)

  async function login(credentials: LoginRequest) {
    const response = await authService.login(credentials)

    token.value = response.token
    usuario.value = { nome: response.nome, email: response.email }

    localStorage.setItem(TOKEN_KEY, response.token)
    localStorage.setItem(USER_KEY, JSON.stringify(usuario.value))
  }

  function logout() {
    token.value = null
    usuario.value = null
    localStorage.removeItem(TOKEN_KEY)
    localStorage.removeItem(USER_KEY)
  }

  return { token, usuario, isAuthenticated, login, logout }
})

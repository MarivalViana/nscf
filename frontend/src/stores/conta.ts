import { defineStore } from 'pinia'
import { ref } from 'vue'
import { contaService } from '@/services/api'
import type { ContaResponse, ContaRequest } from '@/types'

export const useContaStore = defineStore('conta', () => {
  const contas = ref<ContaResponse[]>([])
  const carregando = ref(false)

  async function carregar() {
    carregando.value = true
    try {
      contas.value = await contaService.listar()
    } finally {
      carregando.value = false
    }
  }

  async function criar(data: ContaRequest): Promise<ContaResponse> {
    const nova = await contaService.criar(data)
    contas.value.push(nova)
    contas.value.sort((a, b) => a.nome.localeCompare(b.nome))
    return nova
  }

  async function atualizar(id: number, data: ContaRequest): Promise<ContaResponse> {
    const atualizada = await contaService.atualizar(id, data)
    const idx = contas.value.findIndex((c) => c.id === id)
    if (idx !== -1) contas.value[idx] = atualizada
    return atualizada
  }

  async function desativar(id: number) {
    await contaService.desativar(id)
    contas.value = contas.value.filter((c) => c.id !== id)
  }

  return { contas, carregando, carregar, criar, atualizar, desativar }
})

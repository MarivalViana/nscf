import { defineStore } from 'pinia'
import { ref } from 'vue'
import { despesaService } from '@/services/api'
import type { DespesaResponse, DespesaRequest } from '@/types'

export const useDespesaStore = defineStore('despesa', () => {
  const despesas = ref<DespesaResponse[]>([])
  const carregando = ref(false)

  async function carregar() {
    carregando.value = true
    try {
      despesas.value = await despesaService.listar()
    } finally {
      carregando.value = false
    }
  }

  async function criar(data: DespesaRequest): Promise<DespesaResponse> {
    const nova = await despesaService.criar(data)
    despesas.value.push(nova)
    despesas.value.sort((a, b) => a.descricao.localeCompare(b.descricao))
    return nova
  }

  async function atualizar(id: number, data: DespesaRequest): Promise<DespesaResponse> {
    const atualizada = await despesaService.atualizar(id, data)
    const idx = despesas.value.findIndex((d) => d.id === id)
    if (idx !== -1) despesas.value[idx] = atualizada
    return atualizada
  }

  async function desativar(id: number) {
    await despesaService.desativar(id)
    despesas.value = despesas.value.filter((d) => d.id !== id)
  }

  return { despesas, carregando, carregar, criar, atualizar, desativar }
})

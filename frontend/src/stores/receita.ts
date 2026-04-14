import { defineStore } from 'pinia'
import { ref } from 'vue'
import { receitaService } from '@/services/api'
import type { ReceitaResponse, ReceitaRequest, ReceitaValorRequest } from '@/types'

export const useReceitaStore = defineStore('receita', () => {
  const receitas = ref<ReceitaResponse[]>([])
  const carregando = ref(false)

  async function carregar() {
    carregando.value = true
    try {
      receitas.value = await receitaService.listar()
    } finally {
      carregando.value = false
    }
  }

  async function criar(data: ReceitaRequest): Promise<ReceitaResponse> {
    const nova = await receitaService.criar(data)
    receitas.value.push(nova)
    receitas.value.sort((a, b) => a.descricao.localeCompare(b.descricao))
    return nova
  }

  async function atualizar(id: number, data: ReceitaRequest): Promise<ReceitaResponse> {
    const atualizada = await receitaService.atualizar(id, data)
    const idx = receitas.value.findIndex((r) => r.id === id)
    if (idx !== -1) receitas.value[idx] = atualizada
    return atualizada
  }

  async function desativar(id: number) {
    await receitaService.desativar(id)
    receitas.value = receitas.value.filter((r) => r.id !== id)
  }

  async function adicionarValor(receitaId: number, data: ReceitaValorRequest) {
    await receitaService.adicionarValor(receitaId, data)
    // Recarrega a receita específica para atualizar valores
    const atualizada = await receitaService.buscarPorId(receitaId)
    const idx = receitas.value.findIndex((r) => r.id === receitaId)
    if (idx !== -1) receitas.value[idx] = atualizada
  }

  async function removerValor(receitaId: number, valorId: number) {
    await receitaService.removerValor(valorId)
    const atualizada = await receitaService.buscarPorId(receitaId)
    const idx = receitas.value.findIndex((r) => r.id === receitaId)
    if (idx !== -1) receitas.value[idx] = atualizada
  }

  return { receitas, carregando, carregar, criar, atualizar, desativar, adicionarValor, removerValor }
})

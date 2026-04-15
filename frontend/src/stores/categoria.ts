import { defineStore } from 'pinia'
import { ref } from 'vue'
import { categoriaService } from '@/services/api'
import type { CategoriaResponse, CategoriaRequest } from '@/types'

export const useCategoriaStore = defineStore('categoria', () => {
  const categorias = ref<CategoriaResponse[]>([])
  const carregando = ref(false)

  async function carregar() {
    carregando.value = true
    try {
      categorias.value = await categoriaService.listar()
    } finally {
      carregando.value = false
    }
  }

  async function criar(data: CategoriaRequest): Promise<CategoriaResponse> {
    const nova = await categoriaService.criar(data)
    categorias.value.push(nova)
    categorias.value.sort((a, b) => a.nome.localeCompare(b.nome))
    return nova
  }

  async function atualizar(id: number, data: CategoriaRequest): Promise<CategoriaResponse> {
    const atualizada = await categoriaService.atualizar(id, data)
    const idx = categorias.value.findIndex((c) => c.id === id)
    if (idx !== -1) categorias.value[idx] = atualizada
    return atualizada
  }

  async function desativar(id: number) {
    await categoriaService.desativar(id)
    categorias.value = categorias.value.filter((c) => c.id !== id)
  }

  return { categorias, carregando, carregar, criar, atualizar, desativar }
})

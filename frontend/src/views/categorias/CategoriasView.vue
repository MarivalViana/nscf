<template>
  <div class="page">
    <div class="page-header">
      <div>
        <h2>Categorias</h2>
        <p>Organize suas despesas por tipo de gasto</p>
      </div>
      <Button label="Nova Categoria" icon="pi pi-plus" @click="abrirFormNovo" />
    </div>

    <DataTable
      :value="categoriaStore.categorias"
      :loading="categoriaStore.carregando"
      striped-rows
      empty-message="Nenhuma categoria cadastrada"
    >
      <Column header="Ícone" style="width: 70px">
        <template #body="{ data }">
          <i :class="[data.icone, 'icone-lista']" />
        </template>
      </Column>

      <Column field="nome" header="Nome" />

      <Column header="Ações" style="width: 100px">
        <template #body="{ data }">
          <div class="acoes">
            <Button
              icon="pi pi-pencil"
              severity="secondary"
              text
              rounded
              v-tooltip="'Editar'"
              @click="abrirFormEditar(data)"
            />
            <Button
              icon="pi pi-trash"
              severity="danger"
              text
              rounded
              v-tooltip="'Excluir'"
              @click="confirmarDesativar(data)"
            />
          </div>
        </template>
      </Column>
    </DataTable>

    <CategoriaForm
      v-model:visible="formVisible"
      :categoria="categoriaSelecionada"
      @salvo="onSalvo"
    />

    <ConfirmDialog />
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useConfirm } from 'primevue/useconfirm'
import { useToast } from 'primevue/usetoast'
import DataTable from 'primevue/datatable'
import Column from 'primevue/column'
import Button from 'primevue/button'
import ConfirmDialog from 'primevue/confirmdialog'
import CategoriaForm from '@/components/categorias/CategoriaForm.vue'
import { useCategoriaStore } from '@/stores/categoria'
import type { CategoriaResponse, CategoriaRequest } from '@/types'

const categoriaStore = useCategoriaStore()
const confirm = useConfirm()
const toast = useToast()

const formVisible = ref(false)
const categoriaSelecionada = ref<CategoriaResponse | null>(null)

onMounted(() => categoriaStore.carregar())

function abrirFormNovo() {
  categoriaSelecionada.value = null
  formVisible.value = true
}

function abrirFormEditar(categoria: CategoriaResponse) {
  categoriaSelecionada.value = categoria
  formVisible.value = true
}

async function onSalvo(data: CategoriaRequest) {
  try {
    if (categoriaSelecionada.value) {
      await categoriaStore.atualizar(categoriaSelecionada.value.id, data)
      toast.add({ severity: 'success', summary: 'Atualizada', detail: 'Categoria atualizada', life: 3000 })
    } else {
      await categoriaStore.criar(data)
      toast.add({ severity: 'success', summary: 'Criada', detail: 'Categoria criada', life: 3000 })
    }
  } catch {
    toast.add({ severity: 'error', summary: 'Erro', detail: 'Não foi possível salvar', life: 3000 })
  }
}

function confirmarDesativar(categoria: CategoriaResponse) {
  confirm.require({
    message: `Deseja excluir "${categoria.nome}"?`,
    header: 'Confirmar exclusão',
    icon: 'pi pi-exclamation-triangle',
    acceptLabel: 'Excluir',
    rejectLabel: 'Cancelar',
    acceptSeverity: 'danger',
    accept: async () => {
      await categoriaStore.desativar(categoria.id)
      toast.add({ severity: 'success', summary: 'Excluída', detail: 'Categoria removida', life: 3000 })
    }
  })
}
</script>

<style scoped>
.page { padding: 2rem; }
.page-header {
  display: flex; align-items: flex-start; justify-content: space-between;
  margin-bottom: 1.5rem;
}
.page-header h2 { font-size: 1.5rem; font-weight: 700; color: #1a1a2e; margin-bottom: 0.25rem; }
.page-header p { color: #6c757d; font-size: 0.9rem; }
.acoes { display: flex; gap: 0.25rem; }
.icone-lista { font-size: 1.1rem; color: #6c757d; }
</style>

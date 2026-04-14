<template>
  <div class="page">
    <div class="page-header">
      <div>
        <h2>Receitas</h2>
        <p>Gerencie suas fontes de renda</p>
      </div>
      <Button label="Nova Receita" icon="pi pi-plus" @click="abrirFormNovo" />
    </div>

    <DataTable
      :value="receitaStore.receitas"
      :loading="receitaStore.carregando"
      striped-rows
      empty-message="Nenhuma receita cadastrada"
    >
      <Column field="descricao" header="Descrição" />
      <Column field="tipo" header="Tipo">
        <template #body="{ data }">
          <Tag
            :value="data.tipo === 'RECORRENTE' ? 'Recorrente' : 'Pontual'"
            :severity="data.tipo === 'RECORRENTE' ? 'success' : 'info'"
          />
        </template>
      </Column>
      <Column header="Valor Atual">
        <template #body="{ data }">
          <span>{{ valorAtual(data) }}</span>
        </template>
      </Column>
      <Column header="Ações" style="width: 160px">
        <template #body="{ data }">
          <div class="acoes">
            <Button
              icon="pi pi-list"
              severity="info"
              text
              rounded
              v-tooltip="'Ver histórico'"
              @click="$router.push(`/receitas/${data.id}`)"
            />
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

    <ReceitaForm
      v-model:visible="formVisible"
      :receita="receitaSelecionada"
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
import Tag from 'primevue/tag'
import ConfirmDialog from 'primevue/confirmdialog'
import ReceitaForm from '@/components/receitas/ReceitaForm.vue'
import { useReceitaStore } from '@/stores/receita'
import type { ReceitaResponse, ReceitaRequest } from '@/types'

const receitaStore = useReceitaStore()
const confirm = useConfirm()
const toast = useToast()

const formVisible = ref(false)
const receitaSelecionada = ref<ReceitaResponse | null>(null)

onMounted(() => receitaStore.carregar())

function valorAtual(receita: ReceitaResponse): string {
  const vigente = receita.valores.find((v) => v.vigente)
  const ultimo = receita.valores.at(-1)
  const rv = vigente ?? ultimo
  if (!rv) return '—'
  return rv.valor.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })
}

function abrirFormNovo() {
  receitaSelecionada.value = null
  formVisible.value = true
}

function abrirFormEditar(receita: ReceitaResponse) {
  receitaSelecionada.value = receita
  formVisible.value = true
}

async function onSalvo(data: ReceitaRequest) {
  try {
    if (receitaSelecionada.value) {
      await receitaStore.atualizar(receitaSelecionada.value.id, data)
      toast.add({ severity: 'success', summary: 'Atualizada', detail: 'Receita atualizada', life: 3000 })
    } else {
      await receitaStore.criar(data)
      toast.add({ severity: 'success', summary: 'Criada', detail: 'Receita criada', life: 3000 })
    }
  } catch {
    toast.add({ severity: 'error', summary: 'Erro', detail: 'Não foi possível salvar', life: 3000 })
  }
}

function confirmarDesativar(receita: ReceitaResponse) {
  confirm.require({
    message: `Deseja excluir "${receita.descricao}"?`,
    header: 'Confirmar exclusão',
    icon: 'pi pi-exclamation-triangle',
    acceptLabel: 'Excluir',
    rejectLabel: 'Cancelar',
    acceptSeverity: 'danger',
    accept: async () => {
      await receitaStore.desativar(receita.id)
      toast.add({ severity: 'success', summary: 'Excluída', detail: 'Receita removida', life: 3000 })
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
</style>

<template>
  <div class="page">
    <div class="page-header">
      <div>
        <h2>Contas e Cartões</h2>
        <p>Gerencie seus meios de pagamento</p>
      </div>
      <Button label="Nova Conta" icon="pi pi-plus" @click="abrirFormNovo" />
    </div>

    <DataTable
      :value="contaStore.contas"
      :loading="contaStore.carregando"
      striped-rows
      empty-message="Nenhuma conta cadastrada"
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

    <ContaForm
      v-model:visible="formVisible"
      :conta="contaSelecionada"
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
import ContaForm from '@/components/contas/ContaForm.vue'
import { useContaStore } from '@/stores/conta'
import type { ContaResponse, ContaRequest } from '@/types'

const contaStore = useContaStore()
const confirm = useConfirm()
const toast = useToast()

const formVisible = ref(false)
const contaSelecionada = ref<ContaResponse | null>(null)

onMounted(() => contaStore.carregar())

function abrirFormNovo() {
  contaSelecionada.value = null
  formVisible.value = true
}

function abrirFormEditar(conta: ContaResponse) {
  contaSelecionada.value = conta
  formVisible.value = true
}

async function onSalvo(data: ContaRequest) {
  try {
    if (contaSelecionada.value) {
      await contaStore.atualizar(contaSelecionada.value.id, data)
      toast.add({ severity: 'success', summary: 'Atualizada', detail: 'Conta atualizada', life: 3000 })
    } else {
      await contaStore.criar(data)
      toast.add({ severity: 'success', summary: 'Criada', detail: 'Conta criada', life: 3000 })
    }
  } catch {
    toast.add({ severity: 'error', summary: 'Erro', detail: 'Não foi possível salvar', life: 3000 })
  }
}

function confirmarDesativar(conta: ContaResponse) {
  confirm.require({
    message: `Deseja excluir "${conta.nome}"?`,
    header: 'Confirmar exclusão',
    icon: 'pi pi-exclamation-triangle',
    acceptLabel: 'Excluir',
    rejectLabel: 'Cancelar',
    acceptSeverity: 'danger',
    accept: async () => {
      await contaStore.desativar(conta.id)
      toast.add({ severity: 'success', summary: 'Excluída', detail: 'Conta removida', life: 3000 })
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

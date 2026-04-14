<template>
  <div class="page">
    <div class="page-header">
      <div class="header-left">
        <Button icon="pi pi-arrow-left" text @click="$router.push('/receitas')" />
        <div>
          <h2>{{ receita?.descricao }}</h2>
          <Tag
            v-if="receita"
            :value="receita.tipo === 'RECORRENTE' ? 'Recorrente' : 'Pontual'"
            :severity="receita.tipo === 'RECORRENTE' ? 'success' : 'info'"
          />
        </div>
      </div>
      <Button label="Novo Período" icon="pi pi-plus" @click="abrirFormNovo" />
    </div>

    <DataTable
      :value="receita?.valores ?? []"
      :loading="carregando"
      striped-rows
      empty-message="Nenhum período cadastrado"
    >
      <Column header="Período início">
        <template #body="{ data }">
          {{ nomeMes(data.mesInicio) }}/{{ data.anoInicio }}
        </template>
      </Column>
      <Column header="Período fim">
        <template #body="{ data }">
          <Tag v-if="data.vigente" value="Vigente" severity="success" />
          <span v-else>{{ nomeMes(data.mesFim) }}/{{ data.anoFim }}</span>
        </template>
      </Column>
      <Column header="Valor">
        <template #body="{ data }">
          <strong>{{ formatarValor(data.valor) }}</strong>
        </template>
      </Column>
      <Column header="Ações" style="width: 120px">
        <template #body="{ data }">
          <div class="acoes">
            <Button icon="pi pi-pencil" severity="secondary" text rounded @click="abrirFormEditar(data)" />
            <Button icon="pi pi-trash" severity="danger" text rounded @click="confirmarRemover(data)" />
          </div>
        </template>
      </Column>
    </DataTable>

    <ReceitaValorForm
      v-model:visible="formVisible"
      :valor-editando="valorSelecionado"
      @salvo="onSalvo"
    />

    <ConfirmDialog />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useToast } from 'primevue/usetoast'
import { useConfirm } from 'primevue/useconfirm'
import DataTable from 'primevue/datatable'
import Column from 'primevue/column'
import Button from 'primevue/button'
import Tag from 'primevue/tag'
import ConfirmDialog from 'primevue/confirmdialog'
import ReceitaValorForm from '@/components/receitas/ReceitaValorForm.vue'
import { receitaService } from '@/services/api'
import { useReceitaStore } from '@/stores/receita'
import type { ReceitaResponse, ReceitaValorRequest, ReceitaValorResponse } from '@/types'

const route = useRoute()
const toast = useToast()
const confirm = useConfirm()
const receitaStore = useReceitaStore()

const receitaId = Number(route.params.id)
const receita = ref<ReceitaResponse | null>(null)
const carregando = ref(false)
const formVisible = ref(false)
const valorSelecionado = ref<ReceitaValorResponse | null>(null)

const meses = ['Jan','Fev','Mar','Abr','Mai','Jun','Jul','Ago','Set','Out','Nov','Dez']
const nomeMes = (m: number | null) => m ? meses[m - 1] : '—'
const formatarValor = (v: number) => v.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })

async function carregar() {
  carregando.value = true
  try {
    receita.value = await receitaService.buscarPorId(receitaId)
  } finally {
    carregando.value = false
  }
}

onMounted(carregar)

function abrirFormNovo() {
  valorSelecionado.value = null
  formVisible.value = true
}

function abrirFormEditar(valor: ReceitaValorResponse) {
  valorSelecionado.value = valor
  formVisible.value = true
}

async function onSalvo(data: ReceitaValorRequest) {
  try {
    if (valorSelecionado.value) {
      await receitaStore.removerValor(receitaId, valorSelecionado.value.id)
      await receitaStore.adicionarValor(receitaId, data)
    } else {
      await receitaStore.adicionarValor(receitaId, data)
    }
    await carregar()
    toast.add({ severity: 'success', summary: 'Salvo', detail: 'Período salvo', life: 3000 })
  } catch {
    toast.add({ severity: 'error', summary: 'Erro', detail: 'Não foi possível salvar', life: 3000 })
  }
}

function confirmarRemover(valor: ReceitaValorResponse) {
  confirm.require({
    message: 'Deseja remover este período?',
    header: 'Confirmar',
    icon: 'pi pi-exclamation-triangle',
    acceptLabel: 'Remover',
    rejectLabel: 'Cancelar',
    acceptSeverity: 'danger',
    accept: async () => {
      await receitaStore.removerValor(receitaId, valor.id)
      await carregar()
      toast.add({ severity: 'success', summary: 'Removido', detail: 'Período removido', life: 3000 })
    }
  })
}
</script>

<style scoped>
.page { padding: 2rem; }
.page-header {
  display: flex; align-items: center; justify-content: space-between; margin-bottom: 1.5rem;
}
.header-left { display: flex; align-items: center; gap: 0.75rem; }
.header-left h2 { font-size: 1.5rem; font-weight: 700; color: #1a1a2e; margin-bottom: 0.25rem; }
.acoes { display: flex; gap: 0.25rem; }
</style>

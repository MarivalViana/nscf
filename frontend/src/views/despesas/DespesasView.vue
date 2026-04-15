<template>
  <div class="page">
    <div class="page-header">
      <div>
        <h2>Despesas</h2>
        <p>Gerencie seus gastos e compromissos financeiros</p>
      </div>
      <Button label="Nova Despesa" icon="pi pi-plus" @click="abrirFormNovo" />
    </div>

    <DataTable
      :value="despesaStore.despesas"
      :loading="despesaStore.carregando"
      striped-rows
      empty-message="Nenhuma despesa cadastrada"
    >
      <Column field="descricao" header="Descrição" />

      <Column header="Conta / Cartão" style="width: 160px">
        <template #body="{ data }">
          <span v-if="data.conta" class="categoria-chip">
            <i :class="data.conta.icone" />
            {{ data.conta.nome }}
          </span>
          <span v-else class="sem-categoria">—</span>
        </template>
      </Column>

      <Column header="Categoria" style="width: 160px">
        <template #body="{ data }">
          <span v-if="data.categoria" class="categoria-chip">
            <i :class="data.categoria.icone" />
            {{ data.categoria.nome }}
          </span>
          <span v-else class="sem-categoria">—</span>
        </template>
      </Column>

      <Column header="Tipo" style="width: 140px">
        <template #body="{ data }">
          <Tag :value="labelTipo(data.tipo)" :severity="severidadeTipo(data.tipo)" />
        </template>
      </Column>

      <Column header="Valor" style="width: 160px">
        <template #body="{ data }">
          <div class="valor-col">
            <strong>{{ fmt(data.valorParcela) }}</strong>
            <small v-if="data.tipo === 'PARCELADA'">
              {{ data.qtdParcelas }}x · total {{ fmt(data.valor) }}
            </small>
            <small v-else-if="data.tipo === 'RECORRENTE'">por mês</small>
          </div>
        </template>
      </Column>

      <Column header="Período" style="width: 180px">
        <template #body="{ data }">
          <span v-if="data.tipo === 'RECORRENTE'">
            desde {{ nomeMes(data.mesInicio) }}/{{ data.anoInicio }}
          </span>
          <span v-else-if="data.tipo === 'ANUAL'">
            todo {{ nomeMes(data.mesInicio) }}, desde {{ data.anoInicio }}
          </span>
          <span v-else-if="data.tipo === 'PARCELADA'">
            {{ nomeMes(data.mesInicio) }}/{{ data.anoInicio }}
            → {{ ultimaParcela(data) }}
          </span>
          <span v-else>
            {{ nomeMes(data.mesInicio) }}/{{ data.anoInicio }}
          </span>
        </template>
      </Column>

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

    <DespesaForm
      v-model:visible="formVisible"
      :despesa="despesaSelecionada"
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
import DespesaForm from '@/components/despesas/DespesaForm.vue'
import { useDespesaStore } from '@/stores/despesa'
import type { DespesaResponse, DespesaRequest, TipoDespesa } from '@/types'

const despesaStore = useDespesaStore()
const confirm = useConfirm()
const toast = useToast()

const formVisible = ref(false)
const despesaSelecionada = ref<DespesaResponse | null>(null)

const meses = ['Jan','Fev','Mar','Abr','Mai','Jun','Jul','Ago','Set','Out','Nov','Dez']
const nomeMes = (m: number) => meses[m - 1] ?? '—'
const fmt = (v: number) => v.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })

function labelTipo(tipo: TipoDespesa): string {
  return { AVISTA: 'À Vista', PARCELADA: 'Parcelada', RECORRENTE: 'Recorrente', ANUAL: 'Anual' }[tipo]
}

function severidadeTipo(tipo: TipoDespesa) {
  return { AVISTA: 'secondary', PARCELADA: 'info', RECORRENTE: 'warn', ANUAL: 'success' }[tipo]
}

function ultimaParcela(d: DespesaResponse): string {
  if (!d.qtdParcelas || d.qtdParcelas <= 1) return nomeMes(d.mesInicio) + '/' + d.anoInicio
  const totalMeses = d.mesInicio + d.qtdParcelas - 2
  const mesFim = (totalMeses % 12) + 1
  const anoFim = d.anoInicio + Math.floor(totalMeses / 12)
  return `${nomeMes(mesFim)}/${anoFim}`
}

onMounted(() => despesaStore.carregar())

function abrirFormNovo() {
  despesaSelecionada.value = null
  formVisible.value = true
}

function abrirFormEditar(despesa: DespesaResponse) {
  despesaSelecionada.value = despesa
  formVisible.value = true
}

async function onSalvo(data: DespesaRequest) {
  try {
    if (despesaSelecionada.value) {
      await despesaStore.atualizar(despesaSelecionada.value.id, data)
      toast.add({ severity: 'success', summary: 'Atualizada', detail: 'Despesa atualizada', life: 3000 })
    } else {
      await despesaStore.criar(data)
      toast.add({ severity: 'success', summary: 'Criada', detail: 'Despesa criada com sucesso', life: 3000 })
    }
  } catch {
    toast.add({ severity: 'error', summary: 'Erro', detail: 'Não foi possível salvar', life: 3000 })
  }
}

function confirmarDesativar(despesa: DespesaResponse) {
  confirm.require({
    message: `Deseja excluir "${despesa.descricao}"?`,
    header: 'Confirmar exclusão',
    icon: 'pi pi-exclamation-triangle',
    acceptLabel: 'Excluir',
    rejectLabel: 'Cancelar',
    acceptSeverity: 'danger',
    accept: async () => {
      await despesaStore.desativar(despesa.id)
      toast.add({ severity: 'success', summary: 'Excluída', detail: 'Despesa removida', life: 3000 })
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
.valor-col { display: flex; flex-direction: column; gap: 0.1rem; }
.valor-col small { color: #6c757d; font-size: 0.78rem; }
.categoria-chip { display: inline-flex; align-items: center; gap: 0.4rem; font-size: 0.875rem; }
.categoria-chip i { font-size: 0.85rem; color: #6c757d; }
.sem-categoria { color: #9ca3af; }
</style>

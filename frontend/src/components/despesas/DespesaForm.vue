<template>
  <Dialog
    :visible="visible"
    :header="despesa ? 'Editar Despesa' : 'Nova Despesa'"
    :style="{ width: '480px' }"
    modal
    @update:visible="$emit('update:visible', $event)"
  >
    <form @submit.prevent="salvar" class="form">
      <div class="field">
        <label for="descricao">Descrição</label>
        <InputText
          id="descricao"
          v-model="form.descricao"
          placeholder="Ex: Aluguel, Netflix, Supermercado..."
          :class="{ 'p-invalid': erros.descricao }"
          fluid
        />
        <small v-if="erros.descricao" class="erro">{{ erros.descricao }}</small>
      </div>

      <div class="grid-2">
        <div class="field">
          <label for="tipo">Tipo</label>
          <Select
            id="tipo"
            v-model="form.tipo"
            :options="tiposDespesa"
            option-label="label"
            option-value="value"
            placeholder="Selecione o tipo"
            :class="{ 'p-invalid': erros.tipo }"
            fluid
          />
          <small v-if="erros.tipo" class="erro">{{ erros.tipo }}</small>
        </div>

        <div class="field">
          <label for="conta">Conta / Cartão</label>
          <Select
            id="conta"
            v-model="form.contaId"
            :options="opcoesConta"
            option-label="label"
            option-value="value"
            placeholder="Selecione"
            fluid
          />
        </div>
      </div>

      <div class="field">
        <label for="categoria">Categoria</label>
        <Select
          id="categoria"
          v-model="form.categoriaId"
          :options="opcoesCategoria"
          option-label="label"
          option-value="value"
          placeholder="Sem categoria"
          fluid
        />
      </div>

      <div class="field">
        <label>Valor (R$)</label>
        <InputNumber
          v-model="form.valor"
          mode="currency"
          currency="BRL"
          locale="pt-BR"
          :min="0.01"
          :class="{ 'p-invalid': erros.valor }"
          fluid
        />
        <small v-if="erros.valor" class="erro">{{ erros.valor }}</small>
      </div>

      <!-- PARCELADA: exibe campo de parcelas + preview -->
      <template v-if="form.tipo === 'PARCELADA'">
        <div class="field">
          <label>Número de parcelas</label>
          <InputNumber
            v-model="form.qtdParcelas"
            :min="2"
            :max="360"
            :use-grouping="false"
            :class="{ 'p-invalid': erros.qtdParcelas }"
            fluid
          />
          <small v-if="erros.qtdParcelas" class="erro">{{ erros.qtdParcelas }}</small>
          <small v-if="form.valor && form.qtdParcelas && form.qtdParcelas > 0" class="dica">
            <i class="pi pi-calculator" />
            {{ form.qtdParcelas }}x de {{ valorParcela }}
          </small>
        </div>
      </template>

      <Divider />

      <p class="secao-label">
        {{ labelSecaoData }}
      </p>

      <div class="grid-2">
        <div class="field">
          <label>{{ labelMes }}</label>
          <Select
            v-model="form.mesInicio"
            :options="meses"
            option-label="label"
            option-value="value"
            fluid
          />
        </div>
        <div class="field">
          <label>{{ labelAno }}</label>
          <InputNumber
            v-model="form.anoInicio"
            :use-grouping="false"
            :min="2000"
            fluid
          />
        </div>
      </div>

      <small v-if="form.tipo === 'RECORRENTE'" class="dica">
        <i class="pi pi-info-circle" /> Despesa recorrente — debitada todo mês a partir desta data.
      </small>
      <small v-if="form.tipo === 'ANUAL'" class="dica">
        <i class="pi pi-sync" /> Pagamento anual — cobrado todo ano neste mês.
      </small>
      <small v-if="form.tipo === 'PARCELADA' && form.qtdParcelas && ultimaParcela" class="dica">
        <i class="pi pi-calendar" /> Última parcela em {{ ultimaParcela }}.
      </small>
    </form>

    <template #footer>
      <Button label="Cancelar" severity="secondary" text @click="$emit('update:visible', false)" />
      <Button label="Salvar" :loading="salvando" @click="salvar" />
    </template>
  </Dialog>
</template>

<script setup lang="ts">
import { ref, computed, watch, onMounted } from 'vue'
import Dialog from 'primevue/dialog'
import InputText from 'primevue/inputtext'
import InputNumber from 'primevue/inputnumber'
import Select from 'primevue/select'
import Button from 'primevue/button'
import Divider from 'primevue/divider'
import { useCategoriaStore } from '@/stores/categoria'
import { useContaStore } from '@/stores/conta'
import type { DespesaResponse, DespesaRequest, TipoDespesa } from '@/types'

const props = defineProps<{
  visible: boolean
  despesa?: DespesaResponse | null
}>()

const emit = defineEmits<{
  'update:visible': [value: boolean]
  salvo: [data: DespesaRequest]
}>()

const categoriaStore = useCategoriaStore()
const contaStore = useContaStore()

onMounted(() => {
  if (categoriaStore.categorias.length === 0) categoriaStore.carregar()
  if (contaStore.contas.length === 0) contaStore.carregar()
})

const opcoesCategoria = computed(() => [
  { label: 'Sem categoria', value: null },
  ...categoriaStore.categorias.map((c) => ({ label: c.nome, value: c.id }))
])

const opcoesConta = computed(() => [
  { label: 'Sem conta', value: null },
  ...contaStore.contas.map((c) => ({ label: c.nome, value: c.id }))
])

const tiposDespesa = [
  { label: 'Recorrente (todo mês)', value: 'RECORRENTE' },
  { label: 'Anual (todo ano)', value: 'ANUAL' },
  { label: 'Parcelada (N vezes)', value: 'PARCELADA' },
  { label: 'À vista (pagamento único)', value: 'AVISTA' }
]

const meses = [
  { label: 'Janeiro', value: 1 }, { label: 'Fevereiro', value: 2 },
  { label: 'Março', value: 3 }, { label: 'Abril', value: 4 },
  { label: 'Maio', value: 5 }, { label: 'Junho', value: 6 },
  { label: 'Julho', value: 7 }, { label: 'Agosto', value: 8 },
  { label: 'Setembro', value: 9 }, { label: 'Outubro', value: 10 },
  { label: 'Novembro', value: 11 }, { label: 'Dezembro', value: 12 }
]

const nomeMeses = ['Jan','Fev','Mar','Abr','Mai','Jun','Jul','Ago','Set','Out','Nov','Dez']

const anoAtual = new Date().getFullYear()
const mesAtual = new Date().getMonth() + 1

const form = ref<DespesaRequest>({
  descricao: '', tipo: 'RECORRENTE', valor: 0,
  qtdParcelas: null, mesInicio: mesAtual, anoInicio: anoAtual, categoriaId: null, contaId: null
})
const erros = ref({ descricao: '', tipo: '', valor: '', qtdParcelas: '' })
const salvando = ref(false)

const valorParcela = computed(() => {
  if (!form.value.valor || !form.value.qtdParcelas || form.value.qtdParcelas <= 0) return ''
  return (form.value.valor / form.value.qtdParcelas)
    .toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })
})

const ultimaParcela = computed(() => {
  const { mesInicio, anoInicio, qtdParcelas } = form.value
  if (!mesInicio || !anoInicio || !qtdParcelas || qtdParcelas <= 1) return null
  const totalMeses = mesInicio + qtdParcelas - 2
  const mesFim = (totalMeses % 12) + 1
  const anoFim = anoInicio + Math.floor(totalMeses / 12)
  return `${nomeMeses[mesFim - 1]}/${anoFim}`
})

const labelSecaoData = computed(() => {
  if (form.value.tipo === 'AVISTA') return 'Data do pagamento'
  if (form.value.tipo === 'ANUAL') return 'Mês do pagamento anual'
  return 'Início'
})

const labelMes = computed(() => (form.value.tipo === 'AVISTA' || form.value.tipo === 'ANUAL') ? 'Mês' : 'Mês início')
const labelAno = computed(() => (form.value.tipo === 'AVISTA' || form.value.tipo === 'ANUAL') ? 'Ano (1º pagamento)' : 'Ano início')

watch(
  () => props.visible,
  (val) => {
    if (val) {
      form.value = props.despesa
        ? {
            descricao: props.despesa.descricao,
            tipo: props.despesa.tipo,
            valor: props.despesa.valor,
            qtdParcelas: props.despesa.qtdParcelas,
            mesInicio: props.despesa.mesInicio,
            anoInicio: props.despesa.anoInicio,
            categoriaId: props.despesa.categoria?.id ?? null,
            contaId: props.despesa.conta?.id ?? null
          }
        : {
            descricao: '', tipo: 'RECORRENTE', valor: 0,
            qtdParcelas: null, mesInicio: mesAtual, anoInicio: anoAtual, categoriaId: null, contaId: null
          }
      erros.value = { descricao: '', tipo: '', valor: '', qtdParcelas: '' }
    }
  }
)

watch(() => form.value.tipo, (tipo) => {
  if (tipo !== 'PARCELADA') form.value.qtdParcelas = null
  if (tipo === 'PARCELADA' && !form.value.qtdParcelas) form.value.qtdParcelas = 2
})

function validar(): boolean {
  erros.value = { descricao: '', tipo: '', valor: '', qtdParcelas: '' }
  let valido = true

  if (!form.value.descricao.trim()) {
    erros.value.descricao = 'Descrição é obrigatória'
    valido = false
  }
  if (!form.value.tipo) {
    erros.value.tipo = 'Tipo é obrigatório'
    valido = false
  }
  if (!form.value.valor || form.value.valor <= 0) {
    erros.value.valor = 'Informe um valor válido'
    valido = false
  }
  if (form.value.tipo === 'PARCELADA' && (!form.value.qtdParcelas || form.value.qtdParcelas < 2)) {
    erros.value.qtdParcelas = 'Informe pelo menos 2 parcelas'
    valido = false
  }

  return valido
}

async function salvar() {
  if (!validar()) return
  salvando.value = true
  try {
    emit('salvo', { ...form.value })
    emit('update:visible', false)
  } finally {
    salvando.value = false
  }
}
</script>

<style scoped>
.form { display: flex; flex-direction: column; gap: 1rem; }
.grid-2 { display: grid; grid-template-columns: 1fr 1fr; gap: 1rem; }
.field { display: flex; flex-direction: column; gap: 0.4rem; }
.field label { font-weight: 500; font-size: 0.9rem; color: #374151; }
.erro { color: #ef4444; font-size: 0.8rem; }
.dica { color: #6c757d; font-size: 0.8rem; display: flex; align-items: center; gap: 0.3rem; }
.secao-label { font-size: 0.8rem; font-weight: 600; color: #6c757d; text-transform: uppercase; letter-spacing: 0.05em; margin: -0.25rem 0; }
</style>

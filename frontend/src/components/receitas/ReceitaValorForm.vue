<template>
  <Dialog
    :visible="visible"
    :header="valorEditando ? 'Editar Período' : 'Novo Período / Valor'"
    :style="{ width: '460px' }"
    modal
    @update:visible="$emit('update:visible', $event)"
  >
    <form @submit.prevent="salvar" class="form">
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

      <!-- RECORRENTE: início obrigatório, fim opcional -->
      <template v-if="tipo === 'RECORRENTE'">
        <div class="grid-2">
          <div class="field">
            <label>Mês início</label>
            <Select
              v-model="form.mesInicio"
              :options="meses"
              option-label="label"
              option-value="value"
              placeholder="Mês"
              fluid
            />
          </div>
          <div class="field">
            <label>Ano início</label>
            <InputNumber v-model="form.anoInicio" :use-grouping="false" :min="2000" fluid />
          </div>
        </div>

        <Divider />
        <p class="hint">
          <i class="pi pi-info-circle" /> Data fim — preencha apenas quando a receita encerrar.
        </p>

        <div class="grid-2">
          <div class="field">
            <label>Mês fim <span class="opcional">(opcional)</span></label>
            <Select
              v-model="form.mesFim"
              :options="[{ label: '— Em aberto', value: null }, ...meses]"
              option-label="label"
              option-value="value"
              fluid
            />
          </div>
          <div class="field">
            <label>Ano fim <span class="opcional">(opcional)</span></label>
            <InputNumber
              v-model="form.anoFim"
              :use-grouping="false"
              :min="2000"
              :placeholder="'—'"
              fluid
            />
          </div>
        </div>
      </template>

      <!-- PONTUAL: apenas mês/ano de ocorrência -->
      <template v-else>
        <div class="grid-2">
          <div class="field">
            <label>Mês</label>
            <Select
              v-model="form.mesInicio"
              :options="meses"
              option-label="label"
              option-value="value"
              placeholder="Mês"
              fluid
            />
          </div>
          <div class="field">
            <label>Ano</label>
            <InputNumber v-model="form.anoInicio" :use-grouping="false" :min="2000" fluid />
          </div>
        </div>
        <small class="hint">
          <i class="pi pi-info-circle" /> Receita pontual — ocorre somente neste mês.
        </small>
      </template>
    </form>

    <template #footer>
      <Button label="Cancelar" severity="secondary" text @click="$emit('update:visible', false)" />
      <Button label="Salvar" :loading="salvando" @click="salvar" />
    </template>
  </Dialog>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import InputNumber from 'primevue/inputnumber'
import Select from 'primevue/select'
import Button from 'primevue/button'
import Dialog from 'primevue/dialog'
import Divider from 'primevue/divider'
import type { ReceitaValorRequest, ReceitaValorResponse, TipoReceita } from '@/types'

const props = defineProps<{
  visible: boolean
  tipo: TipoReceita
  valorEditando?: ReceitaValorResponse | null
}>()

const emit = defineEmits<{
  'update:visible': [value: boolean]
  salvo: [data: ReceitaValorRequest]
}>()

const meses = [
  { label: 'Janeiro', value: 1 }, { label: 'Fevereiro', value: 2 },
  { label: 'Março', value: 3 }, { label: 'Abril', value: 4 },
  { label: 'Maio', value: 5 }, { label: 'Junho', value: 6 },
  { label: 'Julho', value: 7 }, { label: 'Agosto', value: 8 },
  { label: 'Setembro', value: 9 }, { label: 'Outubro', value: 10 },
  { label: 'Novembro', value: 11 }, { label: 'Dezembro', value: 12 }
]

const anoAtual = new Date().getFullYear()
const mesAtual = new Date().getMonth() + 1

const form = ref<ReceitaValorRequest>({
  valor: 0,
  mesInicio: mesAtual,
  anoInicio: anoAtual,
  mesFim: null,
  anoFim: null
})
const erros = ref({ valor: '' })
const salvando = ref(false)

watch(
  () => props.visible,
  (val) => {
    if (val) {
      form.value = props.valorEditando
        ? {
            valor: props.valorEditando.valor,
            mesInicio: props.valorEditando.mesInicio,
            anoInicio: props.valorEditando.anoInicio,
            mesFim: props.valorEditando.mesFim,
            anoFim: props.valorEditando.anoFim
          }
        : { valor: 0, mesInicio: mesAtual, anoInicio: anoAtual, mesFim: null, anoFim: null }
      erros.value = { valor: '' }
    }
  }
)

function validar(): boolean {
  erros.value = { valor: '' }
  if (!form.value.valor || form.value.valor <= 0) {
    erros.value.valor = 'Informe um valor válido'
    return false
  }
  return true
}

async function salvar() {
  if (!validar()) return
  salvando.value = true
  try {
    const dados: ReceitaValorRequest = { ...form.value }

    // Pontual: data fim = data início (mesmo mês)
    if (props.tipo === 'PONTUAL') {
      dados.mesFim = dados.mesInicio
      dados.anoFim = dados.anoInicio
    }

    emit('salvo', dados)
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
.hint { font-size: 0.8rem; color: #6c757d; display: flex; align-items: center; gap: 0.3rem; margin: -0.25rem 0; }
.opcional { font-weight: 400; color: #9ca3af; font-size: 0.8rem; }
</style>

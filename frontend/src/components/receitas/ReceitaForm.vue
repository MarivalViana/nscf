<template>
  <Dialog
    :visible="visible"
    :header="receita ? 'Editar Receita' : 'Nova Receita'"
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
          placeholder="Ex: Salário, Freelance..."
          :class="{ 'p-invalid': erros.descricao }"
          fluid
        />
        <small v-if="erros.descricao" class="erro">{{ erros.descricao }}</small>
      </div>

      <div class="field">
        <label for="tipo">Tipo</label>
        <Select
          id="tipo"
          v-model="form.tipo"
          :options="tiposReceita"
          option-label="label"
          option-value="value"
          placeholder="Selecione o tipo"
          :class="{ 'p-invalid': erros.tipo }"
          fluid
        />
        <small v-if="erros.tipo" class="erro">{{ erros.tipo }}</small>
      </div>

      <!-- Valor inicial: só aparece na criação -->
      <template v-if="!receita">
        <Divider />
        <p class="secao-label">Valor inicial</p>

        <div class="field">
          <label>Valor (R$)</label>
          <InputNumber
            v-model="valorInicial.valor"
            mode="currency"
            currency="BRL"
            locale="pt-BR"
            :min="0"
            :class="{ 'p-invalid': erros.valor }"
            fluid
          />
          <small v-if="erros.valor" class="erro">{{ erros.valor }}</small>
        </div>

        <!-- RECORRENTE: só mês/ano de início -->
        <template v-if="form.tipo === 'RECORRENTE'">
          <div class="grid-2">
            <div class="field">
              <label>Mês início</label>
              <Select
                v-model="valorInicial.mesInicio"
                :options="meses"
                option-label="label"
                option-value="value"
                fluid
              />
            </div>
            <div class="field">
              <label>Ano início</label>
              <InputNumber
                v-model="valorInicial.anoInicio"
                :use-grouping="false"
                :min="2000"
                fluid
              />
            </div>
          </div>
          <small class="dica">
            <i class="pi pi-info-circle" /> Receita em aberto — a data fim é preenchida quando encerrar.
          </small>
        </template>

        <!-- PONTUAL: mês e ano de ocorrência (único) -->
        <template v-else-if="form.tipo === 'PONTUAL'">
          <div class="grid-2">
            <div class="field">
              <label>Mês</label>
              <Select
                v-model="valorInicial.mesInicio"
                :options="meses"
                option-label="label"
                option-value="value"
                fluid
              />
            </div>
            <div class="field">
              <label>Ano</label>
              <InputNumber
                v-model="valorInicial.anoInicio"
                :use-grouping="false"
                :min="2000"
                fluid
              />
            </div>
          </div>
          <small class="dica">
            <i class="pi pi-info-circle" /> Receita pontual — ocorre somente neste mês.
          </small>
        </template>
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
import Dialog from 'primevue/dialog'
import InputText from 'primevue/inputtext'
import InputNumber from 'primevue/inputnumber'
import Select from 'primevue/select'
import Button from 'primevue/button'
import Divider from 'primevue/divider'
import type { ReceitaResponse, ReceitaRequest, ReceitaValorRequest, TipoReceita } from '@/types'

const props = defineProps<{
  visible: boolean
  receita?: ReceitaResponse | null
}>()

const emit = defineEmits<{
  'update:visible': [value: boolean]
  salvo: [receita: ReceitaRequest, valor: ReceitaValorRequest | null]
}>()

const tiposReceita = [
  { label: 'Recorrente (todo mês)', value: 'RECORRENTE' },
  { label: 'Pontual (mês específico)', value: 'PONTUAL' }
]

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

const form = ref<ReceitaRequest>({ descricao: '', tipo: 'RECORRENTE' })
const valorInicial = ref<ReceitaValorRequest>({
  valor: 0, mesInicio: mesAtual, anoInicio: anoAtual, mesFim: null, anoFim: null
})
const erros = ref({ descricao: '', tipo: '', valor: '' })
const salvando = ref(false)

watch(
  () => props.visible,
  (val) => {
    if (val) {
      form.value = {
        descricao: props.receita?.descricao ?? '',
        tipo: (props.receita?.tipo ?? 'RECORRENTE') as TipoReceita
      }
      valorInicial.value = {
        valor: 0, mesInicio: mesAtual, anoInicio: anoAtual, mesFim: null, anoFim: null
      }
      erros.value = { descricao: '', tipo: '', valor: '' }
    }
  }
)

function validar(): boolean {
  erros.value = { descricao: '', tipo: '', valor: '' }
  let valido = true

  if (!form.value.descricao.trim()) {
    erros.value.descricao = 'Descrição é obrigatória'
    valido = false
  }
  if (!form.value.tipo) {
    erros.value.tipo = 'Tipo é obrigatório'
    valido = false
  }
  if (!props.receita && (!valorInicial.value.valor || valorInicial.value.valor <= 0)) {
    erros.value.valor = 'Informe o valor inicial'
    valido = false
  }

  return valido
}

async function salvar() {
  if (!validar()) return
  salvando.value = true
  try {
    let valorParaEnviar: ReceitaValorRequest | null = null

    if (!props.receita) {
      valorParaEnviar = { ...valorInicial.value }

      // Pontual: fecha no mesmo mês de início
      if (form.value.tipo === 'PONTUAL') {
        valorParaEnviar.mesFim = valorParaEnviar.mesInicio
        valorParaEnviar.anoFim = valorParaEnviar.anoInicio
      } else {
        // Recorrente: em aberto
        valorParaEnviar.mesFim = null
        valorParaEnviar.anoFim = null
      }
    }

    emit('salvo', { ...form.value }, valorParaEnviar)
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
.dica { color: #6c757d; font-size: 0.8rem; display: flex; align-items: center; gap: 0.3rem; margin-top: -0.25rem; }
.secao-label { font-size: 0.8rem; font-weight: 600; color: #6c757d; text-transform: uppercase; letter-spacing: 0.05em; margin: -0.25rem 0; }
</style>

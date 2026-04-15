<template>
  <Dialog
    :visible="visible"
    :header="conta ? 'Editar Conta' : 'Nova Conta'"
    :style="{ width: '440px' }"
    modal
    @update:visible="$emit('update:visible', $event)"
  >
    <form @submit.prevent="salvar" class="form">
      <div class="field">
        <label for="nome">Nome</label>
        <InputText
          id="nome"
          v-model="form.nome"
          placeholder="Ex: Cartão Nubank, Conta Corrente..."
          :class="{ 'p-invalid': erros.nome }"
          fluid
        />
        <small v-if="erros.nome" class="erro">{{ erros.nome }}</small>
      </div>

      <div class="field">
        <label>Ícone</label>
        <Select
          v-model="form.icone"
          :options="iconesDisponiveis"
          option-label="label"
          option-value="value"
          placeholder="Selecione um ícone"
          :class="{ 'p-invalid': erros.icone }"
          fluid
        >
          <template #option="{ option }">
            <div class="icone-opcao">
              <i :class="option.value" />
              <span>{{ option.label }}</span>
            </div>
          </template>
          <template #value="{ value }">
            <div v-if="value" class="icone-opcao">
              <i :class="value" />
              <span>{{ iconesDisponiveis.find(i => i.value === value)?.label }}</span>
            </div>
          </template>
        </Select>
        <small v-if="erros.icone" class="erro">{{ erros.icone }}</small>
      </div>

      <div v-if="form.nome || form.icone" class="preview">
        <i :class="form.icone || 'pi pi-credit-card'" />
        <span>{{ form.nome || 'Preview' }}</span>
      </div>
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
import Select from 'primevue/select'
import Button from 'primevue/button'
import type { ContaResponse, ContaRequest } from '@/types'

const props = defineProps<{
  visible: boolean
  conta?: ContaResponse | null
}>()

const emit = defineEmits<{
  'update:visible': [value: boolean]
  salvo: [data: ContaRequest]
}>()

const iconesDisponiveis = [
  { label: 'Cartão de Crédito', value: 'pi pi-credit-card' },
  { label: 'Banco / Agência', value: 'pi pi-building' },
  { label: 'Carteira / Dinheiro', value: 'pi pi-wallet' },
  { label: 'Dólar / Financeiro', value: 'pi pi-dollar' },
  { label: 'Casa / Financiamento', value: 'pi pi-home' },
  { label: 'Cofre / Poupança', value: 'pi pi-save' },
  { label: 'Gráfico / Investimento', value: 'pi pi-chart-line' },
  { label: 'Troféu / Conquista', value: 'pi pi-trophy' },
  { label: 'Celular / Digital', value: 'pi pi-mobile' },
  { label: 'Link / Transferência', value: 'pi pi-link' },
  { label: 'Seta / Empréstimo', value: 'pi pi-arrow-right-arrow-left' },
  { label: 'Estrela', value: 'pi pi-star' },
]

const form = ref<ContaRequest>({ nome: '', icone: 'pi pi-credit-card' })
const erros = ref({ nome: '', icone: '' })
const salvando = ref(false)

watch(
  () => props.visible,
  (val) => {
    if (val) {
      form.value = props.conta
        ? { nome: props.conta.nome, icone: props.conta.icone }
        : { nome: '', icone: 'pi pi-credit-card' }
      erros.value = { nome: '', icone: '' }
    }
  }
)

function validar(): boolean {
  erros.value = { nome: '', icone: '' }
  let valido = true
  if (!form.value.nome.trim()) {
    erros.value.nome = 'Nome é obrigatório'
    valido = false
  }
  if (!form.value.icone) {
    erros.value.icone = 'Selecione um ícone'
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
.field { display: flex; flex-direction: column; gap: 0.4rem; }
.field label { font-weight: 500; font-size: 0.9rem; color: #374151; }
.erro { color: #ef4444; font-size: 0.8rem; }
.icone-opcao { display: flex; align-items: center; gap: 0.6rem; }
.icone-opcao i { font-size: 1rem; color: #6c757d; width: 20px; text-align: center; }
.preview {
  display: flex; align-items: center; gap: 0.6rem;
  padding: 0.75rem 1rem; background: #f8f9fa; border-radius: 8px;
  font-size: 0.9rem; color: #374151; font-weight: 500;
}
.preview i { font-size: 1.1rem; color: #6c757d; }
</style>

<template>
  <Dialog
    :visible="visible"
    :header="receita ? 'Editar Receita' : 'Nova Receita'"
    :style="{ width: '420px' }"
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
import type { ReceitaResponse, ReceitaRequest, TipoReceita } from '@/types'

const props = defineProps<{
  visible: boolean
  receita?: ReceitaResponse | null
}>()

const emit = defineEmits<{
  'update:visible': [value: boolean]
  salvo: [data: ReceitaRequest]
}>()

const tiposReceita = [
  { label: 'Recorrente (todo mês)', value: 'RECORRENTE' },
  { label: 'Pontual (mês específico)', value: 'PONTUAL' }
]

const form = ref<ReceitaRequest>({ descricao: '', tipo: 'RECORRENTE' })
const erros = ref({ descricao: '', tipo: '' })
const salvando = ref(false)

watch(
  () => props.visible,
  (val) => {
    if (val) {
      form.value = {
        descricao: props.receita?.descricao ?? '',
        tipo: (props.receita?.tipo ?? 'RECORRENTE') as TipoReceita
      }
      erros.value = { descricao: '', tipo: '' }
    }
  }
)

function validar(): boolean {
  erros.value = { descricao: '', tipo: '' }
  let valido = true
  if (!form.value.descricao.trim()) {
    erros.value.descricao = 'Descrição é obrigatória'
    valido = false
  }
  if (!form.value.tipo) {
    erros.value.tipo = 'Tipo é obrigatório'
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
</style>

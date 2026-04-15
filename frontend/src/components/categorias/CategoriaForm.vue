<template>
  <Dialog
    :visible="visible"
    :header="categoria ? 'Editar Categoria' : 'Nova Categoria'"
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
          placeholder="Ex: Alimentação, Saúde..."
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

      <!-- Preview -->
      <div v-if="form.nome || form.icone" class="preview">
        <i :class="form.icone || 'pi pi-tag'" />
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
import type { CategoriaResponse, CategoriaRequest } from '@/types'

const props = defineProps<{
  visible: boolean
  categoria?: CategoriaResponse | null
}>()

const emit = defineEmits<{
  'update:visible': [value: boolean]
  salvo: [data: CategoriaRequest]
}>()

const iconesDisponiveis = [
  { label: 'Abastecimento', value: 'pi pi-car' },
  { label: 'Alimentação', value: 'pi pi-shopping-cart' },
  { label: 'Calendário', value: 'pi pi-calendar' },
  { label: 'Casa / Moradia', value: 'pi pi-home' },
  { label: 'Configurações', value: 'pi pi-cog' },
  { label: 'Educação', value: 'pi pi-book' },
  { label: 'Estrela / Lazer', value: 'pi pi-star' },
  { label: 'Gráfico / Investimento', value: 'pi pi-chart-line' },
  { label: 'Manutenção', value: 'pi pi-wrench' },
  { label: 'Outros', value: 'pi pi-ellipsis-h' },
  { label: 'Saúde', value: 'pi pi-heart' },
  { label: 'Tag / Vestuário', value: 'pi pi-tag' },
  { label: 'Transporte', value: 'pi pi-send' },
  { label: 'Troféu / Conquista', value: 'pi pi-trophy' },
  { label: 'Usuário', value: 'pi pi-user' },
  { label: 'Viagem', value: 'pi pi-map-marker' },
  { label: 'Cartão / Finanças', value: 'pi pi-credit-card' },
  { label: 'Dinheiro', value: 'pi pi-wallet' },
]

const form = ref<CategoriaRequest>({ nome: '', icone: 'pi pi-tag' })
const erros = ref({ nome: '', icone: '' })
const salvando = ref(false)

watch(
  () => props.visible,
  (val) => {
    if (val) {
      form.value = props.categoria
        ? { nome: props.categoria.nome, icone: props.categoria.icone }
        : { nome: '', icone: 'pi pi-tag' }
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

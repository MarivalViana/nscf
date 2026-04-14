<template>
  <div class="login-page">
    <div class="login-card">
      <div class="login-header">
        <h1>SCF</h1>
        <p>Sistema de Controle Financeiro</p>
      </div>

      <form @submit.prevent="handleLogin" class="login-form">
        <div class="field">
          <label for="email">Email</label>
          <InputText
            id="email"
            v-model="form.email"
            type="email"
            placeholder="seu@email.com"
            :class="{ 'p-invalid': erros.email }"
            fluid
          />
          <small v-if="erros.email" class="erro">{{ erros.email }}</small>
        </div>

        <div class="field">
          <label for="senha">Senha</label>
          <Password
            id="senha"
            v-model="form.senha"
            placeholder="Sua senha"
            :feedback="false"
            :class="{ 'p-invalid': erros.senha }"
            fluid
          />
          <small v-if="erros.senha" class="erro">{{ erros.senha }}</small>
        </div>

        <Message v-if="erroGeral" severity="error" :closable="false">
          {{ erroGeral }}
        </Message>

        <Button
          type="submit"
          label="Entrar"
          :loading="carregando"
          fluid
        />
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import InputText from 'primevue/inputtext'
import Password from 'primevue/password'
import Button from 'primevue/button'
import Message from 'primevue/message'

const router = useRouter()
const authStore = useAuthStore()

const form = ref({ email: '', senha: '' })
const erros = ref({ email: '', senha: '' })
const erroGeral = ref('')
const carregando = ref(false)

function validar(): boolean {
  erros.value = { email: '', senha: '' }
  let valido = true

  if (!form.value.email) {
    erros.value.email = 'Email é obrigatório'
    valido = false
  }
  if (!form.value.senha) {
    erros.value.senha = 'Senha é obrigatória'
    valido = false
  }

  return valido
}

async function handleLogin() {
  if (!validar()) return

  carregando.value = true
  erroGeral.value = ''

  try {
    await authStore.login({ email: form.value.email, senha: form.value.senha })
    router.push('/dashboard')
  } catch {
    erroGeral.value = 'Email ou senha inválidos'
  } finally {
    carregando.value = false
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #1a1a2e 0%, #16213e 100%);
}

.login-card {
  background: #fff;
  border-radius: 12px;
  padding: 2.5rem;
  width: 100%;
  max-width: 400px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
}

.login-header {
  text-align: center;
  margin-bottom: 2rem;
}

.login-header h1 {
  font-size: 2.5rem;
  font-weight: 700;
  color: #1a1a2e;
  margin-bottom: 0.25rem;
}

.login-header p {
  color: #6c757d;
  font-size: 0.9rem;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 1.25rem;
}

.field {
  display: flex;
  flex-direction: column;
  gap: 0.4rem;
}

.field label {
  font-weight: 500;
  font-size: 0.9rem;
  color: #374151;
}

.erro {
  color: #ef4444;
  font-size: 0.8rem;
}
</style>

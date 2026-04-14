<template>
  <div class="dashboard">
    <header class="dashboard-header">
      <div class="header-left">
        <h1>SCF</h1>
        <span>Sistema de Controle Financeiro</span>
      </div>
      <div class="header-right">
        <span class="usuario-nome">{{ authStore.usuario?.nome }}</span>
        <Button
          label="Sair"
          icon="pi pi-sign-out"
          severity="secondary"
          text
          @click="handleLogout"
        />
      </div>
    </header>

    <main class="dashboard-content">
      <div class="boas-vindas">
        <h2>Bem-vindo, {{ authStore.usuario?.nome }}! 👋</h2>
        <p>O dashboard está em construção. Em breve aqui estarão seus dados financeiros.</p>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import Button from 'primevue/button'

const router = useRouter()
const authStore = useAuthStore()

function handleLogout() {
  authStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.dashboard {
  min-height: 100vh;
  background-color: #f8f9fa;
}

.dashboard-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 1rem 2rem;
  background: #1a1a2e;
  color: #fff;
}

.header-left h1 {
  font-size: 1.5rem;
  font-weight: 700;
  margin-bottom: 0;
}

.header-left span {
  font-size: 0.8rem;
  opacity: 0.7;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.usuario-nome {
  font-size: 0.9rem;
  opacity: 0.9;
}

.dashboard-content {
  padding: 2rem;
}

.boas-vindas {
  background: #fff;
  border-radius: 8px;
  padding: 2rem;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
}

.boas-vindas h2 {
  font-size: 1.5rem;
  margin-bottom: 0.5rem;
  color: #1a1a2e;
}

.boas-vindas p {
  color: #6c757d;
}
</style>

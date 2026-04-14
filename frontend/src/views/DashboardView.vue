<template>
  <div class="page">
    <div class="page-header">
      <h2>Dashboard</h2>
      <p>Visão geral do seu controle financeiro</p>
    </div>

    <div class="boas-vindas">
      <h3>Bem-vindo, {{ authStore.usuario?.nome }}! 👋</h3>
      <p>O dashboard está em construção. Em breve aqui estarão seus resumos e gráficos.</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useAuthStore } from '@/stores/auth'
const authStore = useAuthStore()
</script>

<style scoped>
.page { padding: 2rem; }
.page-header { margin-bottom: 1.5rem; }
.page-header h2 { font-size: 1.5rem; font-weight: 700; color: #1a1a2e; margin-bottom: 0.25rem; }
.page-header p { color: #6c757d; font-size: 0.9rem; }

.boas-vindas {
  background: #fff;
  border-radius: 8px;
  padding: 2rem;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
}
.boas-vindas h3 { font-size: 1.25rem; margin-bottom: 0.5rem; color: #1a1a2e; }
.boas-vindas p { color: #6c757d; }
</style>

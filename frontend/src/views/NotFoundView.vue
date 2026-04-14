<template>
  <main class="not-found">
    <h1>404</h1>
    <p>Página não encontrada</p>
    <RouterLink to="/">Voltar ao início</RouterLink>
  </main>
</template>

<script setup lang="ts">
import { RouterLink } from 'vue-router'
</script>

<style scoped>
.not-found {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  gap: 1rem;
  text-align: center;
}
</style>

<template>
  <main class="home">
    <div class="hero">
      <h1>SCF</h1>
      <p>Sistema de Controle Financeiro</p>
    </div>
  </main>
</template>

<script setup lang="ts">
</script>

<style scoped>
.home {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f8f9fa;
}

.hero {
  text-align: center;
}

.hero h1 {
  font-size: 3rem;
  font-weight: 700;
  color: #1a1a2e;
  margin-bottom: 0.5rem;
}

.hero p {
  font-size: 1.25rem;
  color: #6c757d;
}
</style>

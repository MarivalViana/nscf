<template>
  <div class="app-layout">
    <!-- Sidebar -->
    <aside class="sidebar" :class="{ collapsed }">
      <div class="sidebar-header">
        <span class="logo">SCF</span>
        <span v-if="!collapsed" class="logo-sub">Controle Financeiro</span>
        <Button
          :icon="collapsed ? 'pi pi-angle-right' : 'pi pi-angle-left'"
          text
          rounded
          class="toggle-btn"
          @click="collapsed = !collapsed"
        />
      </div>

      <nav class="sidebar-nav">
        <RouterLink
          v-for="item in menuItems"
          :key="item.to"
          :to="item.to"
          class="nav-item"
          active-class="nav-item--active"
        >
          <i :class="item.icon" />
          <span v-if="!collapsed" class="nav-label">{{ item.label }}</span>
        </RouterLink>
      </nav>

      <div class="sidebar-footer">
        <div class="usuario-info" v-if="!collapsed">
          <i class="pi pi-user" />
          <span>{{ authStore.usuario?.nome }}</span>
        </div>
        <Button
          icon="pi pi-sign-out"
          :label="collapsed ? undefined : 'Sair'"
          text
          severity="secondary"
          class="logout-btn"
          @click="handleLogout"
          v-tooltip.right="collapsed ? 'Sair' : undefined"
        />
      </div>
    </aside>

    <!-- Conteúdo principal -->
    <main class="main-content">
      <RouterView />
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter, RouterLink, RouterView } from 'vue-router'
import Button from 'primevue/button'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const authStore = useAuthStore()
const collapsed = ref(false)

const menuItems = [
  { label: 'Dashboard', icon: 'pi pi-home', to: '/dashboard' },
  { label: 'Receitas', icon: 'pi pi-arrow-circle-up', to: '/receitas' }
]

function handleLogout() {
  authStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.app-layout {
  display: flex;
  min-height: 100vh;
}

/* ── Sidebar ── */
.sidebar {
  width: 240px;
  min-width: 240px;
  background: #1a1a2e;
  color: #fff;
  display: flex;
  flex-direction: column;
  transition: width 0.25s ease, min-width 0.25s ease;
}

.sidebar.collapsed {
  width: 64px;
  min-width: 64px;
}

.sidebar-header {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 1.25rem 1rem;
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
  min-height: 64px;
}

.logo {
  font-size: 1.4rem;
  font-weight: 700;
  color: #fff;
  white-space: nowrap;
}

.logo-sub {
  font-size: 0.7rem;
  color: rgba(255, 255, 255, 0.5);
  white-space: nowrap;
  flex: 1;
}

.toggle-btn {
  margin-left: auto;
  color: rgba(255, 255, 255, 0.5) !important;
  flex-shrink: 0;
}

/* ── Nav ── */
.sidebar-nav {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
  padding: 1rem 0.5rem;
  overflow: hidden;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding: 0.75rem;
  border-radius: 8px;
  color: rgba(255, 255, 255, 0.65);
  text-decoration: none;
  font-size: 0.9rem;
  transition: background 0.15s, color 0.15s;
  white-space: nowrap;
}

.nav-item:hover {
  background: rgba(255, 255, 255, 0.08);
  color: #fff;
}

.nav-item--active {
  background: rgba(255, 255, 255, 0.12);
  color: #fff;
  font-weight: 600;
}

.nav-item i {
  font-size: 1rem;
  flex-shrink: 0;
  width: 20px;
  text-align: center;
}

.nav-label {
  overflow: hidden;
  text-overflow: ellipsis;
}

/* ── Footer ── */
.sidebar-footer {
  padding: 1rem 0.5rem;
  border-top: 1px solid rgba(255, 255, 255, 0.08);
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.usuario-info {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 0.75rem;
  font-size: 0.85rem;
  color: rgba(255, 255, 255, 0.6);
  white-space: nowrap;
  overflow: hidden;
}

.logout-btn {
  color: rgba(255, 255, 255, 0.6) !important;
  justify-content: flex-start;
}

/* ── Conteúdo ── */
.main-content {
  flex: 1;
  background: #f8f9fa;
  overflow-y: auto;
}
</style>

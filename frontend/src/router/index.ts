import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import AppLayout from '@/components/layout/AppLayout.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/LoginView.vue'),
      meta: { requiresGuest: true }
    },
    {
      path: '/',
      component: AppLayout,
      meta: { requiresAuth: true },
      redirect: '/dashboard',
      children: [
        {
          path: 'dashboard',
          name: 'dashboard',
          component: () => import('@/views/DashboardView.vue')
        },
        {
          path: 'receitas',
          name: 'receitas',
          component: () => import('@/views/receitas/ReceitasView.vue')
        },
        {
          path: 'receitas/:id',
          name: 'receita-detalhe',
          component: () => import('@/views/receitas/ReceitaDetalheView.vue')
        },
        {
          path: 'despesas',
          name: 'despesas',
          component: () => import('@/views/despesas/DespesasView.vue')
        },
        {
          path: 'categorias',
          name: 'categorias',
          component: () => import('@/views/categorias/CategoriasView.vue')
        },
        {
          path: 'contas',
          name: 'contas',
          component: () => import('@/views/contas/ContasView.vue')
        }
      ]
    },
    {
      path: '/:pathMatch(.*)*',
      name: 'not-found',
      component: () => import('@/views/NotFoundView.vue')
    }
  ]
})

router.beforeEach((to) => {
  const authStore = useAuthStore()

  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    return { name: 'login' }
  }

  if (to.meta.requiresGuest && authStore.isAuthenticated) {
    return { name: 'dashboard' }
  }
})

export default router

import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '@views/auth/LoginView.vue'
import MenuView from '@components/menu/MenuView.vue'
import { authGuard } from './guard'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: LoginView,
      meta: { requiresGuest: true }
    },
    {
      path: '/',
      component: MenuView,
      redirect: '/dashboard',
      meta: { requiresAuth: true },
      children: [
        {
          path: 'dashboard',
          name: 'dashboard',
          component: () => import('../views/DashboardView.vue'),
        },
        {
          path: 'users',
          name: 'users',
          component: () => import('../views/UsersView.vue'),
        },

        {
          path: 'companies',
          name: 'companies',
          component: () => import('../views/CompaniesView.vue'),
        },

        {
          path: 'operations',
          name: 'operations',
          component: () => import('../views/OperationsView.vue'),
        }

      ]
    }
  ],
})

router.beforeEach(authGuard)

export default router

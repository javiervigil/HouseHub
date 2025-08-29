import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/service/auth'


import NotFound from '@/views/NotFound.vue'

import AppLayout from '@/layout/AppLayout.vue'

const routes = [
  {
    path: '/',
    component: AppLayout,
    meta: { requiresAuth: true },
    children: [
      {
        path: '/',
        name: 'homeadmin',
        component: () => import('@/views/HomeAdmin.vue')
      },
      {
        path: '/',
        name: 'homeuser',
        component: () => import('@/views/HomeUser.vue')
      },
      {
        path: '/',
        name: 'homesuperuser',
        component: () => import('@/views/HomeSuperUser.vue')
      },
      {
        path: '/finanzasa',
        name: 'finanzasa',
        component: () => import('@/views/finanzasa/Finanzas.vue')
      },
      {
        path: '/finanzasu',
        name: 'finanzasu',
        component: () => import('@/views/finanzasuser/Finanzas.vue')
      },
      {
        path: '/administracion',
        name: 'administracion',
        component: () => import('@/views/administracion/Administracion.vue')
      },
      {
        path: '/configuracion',
        name: 'configuracion',
        component: () => import('@/views/configuracion/Configuracion.vue')
      },
      {
        path: '/about',
        name: 'about',
        component: () => import('@/views/About.vue')
      }
    ]
  },
  {
    path: '/login',
    name: 'login',
    component: () => import('@/views/login/Login.vue')
  },
  {//catch 404
    path: '/:catchAll(.*)',
    name: 'notFound',
    meta: { requiresAuth: true },
    component: NotFound
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore();
  const requiresAuth = to.meta.requiresAuth;

  console.log('requiresAuth ' + requiresAuth);
  console.log('authStore ' + authStore);
  console.log('authStore.isLoggedIn ' + authStore.isLoggedIn);
  
  if (requiresAuth && authStore && !authStore.isLoggedIn) {
    next({ name: 'login' });
  } else {
    next();
  }
});

export default router

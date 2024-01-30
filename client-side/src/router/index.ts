import { createRouter, createWebHistory } from 'vue-router'
import * as views from '@/components/views'

const routes = [
  { path: '/', component: views.HomeView }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
})

export default router

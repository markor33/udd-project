import { createRouter, createWebHistory } from 'vue-router'
import * as views from '@/components/views'
import { IndexContract, IndexLaw, SearchContract, SearchLaw, Statistics, SearchGlobal } from "@/components";

const routes = [
  { path: '/',
    component: views.HomeView,
    children: [
      { path: '/search-global', component: SearchGlobal },
      { path: '/index-contract', component: IndexContract },
      { path: '/index-law', component: IndexLaw },
      { path: '/search-law', component: SearchLaw },
      { path: '/search-contract', component: SearchContract },
      { path: '/statistics', component: Statistics }
    ]
  },
  { path: '/login', component: views.LoginView }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
})

export default router

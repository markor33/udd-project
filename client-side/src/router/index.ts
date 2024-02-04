import { createRouter, createWebHistory } from 'vue-router'
import * as views from '@/components/views'
import {IndexContract, IndexLaw, SearchLaw} from "@/components";

const routes = [
  { path: '/',
    component: views.HomeView,
    children: [
      { path: '/index-contract', component: IndexContract},
      { path: '/index-law', component: IndexLaw },
      { path: '/search-law', component: SearchLaw }
    ]
  },
  { path: '/login', component: views.LoginView }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
})

export default router

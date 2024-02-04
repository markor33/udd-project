<script setup lang="ts">
import {useRouter} from "vue-router";
import {useAuthStore} from "@/store/auth";
import {storeToRefs} from "pinia";

const router = useRouter()
const authStore = useAuthStore()

const { role } = storeToRefs(authStore)

const logout = () => {
  authStore.logout()
  router.push('/login')
}

const goTo = (route: string) => {
  router.push(route)
}
</script>

<template>
  <v-layout class="rounded rounded-md">
    <v-app-bar title="Repository search" color="primary">
      <div></div>
      <v-spacer></v-spacer>
      <v-btn @click="logout" prepend-icon="mdi-logout">Logout</v-btn>
    </v-app-bar>

    <v-navigation-drawer>
      <v-list class="mt-10">
        <v-list-group>
          <template #activator="{ props }">
            <v-list-item v-bind="props" title="Contract" prepend-icon="mdi-briefcase-outline"></v-list-item>
          </template>
          <v-list-item title="Search" @click="goTo('search-contract')" prepend-icon="mdi-magnify"></v-list-item>
          <v-list-item v-if="role === 'CONTRACT'" title="Index" @click="goTo('index-contract')" prepend-icon="mdi-upload-outline"></v-list-item>
        </v-list-group>
        <v-list-group>
          <template #activator="{ props }">
            <v-list-item v-bind="props" title="Law" prepend-icon="mdi-scale-balance"></v-list-item>
          </template>
          <v-list-item title="Search" @click="goTo('search-law')" prepend-icon="mdi-magnify"></v-list-item>
          <v-list-item v-if="role === 'LAW'" title="Index" @click="goTo('index-law')" prepend-icon="mdi-upload-outline"></v-list-item>
        </v-list-group>
      </v-list>
    </v-navigation-drawer>

    <v-main class="d-flex justify-center" style="min-height: 300px;">
      <router-view></router-view>
    </v-main>
  </v-layout>
</template>

<style scoped>

</style>

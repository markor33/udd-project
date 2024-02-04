<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/store/auth'

const router = useRouter()
const authStore = useAuthStore()

const credentials = ref({
  username: '',
  password: ''
})

const showError = ref(false)

const login = async () => {
  const result = await authStore.login(credentials.value.username, credentials.value.password)
  if (result)
    await router.push('/')
  else
    showError.value = !result
}

</script>

<template>
  <div class="d-flex justify-center mt-16">
    <v-card width="600" class="pa-3">
      <v-card-title>Login</v-card-title>
      <v-form @submit.prevent="login()">
        <v-text-field v-model="credentials.username" label="Username"></v-text-field>
        <v-text-field v-model="credentials.password" type="password" label="Password"></v-text-field>
        <v-btn color="primary" type="submit">Login</v-btn>
      </v-form>
    </v-card>
  </div>
  <v-snackbar v-model="showError" color="red">
    Wrong username and/or password
  </v-snackbar>
</template>


<style scoped>

</style>

import axios from 'axios'
import {useAuthStore} from "@/store/auth";

function tokenInterceptor() {
  axios.interceptors.request.use(async (config) => {
    const authStore = useAuthStore()
    const authToken = authStore.accessToken
    config.headers.Authorization = `Bearer ${authToken}`

    return config
  })
}

export default tokenInterceptor

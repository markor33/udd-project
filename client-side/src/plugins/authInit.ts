import {useAuthStore} from "@/store/auth";

export default function authInit() {
  const authStore = useAuthStore()
  authStore.init()
}

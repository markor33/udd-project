import { defineStore } from 'pinia'
import axios from 'axios';
import { LoginResponse, TokenContent } from "@/models/auth";
import { jwtDecode } from "jwt-decode";

export const useAuthStore = defineStore('auth', {
  state: () => ({
    decodedToken: null as null | TokenContent
  }),
  getters: {
    role: (state) => state.decodedToken?.role,
    isLogged: (state) => !!state.decodedToken,
    accessToken: (state) => localStorage.getItem('token')
  },
  actions: {
    async login(username: string, password: string) {
      try {
        const response = await axios.post<LoginResponse>('/api/auth/login', {
          username,
          password
        });

        const token = response.data.accessToken
        localStorage.setItem('token', token)
        this.decodedToken = jwtDecode(token) as TokenContent

        return true;
      }
      catch(error) { return false; }
    },
    logout() {
      localStorage.removeItem('token')
      this.decodedToken = null
    }
  }
})

import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

const TOKEN_KEY = 'nutritionist_admin_token'

export const useAuthStore = defineStore('auth', () => {
  const token = ref<string | null>(localStorage.getItem(TOKEN_KEY))
  const account = ref<string | null>(localStorage.getItem('nutritionist_admin_account'))

  const isLoggedIn = computed(() => Boolean(token.value))

  function setSession(nextToken: string, nextAccount: string) {
    token.value = nextToken
    account.value = nextAccount
    localStorage.setItem(TOKEN_KEY, nextToken)
    localStorage.setItem('nutritionist_admin_account', nextAccount)
  }

  function clearSession() {
    token.value = null
    account.value = null
    localStorage.removeItem(TOKEN_KEY)
    localStorage.removeItem('nutritionist_admin_account')
  }

  return { token, account, isLoggedIn, setSession, clearSession }
})

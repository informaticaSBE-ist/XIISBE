import { defineStore } from 'pinia'
import type SBEError from '@/models/SBEError'

// Global UI state: error messages to show and the loading overlay.
export const useAppearanceStore = defineStore('appearance', {
  state: () => ({
    errorMessagesStack: [] as string[],
    loading: false
  }),
  getters: {
    isStackEmpty(): boolean {
      return this.errorMessagesStack.length === 0
    },
    currentErrorMessage(): string | null {
      return this.errorMessagesStack[this.errorMessagesStack.length - 1] ?? null
    }
  },
  actions: {
    pushError(err: SBEError) {
      this.errorMessagesStack.push(err.message)
    },
    popError(): string | null {
      return this.errorMessagesStack.pop() ?? null
    },
    clearErrors() {
      this.errorMessagesStack = []
    }
  }
})

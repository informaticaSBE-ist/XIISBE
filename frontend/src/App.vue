<template>
  <v-layout id="app">
    <TopBar />
    <v-main class="pa-10 d-flex flex-column flex-grow-1 flex-shrink-1 text-center">
      <v-card class="elevation-4 pa-8" width="100%">
        <ErrorMessage />
        <LoadingOverlay />

        <RouterView />
      </v-card>
    </v-main>
  </v-layout>
</template>

<script setup lang="ts">
import TopBar from '@/components/TopBar.vue'
import ErrorMessage from '@/components/ErrorMessage.vue'
import LoadingOverlay from '@/components/LoadingOverlay.vue'

import { RouterView } from 'vue-router'
import { onMounted } from 'vue'
import { useAppearanceStore } from '@/stores/appearance'

const appearanceStore = useAppearanceStore()

onMounted(() => appearanceStore.clearErrors())
</script>

<style scoped>
#app {
  display: flex;
  flex-direction: column;
}
</style>

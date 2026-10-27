<template>
  <v-app-bar>
    <v-app-bar-title>XII SBE</v-app-bar-title>
    <v-spacer></v-spacer>
    <v-btn v-for="item in navbarItems" :key="item.name" :to="item.path">
      {{ item.name }}
      <v-icon class="ml-1" :icon="item.icon"></v-icon>
    </v-btn>
  </v-app-bar>
</template>

<script setup lang="ts">
defineProps<{ navbarItems: { name: string; path: string; icon: string }[] }>()
</script>

<template>
  <NavBar :navbarItems="navbarItems" />
</template>

<script setup lang="ts">
import NavBar from '@/components/NavBar.vue'

const navbarItems = [
  { name: 'Início', path: '/', icon: 'mdi-home' },
  { name: 'Pessoas', path: '/people', icon: 'mdi-account-group' }
]
</script>

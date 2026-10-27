<template>
  <v-row align="center">
    <v-col>
      <h2 class="text-left ml-1">Listagem de Pessoas</h2>
    </v-col>
    <v-col cols="auto">
      <CreatePersonDialog @person-created="getPeople" />
    </v-col>
  </v-row>

  <v-text-field
    v-model="search"
    label="Search"
    prepend-inner-icon="mdi-magnify"
    variant="outlined"
    hide-details
    single-line
  ></v-text-field>

  <v-data-table
    :headers="headers"
    :items="people"
    :search="search"
    :loading="loading"
    item-value="id"
    class="text-left"
    no-data-text="Sem pessoas a apresentar."
  >
    <template v-slot:[`item.actions`]="{ item }">
      <v-icon @click="deletePerson(item)">mdi-delete</v-icon>
    </template>
  </v-data-table>
</template>

<script setup lang="ts">
import type PersonDto from '@/models/PersonDto'
import RemoteService from '@/services/RemoteService'
import CreatePersonDialog from './CreatePersonDialog.vue'
import { ref } from 'vue'

const search = ref('')
const loading = ref(true)
const people = ref<PersonDto[]>([])

const headers = [
  { title: 'ID', key: 'id', sortable: true },
  { title: 'Nome', key: 'name', sortable: true },
  { title: 'Email', key: 'email', sortable: true },
  { title: 'Ações', key: 'actions', sortable: false }
]

getPeople()
async function getPeople() {
  loading.value = true
  try {
    people.value = await RemoteService.getPeople()
  } catch {
    // the error is already shown by RemoteService
  }
  loading.value = false
}

async function deletePerson(person: PersonDto) {
  if (person.id === undefined) return
  try {
    await RemoteService.deletePerson(person.id)
    await getPeople()
  } catch {
    // the error is already shown by RemoteService
  }
}
</script>

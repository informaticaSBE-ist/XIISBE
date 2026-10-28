<template>
  <div class="pa-4 text-center">
    <v-dialog v-model="dialog" max-width="400">
      <template v-slot:activator="{ props: activatorProps }">
        <v-btn
          class="text-none font-weight-regular"
          prepend-icon="mdi-plus"
          text="Adicionar Pessoa"
          v-bind="activatorProps"
          color="primary"
          data-cy="createButton"
        ></v-btn>
      </template>

      <v-card prepend-icon="mdi-account" title="Nova Pessoa">
        <v-card-text>
          <v-text-field
            label="Nome*"
            required
            v-model="newPerson.name"
            data-cy="nameInput"
          ></v-text-field>
          <v-text-field
            label="Email*"
            type="email"
            required
            v-model="newPerson.email"
            data-cy="emailInput"
          ></v-text-field>
        </v-card-text>

        <v-divider></v-divider>

        <v-card-actions>
          <v-spacer></v-spacer>

          <v-btn text="Fechar" variant="plain" @click="dialog = false"></v-btn>

          <v-btn
            color="primary"
            text="Guardar"
            variant="tonal"
            @click="savePerson"
            data-cy="saveButton"
          ></v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import PersonDto from '@/models/PersonDto'
import RemoteService from '@/services/RemoteService'

const dialog = ref(false)

const emit = defineEmits(['person-created'])

const newPerson = ref(new PersonDto({ name: '', email: '' }))

const savePerson = async () => {
  try {
    await RemoteService.createPerson(newPerson.value)
  } catch {
    // the error is already shown by RemoteService; keep the dialog open
    return
  }
  newPerson.value = new PersonDto({ name: '', email: '' })
  dialog.value = false
  emit('person-created')
}
</script>

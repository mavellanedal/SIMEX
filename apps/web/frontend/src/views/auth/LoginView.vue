<template>
  <main class="min-h-screen bg-[#145699] flex items-center justify-center px-4 py-12">
    <div class="bg-white p-16 rounded-lg shadow-md w-full max-w-lg">
     


      <form @submit.prevent="onSubmit" class="space-y-6">
        <img
          :src="logoPrime"
          alt="Logo Prime"
          class="mx-auto mb-8 w-80 h-auto object-contain"
        />

  
        <BaseInput
          v-model="username"
          type="username"
          placeholder="Nombre de usuario *"
          :error="usernameError"
        />

        <BaseInput
          v-model="password"
          type="password"
          placeholder="Contraseña *"
          :error="passwordError"
        />

        <div class="pt-2">
          <BaseCheckbox
            v-model="rememberMe"
            label="Recuérdame"
          />
        </div>

        <div class="pt-2">
          <BaseButton type="submit">
            Iniciar sesión
          </BaseButton>
        </div>
      </form>
    </div>
  </main>
</template>

<script setup>

import { ref } from 'vue'

import BaseInput from '../../components/base/BaseInput.vue'
import BaseCheckbox from '../../components/base/Basecheckbox.vue'
import logoPrime from '@/assets/images/logoPrime.webp'
import BaseButton from '../../components/base/BaseButton.vue'


const username = ref('')
const password = ref('')
const rememberMe = ref(false)

const usernameError = ref('')
const passwordError = ref('')

function onSubmit() {

  usernameError.value = ''
  passwordError.value = ''

  if (!username.value) {

    usernameError.value = 'El nombre de usuario es requerido.'

  }

  if (!password.value) {

    passwordError.value = 'La contraseña es requerida.'

  }

  if (usernameError.value || passwordError.value) {
    return
  }

  console.log('Formulario válido. Enviando datos...')
  console.log('Usuario:', username.value)
  console.log('Contraseña:', password.value)
}
</script>

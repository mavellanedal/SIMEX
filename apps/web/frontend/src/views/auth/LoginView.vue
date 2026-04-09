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
          <BaseButton type="submit">
            Iniciar sesión
          </BaseButton>
        </div>
      </form>
    </div>
  </main>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import api from '@services/api'
import { useRouter } from 'vue-router'

import BaseInput from '@components/base/BaseInput.vue'
import BaseButton from '@components/base/BaseButton.vue'
import logoPrime from '@/assets/images/logoPrime.webp'

const router = useRouter()

const username = ref('')
const password = ref('')

const usernameError = ref('')
const passwordError = ref('')

async function onSubmit() {
  // Limpiamos errores previos
  usernameError.value = ''
  passwordError.value = ''

  // Validación
  if (!username.value) {
    usernameError.value = 'El nombre de usuario es requerido.'
  }

  if (!password.value) {
    passwordError.value = 'La contraseña es requerida.'
  }

  // Si hay errores cortamos la ejecución aquí
  if (usernameError.value || passwordError.value) {
    return
  }

  try {
    const response = await api.post('/login', {
      username: username.value,
      password: password.value
    })

    // Guardamos los datos de sesión en localStorage
    localStorage.setItem('access_token', response.data.access_token)
    localStorage.setItem('user', JSON.stringify(response.data.user))

    // Redirigimos al usuario
    router.push('/dashboard')

  } catch (error: any) {
    console.error('Login failed:', error)

    if (error.response && (error.response.status === 401 || error.response.status === 422)) {
      passwordError.value = 'Las credenciales proporcionadas son incorrectas.'
    } else {
      passwordError.value = 'Ocurrió un error al intentar iniciar sesión. Por favor, inténtalo de nuevo más tarde.'
    }
  }
}
</script>

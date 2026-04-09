<template>
  <main class="min-h-screen bg-[#145699] flex items-center justify-center px-4 py-12">
    <div class="bg-white p-16 rounded-lg shadow-md w-full max-w-lg">

      <form @submit.prevent="onSubmit" class="space-y-6" novalidate>
        <img
          :src="logoPrime"
          alt="Logo Prime"
          class="mx-auto mb-8 w-80 h-auto object-contain"
        />


        <BaseInput
          v-model="username"
          id="username"
          label="Nombre de usuario"
          type="text"
          placeholder="Escribe tu usuario"
          :error="usernameError"
        />

        <div class="relative">
          <BaseInput
            v-model="password"
            id="password"
            label="Contraseña"
            :type="showPassword ? 'text' : 'password'"
            placeholder="Escribe tu contraseña"
            :error="passwordError"
          />
        </div>

        <div class="pt-4">
          <BaseButton type="submit" class="w-full">
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
const showPassword = ref(false)
// const rememberMe = ref(false)

  const usernameError = ref('')
  const passwordError = ref('')

async function onSubmit() {
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

  try {
    const response = await api.post('/login', {
      username: username.value,
      password: password.value
    })

    localStorage.setItem('access_token', response.data.access_token)
    localStorage.setItem('user', JSON.stringify(response.data.user))

    router.push('/dashboard')

  } catch (error: any) {
    console.error('Login failed:', error)

    if (error.response && (error.response.status === 401 || error.response.status === 422)) {
      passwordError.value = 'Las credenciales proporcionadas son incorrectas.'
    } else {
      passwordError.value = 'Ocurrió un error al intentar iniciar sesión. Verifica tu conexión.'
    }
  }
}
</script>

        />

          <button
            type="button"
            @click="showPassword = !showPassword"
            class="absolute right-4 top-9.5 text-gray-400 hover:text-[#FD8036] transition-colors flex items-center justify-center"
            title="Mostrar/Ocultar contraseña"
          >
            <span class="material-symbols-outlined text-[22px]">
              {{ showPassword ? 'visibility_off' : 'visibility' }}
            </span>
          </button>
        </div>

        <div class="pt-4">
          <BaseButton type="submit" class="w-full">
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
<<<<<<< HEAD
const showPassword = ref(false)
=======
// const rememberMe = ref(false)
>>>>>>> f104746 (minor changes)

  const usernameError = ref('')
  const passwordError = ref('')

async function onSubmit() {
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

  try {
    const response = await api.post('/login', {
      username: username.value,
      password: password.value
    })

    localStorage.setItem('access_token', response.data.access_token)
    localStorage.setItem('user', JSON.stringify(response.data.user))

    router.push('/dashboard')

  } catch (error: any) {
    console.error('Login failed:', error)

    if (error.response && (error.response.status === 401 || error.response.status === 422)) {
      passwordError.value = 'Las credenciales proporcionadas son incorrectas.'
    } else {
      passwordError.value = 'Ocurrió un error al intentar iniciar sesión. Verifica tu conexión.'
    }
  }
}
</script>

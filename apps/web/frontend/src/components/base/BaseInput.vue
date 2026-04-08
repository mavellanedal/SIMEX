<template>
  <div>
    <!-- Esta etiqueta solo se muestra si se le pasa la prop "label" -->
    <label v-if="label" class="mb-1 block text-sm font-medium text-gray-700">
      {{ label }}
    </label>

    <div class="relative">
      

      <input
        :type="inputType"
        :placeholder="placeholder"
        :value="modelValue"
        :class="inputClasses"
        @input="onInput"
      />

      <button
        v-if="type === 'password'"
        type="button"
        class="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400"
        @click="togglePassword"
      >
        <svg
          v-if="!showPassword"
          xmlns="http://www.w3.org/2000/svg"
          class="h-6 w-6"
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          stroke-width="2"
        >
          <path d="M1 12s4-7 11-7 11 7 11 7-4 7-11 7S1 12 1 12z" />
          <circle cx="12" cy="12" r="3" />
        </svg>

        <svg
          v-else
          xmlns="http://www.w3.org/2000/svg"
          class="h-6 w-6"
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          stroke-width="2"
        >
          <path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-7-11-7a10.07 10.07 0 0 1 4.93-4.93" />
          <path d="M9.9 4.24A9.98 9.98 0 0 1 12 4c7 0 11 7 11 7a9.98 9.98 0 0 1-2.26 3.34" />
          <line x1="1" y1="1" x2="23" y2="23" />
          <line x1="12" y1="12" x2="12" y2="12" />
        </svg>
      </button>
    </div>

    <p v-if="error" class="mt-1 text-sm text-red-600">{{ error }}</p>
  </div>
</template>

<script setup>

import { computed, ref } from 'vue'

const props = defineProps({
  type: { type: String, default: 'text' },
  placeholder: { type: String, default: '' },
  modelValue: { type: String, default: '' },
  label: { type: String, default: '' },
  error: { type: String, default: '' }
})


const emit = defineEmits(['update:modelValue'])

const showPassword = ref(false)

const inputType = computed(() => {

  if (props.type === 'password') {
    
    if (showPassword.value) {
      return 'text' 
    } else {
      return 'password'
    }
  }
  return props.type
})

const inputClasses = computed(() => {
  const baseClasses = 'h-12 w-full rounded-lg border bg-white px-4 pr-12 text-base font-semibold text-gray-700 outline-none transition-colors duration-150 placeholder:font-normal placeholder:text-gray-400 focus:ring-2'
  
  if (props.error) {
    return `${baseClasses} border-red-500 focus:border-red-500 focus:ring-red-400`
  } else {
    return `${baseClasses} border-gray-300 focus:border-orange-500 focus:ring-orange-400`
  }
})


function onInput(event) {
  
  emit('update:modelValue', event.target.value)
}

function togglePassword() {
  showPassword.value = !showPassword.value
}
</script>

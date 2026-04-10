<template>
  <div class="flex flex-col gap-1.5">
    <label :for="id" class="text-sm font-semibold text-gray-700 flex justify-between items-center">
      <span>{{ label }} <span v-if="required" class="text-red-500">*</span></span>
      <slot name="label-suffix"></slot>
    </label>

    <input
      :id="id"
      :type="type"
      :value="modelValue"
      @input="$emit('update:modelValue', ($event.target as HTMLInputElement).value)"
      :placeholder="placeholder"
      :class="[
        'w-full px-4 py-2.5 border rounded-lg text-base text-gray-900 shadow-sm focus:outline-none focus:ring-1 transition-colors bg-white',
        error ? 'border-red-500 focus:border-red-500 focus:ring-red-500' : 'border-gray-300 focus:border-[#FD8036] focus:ring-[#FD8036]'
      ]"
    >

    <transition name="fade">
      <span v-if="error" class="text-xs font-semibold text-red-500">{{ error }}</span>
    </transition>
  </div>
</template>

<script setup lang="ts">
defineProps({
  modelValue: { type: [String, Number], default: '' },
  label: { type: String, required: true },
  id: { type: String, required: true },
  type: { type: String, default: 'text' },
  placeholder: { type: String, default: '' },
  required: { type: Boolean, default: false },
  error: { type: String, default: '' } // <-- Nueva propiedad
});

defineEmits(['update:modelValue']);
</script>

<style scoped>
.fade-enter-active, .fade-leave-active { transition: opacity 0.2s ease, transform 0.2s ease; }
.fade-enter-from, .fade-leave-to { opacity: 0; transform: translateY(-5px); }
</style>

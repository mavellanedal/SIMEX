<template>
  
  <label class="inline-flex items-center gap-3 cursor-pointer">
    <input
      type="checkbox"
      :checked="modelValue"
      class="h-5 w-5 rounded-lg border-gray-300 text-blue-600 focus:ring-blue-500"
      @change="$emit('update:modelValue', $event.target.checked)"
    />
    <span class="text-lg font-medium text-gray-800">{{ label }}</span>
  </label>
</template>

<script setup>


defineProps({
  label: { type: String, default: '' },
  modelValue: { type: Boolean, default: false }
})


defineEmits(['update:modelValue'])
</script>

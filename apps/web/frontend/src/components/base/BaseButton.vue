<template>
  <button
    :type="type"
    :disabled="disabled"
    class="bg-[#1E67B6] text-white py-3 rounded-xl transition-colors duration-150 hover:bg-[#FD8036] disabled:opacity-60"
  >
    <slot />
  </button>
</template>

<script setup>
defineProps({
  type: { type: String, default: 'button' },
  disabled: { type: Boolean, default: false }
})
</script>
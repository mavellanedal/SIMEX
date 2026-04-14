<template>
  <transition name="slide-fade">
    <div
      v-if="show"
      class="fixed top-6 right-6 z-50 flex items-center gap-3 px-5 py-4 rounded-lg shadow-lg border"
      :class="isSuccess ? 'bg-green-50 border-green-200 text-green-800' : 'bg-red-50 border-red-200 text-red-800'"
    >
      <span class="material-symbols-outlined text-2xl">
        {{ isSuccess ? 'check_circle' : 'error' }}
      </span>
      <div>
        <h4 class="font-bold text-sm">{{ isSuccess ? '¡Éxito!' : 'Error' }}</h4>
        <p class="text-sm opacity-90">{{ message }}</p>
      </div>

      <button @click="close" class="ml-4 opacity-50 hover:opacity-100 transition-opacity">
        <span class="material-symbols-outlined text-xl">close</span>
      </button>
    </div>
  </transition>
</template>

<script setup lang="ts">
import { watch, ref } from 'vue';

const props = defineProps({
  show: { type: Boolean, required: true },
  message: { type: String, required: true },
  type: { type: String, default: 'success' } // 'success' o 'error'
});

const emit = defineEmits(['update:show']);
const isSuccess = ref(props.type === 'success');
let timeoutId: any = null;

watch(() => props.show, (newVal) => {
  isSuccess.value = props.type === 'success';
  if (newVal) {
    if (timeoutId) clearTimeout(timeoutId);
    timeoutId = setTimeout(() => {
      close();
    }, 3500);
  }
});

function close() {
  emit('update:show', false);
}
</script>

<style scoped>
.slide-fade-enter-active {
  transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
}
.slide-fade-leave-active {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}
.slide-fade-enter-from {
  opacity: 0;
  transform: translateX(30px) translateY(-10px);
}
.slide-fade-leave-to {
  opacity: 0;
  transform: translateX(30px);
}
</style>

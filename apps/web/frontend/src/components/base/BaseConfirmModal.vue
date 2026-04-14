<template>
  <transition name="modal">
    <div v-if="isOpen" class="fixed inset-0 z-50 flex items-center justify-center px-4">
      <div
        class="fixed inset-0 bg-gray-900/40 backdrop-blur-sm transition-opacity"
        @click="$emit('cancel')"
      ></div>

      <div class="bg-white rounded-xl shadow-2xl max-w-md w-full p-6 relative z-10 transform transition-all">
        <div class="flex flex-col items-center text-center">
          <div class="w-16 h-16 bg-red-100 text-red-600 rounded-full flex items-center justify-center mb-4">
            <span class="material-symbols-outlined text-4xl">warning</span>
          </div>

          <h3 class="text-xl font-bold text-gray-900 mb-2">{{ title }}</h3>
          <p class="text-sm text-gray-500 mb-6">{{ message }}</p>

          <div class="flex gap-3 w-full">
            <button
              @click="$emit('cancel')"
              class="flex-1 bg-white border border-gray-300 text-gray-700 hover:bg-gray-50 font-semibold py-2.5 px-4 rounded-lg transition-colors"
            >
              {{ cancelText }}
            </button>
            <button
              @click="$emit('confirm')"
              class="flex-1 bg-red-600 hover:bg-red-700 text-white font-semibold py-2.5 px-4 rounded-lg transition-colors shadow-sm"
            >
              {{ confirmText }}
            </button>
          </div>
        </div>
      </div>
    </div>
  </transition>
</template>

<script setup lang="ts">
defineProps({
  isOpen: { type: Boolean, required: true },
  title: { type: String, default: '¿Estás seguro?' },
  message: { type: String, required: true },
  confirmText: { type: String, default: 'Confirmar' },
  cancelText: { type: String, default: 'Cancelar' }
});

defineEmits(['confirm', 'cancel']);
</script>

<style scoped>
.modal-enter-active, .modal-leave-active {
  transition: opacity 0.3s ease;
}
.modal-enter-from, .modal-leave-to {
  opacity: 0;
}
.modal-enter-active .bg-white {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}
.modal-enter-from .bg-white {
  opacity: 0;
  transform: scale(0.95) translateY(10px);
}
</style>

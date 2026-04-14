<template>
  <div class="flex flex-col gap-1.5 relative" ref="dropdownRef">
    <label :for="id" class="text-sm font-semibold text-gray-700">
      {{ label }} <span v-if="required" class="text-red-500">*</span>
    </label>

    <div
      @click="toggleDropdown"
      :class="[
        'w-full px-4 py-2.5 bg-white border rounded-lg text-base text-gray-900 shadow-sm cursor-pointer flex justify-between items-center transition-colors',
        error ? 'border-red-500 ring-1 ring-red-500' : 'border-gray-300 hover:border-gray-400',
        isOpen && !error ? 'border-[#FD8036] ring-1 ring-[#FD8036]' : ''
      ]"
    >
      <span :class="{'text-gray-400': !selectedLabel}">{{ selectedLabel || placeholder }}</span>
      <span class="material-symbols-outlined text-gray-500 transition-transform duration-200" :class="{'rotate-180': isOpen}">
        expand_more
      </span>
    </div>

    <transition name="dropdown">
      <ul
        v-show="isOpen"
        class="absolute top-full mt-1 left-0 z-50 w-full bg-white border border-gray-200 rounded-lg shadow-xl max-h-60 overflow-y-auto py-1"
      >
        <li
          v-for="option in options"
          :key="option[valueKey]"
          @click="selectOption(option)"
          class="px-4 py-2.5 hover:bg-[#eef2f6] hover:text-[#145699] cursor-pointer text-base text-gray-700 transition-colors flex items-center"
          :class="{'bg-[#eef2f6] text-[#145699] font-bold': modelValue === option[valueKey]}"
        >
          {{ capitalizeFirst(option[labelKey]) }}
        </li>
        <li v-if="options.length === 0" class="px-4 py-3 text-gray-500 text-sm text-center">
          No hay opciones disponibles
        </li>
      </ul>
    </transition>

    <transition name="fade">
      <span v-if="error" class="text-xs font-semibold text-red-500">{{ error }}</span>
    </transition>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue';

const props = defineProps({
  modelValue: { type: [String, Number, null], default: '' },
  label: { type: String, required: true },
  id: { type: String, required: true },
  placeholder: { type: String, default: 'Seleccionar...' },
  required: { type: Boolean, default: false },
  options: { type: Array as () => any[], required: true },
  valueKey: { type: String, default: 'id' },
  labelKey: { type: String, default: 'name' },
  error: { type: String, default: '' } // <-- Nueva propiedad
});

const emit = defineEmits(['update:modelValue']);
const isOpen = ref(false);
const dropdownRef = ref<HTMLElement | null>(null);

const selectedLabel = computed(() => {
  const selected = props.options.find(opt => opt[props.valueKey] === props.modelValue);
  return selected ? capitalizeFirst(selected[props.labelKey]) : '';
});

function toggleDropdown() { isOpen.value = !isOpen.value; }

function selectOption(option: any) {
  emit('update:modelValue', option[props.valueKey]);
  isOpen.value = false;
}

function handleClickOutside(event: MouseEvent) {
  if (dropdownRef.value && !dropdownRef.value.contains(event.target as Node)) isOpen.value = false;
}

onMounted(() => document.addEventListener('click', handleClickOutside));
onUnmounted(() => document.removeEventListener('click', handleClickOutside));

function capitalizeFirst(str: string) {
  if (!str) return '';
  return str.charAt(0).toUpperCase() + str.slice(1).toLowerCase();
}
</script>

<style scoped>
.dropdown-enter-active, .dropdown-leave-active { transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1); }
.dropdown-enter-from, .dropdown-leave-to { opacity: 0; transform: translateY(-10px); }
.fade-enter-active, .fade-leave-active { transition: opacity 0.2s ease, transform 0.2s ease; }
.fade-enter-from, .fade-leave-to { opacity: 0; transform: translateY(-5px); }
</style>

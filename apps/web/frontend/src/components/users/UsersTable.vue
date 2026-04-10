<template>
  <div class="overflow-x-auto bg-white rounded-b-lg shadow-sm">
    <table class="w-full text-left text-sm text-gray-700">
      <thead class="bg-[#a8b8d0] text-gray-900 font-bold">
        <tr>
          <th scope="col" class="p-4 w-4">
            <input
              v-model="isAllSelected"
              type="checkbox"
              class="w-4 h-4 text-[#145699] rounded border-gray-300 focus:ring-[#145699]"
            >
          </th>
          <th scope="col" class="px-4 py-3">Usuario</th>
          <th scope="col" class="px-4 py-3">Correo Electrónico</th>
          <th scope="col" class="px-4 py-3 text-center">Rol</th>
          <th scope="col" class="px-4 py-3 text-center">Empresa Perteneciente</th>
          <th scope="col" class="px-4 py-3 text-center">Estado</th>
          <th scope="col" class="px-4 py-3 text-center">Último Acceso</th>
          <th scope="col" class="px-4 py-3 text-center">Acciones</th>
        </tr>
      </thead>
      <tbody>
        <tr
          v-for="(user, index) in users"
          :key="user.id"
          :class="[
            'border-b border-gray-100 hover:bg-blue-50/50 transition-colors',
            index % 2 === 0 ? 'bg-white' : 'bg-gray-50',
            selectedUsers.includes(user.id) ? 'bg-blue-50/50' : ''
          ]"
        >
          <td class="p-4">
            <input
              v-model="selectedUsers"
              :value="user.id"
              type="checkbox"
              class="w-4 h-4 text-[#145699] rounded border-gray-300 focus:ring-[#145699]"
            >
          </td>
          <td class="px-4 py-3 font-medium text-gray-900 flex items-center gap-3">
            <div class="w-8 h-8 rounded-full bg-blue-100 text-[#145699] flex items-center justify-center font-bold text-xs">
              {{ getInitials(user.nombre) }}
            </div>
            {{ user.nombre }}
          </td>
          <td class="px-4 py-3">{{ user.email }}</td>
          <td class="px-4 py-3 text-center">
            <span class="bg-[#3FA9F5] text-white text-[11px] font-bold px-3 py-1 rounded-full">
              {{ user.rol.name }}
            </span>
          </td>
          <td class="px-4 py-3 text-center">
            {{ user.company ? user.company.name : '-' }}
          </td>
          <td class="px-4 py-3 text-center">
            <span
              :class="[
                'text-[11px] font-bold px-3 py-1 rounded-full text-white',
                user.active ? 'bg-[#22C55E]' : 'bg-gray-400'
              ]"
            >
              {{ user.active ? 'Activo' : 'Inactivo' }}
            </span>
          </td>
          <td class="px-4 py-3 text-center text-gray-500">
            N/A
          </td>
          <td class="px-4 py-3 text-center">
            <button
              @click="$emit('edit', user)"
              class="inline-flex items-center gap-1 text-[#145699] hover:text-[#FD8036] transition-colors font-semibold text-xs"
            >
              <span class="material-symbols-outlined text-[18px]">edit</span>
              Editar
            </button>
          </td>
        </tr>

        <tr v-if="users.length === 0">
          <td colspan="8" class="px-4 py-8 text-center text-gray-500">
            No se encontraron usuarios.
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue';
import type { PropType } from 'vue';
import type { User } from '@/interfaces/user/user';

const props = defineProps({
  users: {
    type: Array as PropType<User[]>,
    required: true,
  }
});

const emit = defineEmits(['edit', 'selection-changed']);

const selectedUsers = ref<number[]>([]);

const isAllSelected = computed({
  get: () => props.users.length > 0 && selectedUsers.value.length === props.users.length,

  set: (value: boolean) => {
    if (value) {
      selectedUsers.value = props.users.map(user => user.id);
    } else {
      selectedUsers.value = [];
    }
  }
});

watch(() => props.users, () => {
  selectedUsers.value = [];
});

watch(selectedUsers, (newSelection) => {
  emit('selection-changed', newSelection);
});

function getInitials(name: string | undefined): string {
  if (!name) return 'U';
  const parts = name.trim().replace(/\s+/g, ' ').split(' ');
  if (parts.length >= 2) {
    return (parts[0].charAt(0) + parts[1].charAt(0)).toUpperCase();
  }
  return name.substring(0, 2).toUpperCase();
}
</script>

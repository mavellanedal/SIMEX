<template>
  <div class="overflow-x-auto bg-white rounded-b-lg shadow-sm pb-16"> <!-- Añadido pb-16 para que el último dropdown tenga espacio y no se corte -->
    <table class="w-full text-left text-sm text-gray-700">
      <thead class="bg-[#a8b8d0] text-gray-900 font-bold">
        <tr>
          <th scope="col" class="px-4 py-3">Operación</th>
          <th scope="col" class="px-4 py-3">Importador</th>
          <th scope="col" class="px-4 py-3">Exportador</th>
          <th scope="col" class="px-4 py-3 text-center">Origen</th>
          <th scope="col" class="px-4 py-3 text-center">Destino</th>
          <th scope="col" class="px-4 py-3 text-center">Incoterm</th>
          <th scope="col" class="px-4 py-3 text-center">Estado</th>
          <th scope="col" class="px-4 py-3 text-center">Acciones</th>
        </tr>
      </thead>
      <tbody>
        <tr
          v-for="(operation, index) in operations"
          :key="operation.id"
          :class="[
            'border-b border-gray-100 hover:bg-blue-50/50 transition-colors',
            index % 2 === 0 ? 'bg-white' : 'bg-gray-50'
          ]"
        >
          <td class="px-4 py-3 font-medium text-gray-900 flex items-center gap-3">
              {{ operation.orderReference }}
          </td>
          <td class="px-4 py-3">{{ operation.importer?.name || 'N/A' }}</td>
          <td class="px-4 py-3">{{ operation.exporter?.name || 'N/A' }}</td>
          <td class="px-4 py-3 text-center">
            {{ operation.originPort?.name || 'N/A' }}
          </td>
          <td class="px-4 py-3 text-center">
            {{ operation.destinationPort?.name || 'N/A' }}
          </td>
          <td class="px-4 py-3 text-center">
            {{ operation.incoterm?.incotermType?.name || 'N/A' }}
          </td>
          <td class="px-4 py-3 text-center">
            <span v-if="operation.currentStateId === 1" class="text-blue-600 font-semibold">Pendiente</span>
            <span v-else-if="operation.currentStateId === 2" class="text-green-600 font-semibold">Aceptada</span>
            <span v-else-if="operation.currentStateId === 3" class="text-red-600 font-semibold">Rechazada</span>
            <span v-else class="text-gray-600">{{ operation.currentStateId }}</span>
          </td>
          <td class="px-4 py-3 text-center relative">
            <button
              v-if="![2, 3].includes(operation.currentStateId)"
              @click.stop="toggleMenu(operation.id)"
              class="w-8 h-8 rounded-full flex items-center justify-center text-gray-500 hover:bg-gray-200"
              title="Acciones"
            >
              <span class="material-symbols-outlined text-[20px]">more_vert</span>
            </button>
            <div
              v-if="openedMenuId === operation.id"
              class="absolute right-10 top-10 w-48 bg-white border border-gray-100 rounded-lg shadow-xl z-50 py-1 flex flex-col text-left"
            >
              <!-- Opciones: Aceptar y Rechazar (Aparecen si currentStateId === 1) -->
              <template v-if="operation.currentStateId === 1">
                <button
                  @click="handleAction('accept', operation)"
                  class="w-full px-4 py-2 text-sm text-[#22C55E] hover:bg-green-50 flex items-center gap-2 transition-colors"
                >
                  <span class="material-symbols-outlined text-[18px]">check_circle</span>
                  Aceptar Operación
                </button>
                <button
                  @click="handleAction('reject', operation)"
                  class="w-full px-4 py-2 text-sm text-red-500 hover:bg-red-50 flex items-center gap-2 transition-colors"
                >
                  <span class="material-symbols-outlined text-[18px]">cancel</span>
                  Rechazar Operación
                </button>
              </template>

              <!-- Opción: Editar (Solo ROLES DIFERENTES AL 2) -->
              <button
                v-if="userRoleId !== 2"
                @click="handleAction('edit', operation)"
                :class="['w-full px-4 py-2 text-sm text-[#145699] hover:bg-blue-50 flex items-center gap-2 transition-colors', operation.currentStateId === 1 ? 'border-t border-gray-50' : '']"
              >
                <span class="material-symbols-outlined text-[18px]">edit</span>
                Editar Operación
              </button>
            </div>

          </td>
        </tr>

        <tr v-if="operations.length === 0">
          <td colspan="8" class="px-4 py-8 text-center text-gray-500">
            No se encontraron operaciones.
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue';
import type { PropType } from 'vue';
import type { OperationLight } from '@interfaces/operation/operationLight';

const props = defineProps({
  operations: {
    type: Array as PropType<OperationLight[]>,
    required: true,
  },
  userRoleId: {
    type: Number,
    required: true,
    default: 0
  }
});

const emit = defineEmits(['edit', 'accept', 'reject']);

// Estado para controlar qué dropdown está abierto
const openedMenuId = ref<number | null>(null);

function toggleMenu(id: number) {
  openedMenuId.value = openedMenuId.value === id ? null : id;
}
function handleAction(action: 'edit' | 'accept' | 'reject', operation: OperationLight) {
  openedMenuId.value = null;
  emit(action, operation);
}

function handleClickOutside(event: MouseEvent) {
  const target = event.target as HTMLElement;
  if (!target.closest('.absolute') && !target.closest('button[title="Acciones"]')) {
    openedMenuId.value = null;
  }
}

onMounted(() => {
  document.addEventListener('click', handleClickOutside);
});

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside);
});

</script>

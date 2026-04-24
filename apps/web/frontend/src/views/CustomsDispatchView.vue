<template>
  <div class="h-full flex flex-col text-gray-800">
    <header class="mb-4">
      <h1 class="text-3xl font-bold">Despacho Aduanero</h1>
    </header>

    <div class="flex flex-1 gap-6">
      <!-- Columna de Filtros -->
      <aside class="w-1/4 bg-white p-6 rounded-xl shadow-lg flex flex-col">
        <h2 class="text-xl font-semibold mb-6">Filtros</h2>
        
        <div class="space-y-4">
          <BaseInput v-model="filters.duaId" placeholder="Placeholder" label="DUA id" />
          <BaseInput v-model="filters.ref" placeholder="Placeholder" label="Número de referencia Operación" />
          <BaseInput v-model="filters.seller" placeholder="Placeholder" label="Vendedor" />
          <BaseInput v-model="filters.buyer" placeholder="Placeholder" label="Comprador" />
          <BaseInput v-model="filters.origin" placeholder="Placeholder" label="Origen" />
          <BaseInput v-model="filters.destination" placeholder="Placeholder" label="Destino" />
        </div>

        <div class="mt-auto pt-6 flex gap-4">
          <BaseButton @click="clearFilters" class="w-full bg-gray-200 hover:bg-gray-300 text-gray-700">
            Borrar filtros
          </BaseButton>
          <BaseButton @click="applyFilters" class="w-full bg-[#FD8036] hover:bg-orange-600 text-white">
            Filtrar
          </BaseButton>
        </div>
      </aside>

      <!-- Contenido Principal -->
      <main class="w-3/4 flex flex-col">
        <div class="flex justify-end mb-4">
          <BaseButton 
            @click="openDuaModal"
            class="bg-[#FD8036] hover:bg-orange-600 text-white !py-2 !px-4 !text-sm flex items-center"
          >
            <span class="material-symbols-outlined mr-2 !text-base">add</span>
            Crear Nuevo DUA
          </BaseButton>
        </div>

        <div class="bg-white rounded-xl shadow-lg overflow-hidden flex-1">
          <table class="w-full text-left">
            <thead class="bg-gray-50 border-b border-gray-200">
              <tr>
                <th v-for="header in tableHeaders" :key="header" class="p-4 font-semibold text-gray-600">
                  {{ header }}
                </th>
              </tr>
            </thead>
            <TransitionGroup name="list" tag="tbody">
              <tr v-for="(dispatch, index) in dispatches" :key="dispatch.id" class="border-b border-gray-100 last:border-b-0 hover:bg-gray-50" :style="{'--delay': index * 0.05 + 's'}">
                <td class="p-4">{{ dispatch.duaId }}</td>
                <td class="p-4">{{ dispatch.operation }}</td>
                <td class="p-4">{{ dispatch.buyer }}</td>
                <td class="p-4">
                  <span 
                    class="px-2 py-1 text-xs font-semibold rounded-full"
                    :class="dispatch.state.color"
                  >
                    {{ dispatch.state.name }}
                  </span>
                </td>
                <td class="p-4">{{ formatDate(dispatch.creationDate) }}</td>
                <td class="p-4 relative">
                  <button @click.stop="toggleMenu(dispatch.id)" class="text-gray-500 hover:text-gray-800">
                    <span class="material-symbols-outlined">more_vert</span>
                  </button>
                  <!-- Menú desplegable -->
                  <Transition name="fade">
                    <div v-if="openMenuId === dispatch.id" class="absolute right-0 mt-2 w-48 bg-white rounded-md shadow-xl z-10">
                      <a href="#" class="flex items-center px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">
                        <span class="material-symbols-outlined mr-3 !text-base">edit</span>
                        Editar
                      </a>
                      <a href="#" class="flex items-center px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">
                        <span class="material-symbols-outlined mr-3 !text-base">visibility</span>
                        Ver
                      </a>
                      <a href="#" class="flex items-center px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">
                        <span class="material-symbols-outlined mr-3 !text-base">download</span>
                        Descargar
                      </a>
                    </div>
                  </Transition>
                </td>
              </tr>
            </TransitionGroup>
          </table>
        </div>
      </main>
    </div>
    
    <CustomsDispatchModal 
      v-if="isDuaModalOpen" 
      @close="closeDuaModal" 
      @save="saveDua" 
    />

  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, nextTick } from 'vue';
import BaseInput from '@components/base/BaseInput.vue';
import BaseButton from '@components/base/BaseButton.vue';
import CustomsDispatchModal from '@components/modals/CustomsDispatchModal.vue';

const filters = ref({
  duaId: '',
  ref: '',
  seller: '',
  buyer: '',
  origin: '',
  destination: ''
});

const tableHeaders = ['DUA ID', 'OPERACIÓN', 'COMPRADOR', 'ESTADO', 'FECHA CREACIÓN', 'ACCIONES'];

const dispatches = ref([]); // Empezar con la lista vacía

const openMenuId = ref(null);
const isDuaModalOpen = ref(false);

function toggleMenu(dispatchId) {
  openMenuId.value = openMenuId.value === dispatchId ? null : dispatchId;
}

function closeMenu() {
  openMenuId.value = null;
}

function openDuaModal() {
  isDuaModalOpen.value = true;
}

function closeDuaModal() {
  isDuaModalOpen.value = false;
}

function saveDua(payload) {
  console.log('Operación seleccionada:', payload.operation);
  console.log('Datos del DUA:', payload.duaData);
  closeDuaModal();
}

function formatDate(dateString) {
  const date = new Date(dateString);
  return date.toLocaleDateString('es-ES', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric'
  });
}

onMounted(() => {
  window.addEventListener('click', closeMenu);
  
  nextTick(() => {
    dispatches.value = [
      { id: 1, duaId: '00001', operation: 'DHL', buyer: 'Exportaciones SL', state: { name: 'Aceptado', color: 'bg-green-100 text-green-800' }, creationDate: '2026-04-10T10:00:00Z' },
      { id: 2, duaId: '00002', operation: 'UPS', buyer: 'Importaciones SL', state: { name: 'En Revisión', color: 'bg-yellow-100 text-yellow-800' }, creationDate: '2026-04-11T11:30:00Z' },
      { id: 3, duaId: '00003', operation: 'XPO', buyer: 'ExpEspaña', state: { name: 'Presentado', color: 'bg-blue-100 text-blue-800' }, creationDate: '2026-04-12T09:00:00Z' },
      { id: 4, duaId: '00004', operation: 'FedEx', buyer: 'AsianExports', state: { name: 'Rechazado', color: 'bg-red-100 text-red-800' }, creationDate: '2026-04-13T14:00:00Z' },
      { id: 5, duaId: '00005', operation: 'DHL', buyer: 'ImpAmerica SA', state: { name: 'Borrador', color: 'bg-gray-100 text-gray-800' }, creationDate: '2026-04-14T15:00:00Z' },
    ];
  });
});

onUnmounted(() => {
  window.removeEventListener('click', closeMenu);
});

function clearFilters() {
  for (const key in filters.value) {
    filters.value[key] = '';
  }
}

function applyFilters() {
  console.log('Aplicando filtros:', filters.value);
}
</script>

<style scoped>
/* Animación para la lista de la tabla */
.list-enter-active {
  transition: all 0.4s ease-out;
  transition-delay: var(--delay);
}
.list-enter-from {
  opacity: 0;
  transform: translateY(20px);
}

/* Animación para el menú desplegable */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease, transform 0.2s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}
</style>
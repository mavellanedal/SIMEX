<template>
  <div class="w-full px-4 md:px-8 mx-auto">
    <h1 class="text-3xl font-black text-[#145699] mb-6 tracking-tight">Operaciones</h1>

    <div class="bg-white rounded-lg shadow-md border border-gray-100 relative">
      <div class="bg-[#eef2f6] p-4 flex justify-between items-center rounded-t-lg border-b border-gray-200">
        <div class="flex items-center gap-3">
          <div class="relative w-80">
            <span class="material-symbols-outlined absolute left-3 top-1/2 -translate-y-1/2 text-gray-400">search</span>
            <input
              v-model="searchQuery"
              type="text"
              placeholder="Buscar por referencia o importador..."
              class="w-full pl-10 pr-4 py-2 rounded-full border border-gray-300 focus:outline-none focus:border-[#145699] focus:ring-1 focus:ring-[#145699] text-sm"
            >
          </div>
        </div>

        <button
          v-if="canModify"
          @click="openCreateModal"
          class="bg-[#145699] hover:bg-[#FD8036] text-white font-semibold py-2 px-5 rounded flex items-center gap-2 transition-colors text-sm"
        >
          <span class="material-symbols-outlined text-[20px]">add</span>
          Añadir operación
        </button>
      </div>

      <div v-if="isLoading" class="p-8 text-center text-gray-500">
        Cargando operaciones...
      </div>

      <OperationTable
        v-else
        :operations="filteredOperations"
        :userRoleId="userRoleId"
        @edit="openEditModal"
        @accept="handleAcceptOperation"
        @reject="handleRejectOperation"
      />
    </div>

    <BaseNotification
      v-model:show="notification.show"
      :message="notification.message"
      :type="notification.type"
    />

    <OperationModal
      v-if="isOperationModalOpen"
      :is-open="isOperationModalOpen"
      :operation-for-edit="selectedOperationForEdit"
      @confirm="handleOperationModalConfirm"
      @cancel="handleOperationModalCancel"
    />

    <div v-if="isRejectModalOpen" class="fixed inset-0 z-50 flex items-center justify-center p-4">
      <div class="fixed inset-0 bg-black/40 backdrop-blur-sm transition-opacity" @click="cancelReject"></div>
      <div class="bg-white rounded-xl shadow-2xl w-full max-w-md p-6 relative z-10 transform transition-all font-sans flex flex-col">
        <h3 class="text-xl font-bold text-gray-900 mb-4">Rechazar Operación</h3>
        <p class="text-sm text-gray-600 mb-4">
          Por favor, proporcione el motivo por el que rechaza esta operación.
        </p>

        <textarea
          v-model="rejectObservations"
          rows="4"
          class="w-full border border-gray-300 rounded-md p-3 text-sm focus:ring-[#145699] focus:border-[#145699]"
          placeholder="Observaciones o motivos del rechazo..."
        ></textarea>

        <div class="mt-6 flex justify-end gap-3">
          <button @click="cancelReject" class="px-4 py-2 text-gray-600 hover:bg-gray-100 rounded-md transition-colors">
            Cancelar
          </button>
          <button
            @click="confirmReject"
            :disabled="!rejectObservations.trim()"
            class="px-4 py-2 bg-red-600 text-white rounded-md hover:bg-red-700 transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
          >
            Confirmar Rechazo
          </button>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import OperationTable from '@components/operations/OperationsTable.vue';
import BaseNotification from '@components/base/BaseNotification.vue';
import OperationModal from '@components/modals/OperationModal.vue';

import type { Operation } from '@interfaces/operation/operation';
import type { CreateOperation } from '@interfaces/operation/createOperation';
import type { OperationLight } from '@interfaces/operation/operationLight';
import { OperationService } from '@services/operation.service';

const operations = ref<OperationLight[]>([]);
const searchQuery = ref('');
const isLoading = ref(false);


const userStr = localStorage.getItem('user');
const userRoleId = userStr ? JSON.parse(userStr).role?.id : 0;
const canModify = computed(() => userRoleId !== 5 && userRoleId !== 2);

const isOperationModalOpen = ref(false);
const selectedOperationForEdit = ref<Operation | null>(null);

 const isRejectModalOpen = ref(false);
const operationToReject = ref<OperationLight | null>(null);
const rejectObservations = ref('');

const notification = ref({
  show: false,
  message: '',
  type: 'success'
});

const filteredOperations = computed(() => {
  if (!searchQuery.value) return operations.value;

  const query = searchQuery.value.toLowerCase();
  return operations.value.filter(operation => {
    const referenceMatch = operation.orderReference?.toLowerCase().includes(query);
    const importerMatch = operation.importer?.name?.toLowerCase().includes(query);
    const exporterMatch = operation.exporter?.name?.toLowerCase().includes(query);

    return referenceMatch || importerMatch || exporterMatch;
  });
});

onMounted(async () => {
  await fetchOperations();
});

async function fetchOperations(showLoading = true) {
  if (showLoading) isLoading.value = true;
  try {
    operations.value = await OperationService.getOperations();
  } catch (error) {
    console.error('Error cargando operaciones:', error);
    notification.value = { show: true, type: 'error', message: 'Error al cargar las operaciones.' };
  } finally {
    if (showLoading) isLoading.value = false;
  }
}

function openCreateModal() {
  selectedOperationForEdit.value = null;
  isOperationModalOpen.value = true;
}

async function openEditModal(operationLight: OperationLight) {
  isLoading.value = true;
  try {
    const fullOperation = await OperationService.getOperation(operationLight.id);

    selectedOperationForEdit.value = fullOperation;
    isOperationModalOpen.value = true;
  } catch (error) {
    console.error('Error cargando los detalles de la operación:', error);
    notification.value = { show: true, type: 'error', message: 'No se pudieron cargar los detalles.' };
  } finally {
    isLoading.value = false;
  }
}

function handleOperationModalCancel() {
  isOperationModalOpen.value = false;
  selectedOperationForEdit.value = null;
}

async function handleOperationModalConfirm(formData: CreateOperation) {
  isOperationModalOpen.value = false;

  try {
    if (selectedOperationForEdit.value) {
      const operationId = selectedOperationForEdit.value.id;
      await OperationService.updateOperation(operationId, formData as Operation);
      notification.value = { show: true, type: 'success', message: 'Operación actualizada correctamente.' };
    } else {
      await OperationService.createOperation(formData as Operation);
      notification.value = { show: true, type: 'success', message: 'Operación creada correctamente.' };
    }

    selectedOperationForEdit.value = null;
    await fetchOperations(false);

} catch (error) {
    console.error('Error al guardar operación:', error);
    notification.value = {
      show: true,
      type: 'error',
      message: 'Hubo un error al guardar la operación. Comprueba los datos.'
    };
  }
}

async function handleAcceptOperation(operation: OperationLight) {
  try {
    await OperationService.changeOperationState(operation.id, 2);
    notification.value = { show: true, type: 'success', message: 'Operación aceptada.' };
    await fetchOperations(false);
  } catch (error) {
    console.error('Error al aceptar operación:', error);
    notification.value = { show: true, type: 'error', message: 'Error al aceptar la operación.' };
  }
}

function handleRejectOperation(operation: OperationLight) {
  operationToReject.value = operation;
  rejectObservations.value = '';
  isRejectModalOpen.value = true;
}

async function confirmReject() {
  if (!operationToReject.value) return;

  try {

    await OperationService.changeOperationState(
      operationToReject.value.id,
      3,
      rejectObservations.value
    );

    notification.value = { show: true, type: 'success', message: 'Operación rechazada.' };
    isRejectModalOpen.value = false;
    await fetchOperations(false);
  } catch (error) {
    console.error('Error al rechazar operación:', error);
    notification.value = { show: true, type: 'error', message: 'Error al rechazar la operación.' };
  }
}

function cancelReject() {
  isRejectModalOpen.value = false;
  operationToReject.value = null;
}
</script>

<template>
  <div class="w-full px-4 md:px-8 mx-auto">
    <h1 class="text-3xl font-black text-[#145699] mb-6 tracking-tight">Usuarios</h1>

    <div class="bg-white rounded-lg shadow-md border border-gray-100 relative">
      <div class="bg-[#eef2f6] p-4 flex justify-between items-center rounded-t-lg border-b border-gray-200">
        <div class="flex items-center gap-3">
          <div class="relative w-80">
            <span class="material-symbols-outlined absolute left-3 top-1/2 -translate-y-1/2 text-gray-400">search</span>
            <input
              v-model="searchQuery"
              type="text"
              placeholder="Buscar usuario"
              class="w-full pl-10 pr-4 py-2 rounded-full border border-gray-300 focus:outline-none focus:border-[#145699] focus:ring-1 focus:ring-[#145699] text-sm"
            >
          </div>

          <button
            v-if="selectedIds.length > 0"
            @click="promptBulkDelete"
            class="text-red-500 hover:text-red-700 bg-red-50 hover:bg-red-100 p-2 rounded-full transition-colors flex items-center justify-center"
            :title="`Eliminar ${selectedIds.length} usuario(s)`"
          >
            <span class="material-symbols-outlined text-[24px]">delete_sweep</span>
          </button>
        </div>

        <button
          v-if="canModify"
          @click="openCreateModal"
          class="bg-[#145699] hover:bg-[#FD8036] text-white font-semibold py-2 px-5 rounded flex items-center gap-2 transition-colors text-sm"
        >
          <span class="material-symbols-outlined text-[20px]">add</span>
          Añadir usuario
        </button>
      </div>

      <div v-if="isLoading" class="p-8 text-center text-gray-500">
        Cargando usuarios...
      </div>

      <UserTable
        v-else
        :users="filteredUsers"
        @edit="openEditModal"
        @selection-changed="handleSelectionChange"
      />
    </div>

    <BaseConfirmModal
      :is-open="showDeleteModal"
      title="Desactivar Usuarios"
      :message="`¿Estás seguro de que deseas desactivar a ${selectedIds.length} usuario(s)? No podrán acceder al ERP.`"
      confirm-text="Sí, desactivar"
      cancel-text="Cancelar"
      @confirm="executeBulkDelete"
      @cancel="showDeleteModal = false"
    />

    <BaseNotification
      v-model:show="notification.show"
      :message="notification.message"
      :type="notification.type"
    />

    <UserModal
      :is-open="isUserModalOpen"
      :user-for-edit="selectedUserForEdit"
      @confirm="handleUserModalConfirm"
      @cancel="handleUserModalCancel"
    />

  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import UserTable from '@components/users/UsersTable.vue';
import BaseConfirmModal from '@components/base/BaseConfirmModal.vue';
import BaseNotification from '@components/base/BaseNotification.vue';
import UserModal from '@components/modals/UserModal.vue';


import type { User } from '@/interfaces/user/user';
import type { CreateUser } from '@/interfaces/user/createUser';
import { UserService } from '@/services/user.service';

const users = ref<User[]>([]);
const searchQuery = ref('');
const isLoading = ref(false);
const selectedIds = ref<number[]>([]);
const showDeleteModal = ref(false);

const userStr = localStorage.getItem('user');
const userRoleId = userStr ? JSON.parse(userStr).role?.id : 0;

const canModify = computed(() => userRoleId !== 5);

const isUserModalOpen = ref(false);
const selectedUserForEdit = ref<User[] | null>(null);

const notification = ref({
  show: false,
  message: '',
  type: 'success'
});

const filteredUsers = computed(() => {
  if (!searchQuery.value) return users.value;
  const query = searchQuery.value.toLowerCase();

  return users.value.filter(user => {
    const fullName = `${user.firstName || ''} ${user.lastName || ''}`.toLowerCase();
    return fullName.includes(query) ||
      (user.email && user.email.toLowerCase().includes(query)) ||
      (user.company && user.company.name?.toLowerCase().includes(query));
  });
});

onMounted(async () => {
  await fetchUsers();
});

// Usamos el servicio limpio en lugar del Axios crudo
async function fetchUsers() {
  isLoading.value = true;
  try {
    users.value = await UserService.getUsers();
  } catch (error) {
    console.error('Error cargando usuarios:', error);
  } finally {
    isLoading.value = false;
  }
}

function handleSelectionChange(ids: number[]) {
  selectedIds.value = ids;
}

function openCreateModal() {
  selectedUserForEdit.value = null;
  isUserModalOpen.value = true;
}

function openEditModal(user: User) {
  selectedUserForEdit.value = [user];
  isUserModalOpen.value = true;
}

function handleUserModalCancel() {
  isUserModalOpen.value = false;
  selectedUserForEdit.value = null;
}

async function handleUserModalConfirm(formData: CreateUser) {
  isUserModalOpen.value = false;

  try {
    if (selectedUserForEdit.value && selectedUserForEdit.value.length > 0) {
      const userId = selectedUserForEdit.value[0].id;
      await UserService.updateUser(userId, formData);
      notification.value = { show: true, type: 'success', message: 'Usuario actualizado correctamente.' };
    } else {
      await UserService.createUser(formData);
      notification.value = { show: true, type: 'success', message: 'Usuario creado correctamente.' };
    }

    selectedUserForEdit.value = null;
    await fetchUsers();

  } catch (error) {
    console.error('Error al guardar usuario:', error);
    notification.value = {
      show: true,
      type: 'error',
      message: 'Hubo un error al guardar el usuario. Comprueba los datos.'
    };
  }
}

function promptBulkDelete() {
  showDeleteModal.value = true;
}

async function executeBulkDelete() {
  showDeleteModal.value = false;
  try {
    await UserService.deleteUsers(selectedIds.value);
    notification.value = { show: true, type: 'success', message: `Se han desactivado ${selectedIds.value.length} usuario(s) correctamente.` };
    selectedIds.value = [];
    await fetchUsers();
  } catch (error) {
    console.error('Error al desactivar usuarios:', error);
    notification.value = { show: true, type: 'error', message: 'Hubo un error al desactivar los usuarios.' };
  }
}
</script>

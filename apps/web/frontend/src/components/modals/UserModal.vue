<template>
  <transition name="modal">
    <div v-if="isOpen" class="fixed inset-0 z-50 flex items-center justify-center p-4">
      <div
        class="fixed inset-0 bg-black/40 backdrop-blur-sm transition-opacity"
        @click="onCancel"
      ></div>

      <div class="bg-white rounded-xl shadow-2xl max-w-4xl w-full p-10 relative z-10 transform transition-all font-sans">
        <h2 class="text-3xl font-bold text-[#145699] mb-8">{{ modalTitle }}</h2>

        <div v-if="isLoadingUser" class="flex justify-center items-center py-10">
          <span class="material-symbols-outlined animate-spin text-4xl text-[#FD8036]">autorenew</span>
        </div>

        <form v-else @submit.prevent="onConfirm" class="space-y-6" novalidate>
          <div class="grid grid-cols-1 md:grid-cols-2 gap-x-8 gap-y-6">

            <BaseInput v-model="form.first_name" id="first_name" label="Nombre" placeholder="Nombre" required :error="errors.first_name" />

            <BaseInput v-model="form.last_name" id="last_name" label="Apellido" placeholder="Apellido" required :error="errors.last_name" />

            <BaseInput v-model="form.username" id="username" label="Nombre de usuario" placeholder="Nombre de usuario" required :error="errors.username" />

            <BaseInput v-model="form.email" type="email" id="email" label="Correo Electrónico" placeholder="Correo Electrónico" required :error="errors.email" />

            <BaseInput v-model="form.password" type="password" id="password" label="Contraseña" placeholder="Contraseña" :required="!isEditMode" :error="errors.password">
              <template #label-suffix v-if="isEditMode">
                <span class="text-gray-400 font-normal text-xs">(Dejar en blanco para no cambiar)</span>
              </template>
            </BaseInput>

            <BaseInput v-model="form.password_confirmation" type="password" id="password_confirmation" label="Confirmar contraseña" placeholder="Confirmar contraseña" :required="!isEditMode" :error="errors.password_confirmation" />

            <BaseDropdown
              v-model="form.role_id"
              id="role_id"
              label="Rol"
              :options="roles"
              value-key="ID"
              label-key="NAME"
              placeholder="Selecciona un rol"
              required
              :error="errors.role_id"
            />

            <BaseDropdown
              v-if="Number(form.role_id) === 2"
              v-model="form.company_id"
              id="company_id"
              label="Empresa"
              :options="companies"
              value-key="ID"
              label-key="NAME"
              placeholder="Selecciona una empresa"
              required
              :error="errors.company_id"
            />

          </div>

          <div class="flex items-center gap-3 pt-4">
            <input v-model="form.active" type="checkbox" id="active" class="w-5 h-5 rounded border-gray-300 text-[#FD8036] focus:ring-[#FD8036]">
            <label for="active" class="text-base text-gray-800 font-medium">Activo</label>
          </div>

          <div class="flex justify-end gap-3 pt-6 border-t border-gray-100">
            <button
              type="button"
              @click="onCancel"
              class="bg-gray-100 hover:bg-gray-200 text-gray-800 font-semibold py-2.5 px-8 rounded-lg transition-colors text-base"
            >
              Cancelar
            </button>
            <button
              type="submit"
              class="bg-[#FD8036] hover:bg-[#145699] text-white font-semibold py-2.5 px-8 rounded-lg transition-colors shadow-sm text-base"
            >
              {{ confirmButtonText }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </transition>
</template>

<script setup lang="ts">
import { ref, computed, watch, onMounted } from 'vue';
import type { PropType } from 'vue';
import type { User } from '@/interfaces/user/user';
import { CompanyService } from '@services/comapny.service';
import { RoleService } from '@services/roles.services';
import { UserService } from '@services/user.service';
import type { CreateUser } from '@/interfaces/user/createUser';
import BaseInput from '@components/base/BaseInput.vue';
import BaseDropdown from '@components/base/BaseDropdown.vue';

const props = defineProps({
  isOpen: { type: Boolean, required: true },
  userForEdit: { type: Array as PropType<User[] | null>, default: null },
});

const emit = defineEmits(['confirm', 'cancel']);

const form = ref({
  first_name: '',
  last_name: '',
  username: '',
  email: '',
  password: '',
  password_confirmation: '',
  company_id: '' as string | number | null,
  role_id: '' as string | number,
  active: true,
});

// Estado para guardar los errores visuales
const errors = ref<Record<string, string>>({});

const companies = ref<any[]>([]);
const roles = ref<any[]>([]);
const isLoadingUser = ref(false);

const isEditMode = computed(() => !!props.userForEdit);
const modalTitle = computed(() => isEditMode.value ? 'Editar usuario' : 'Añadir nuevo usuario');
const confirmButtonText = computed(() => isEditMode.value ? 'Editar Usuario' : 'Crear Usuario');

onMounted(async () => {
    try {
        const [allCompanies, allRoles] = await Promise.all([
          CompanyService.getCompanies(),
          RoleService.getRoles()
        ]);
        companies.value = allCompanies;
        roles.value = allRoles;
    } catch(err) {
        console.error("Error cargando catálogos", err);
    }
});

watch(() => props.userForEdit, async (newVal) => {
  errors.value = {}; // Limpiamos errores al abrir el modal
  if (newVal && newVal.length > 0) {
    isLoadingUser.value = true;
    try {
      const userId = newVal[0].id;
      const userDetails = await UserService.getUserById(userId);

      form.value = {
        first_name: userDetails.first_name || '',
        last_name: userDetails.last_name || '',
        username: userDetails.username || '',
        email: userDetails.email || '',
        password: '',
        password_confirmation: '',
        company_id: userDetails.company ? userDetails.company.id : '',
        role_id: userDetails.rol ? userDetails.rol.id : '',
        active: Number(userDetails.active) === 1,
      };
    } catch (error) {
      console.error("Error obteniendo detalles:", error);
    } finally {
      isLoadingUser.value = false;
    }
  } else {
    form.value = {
      first_name: '', last_name: '', username: '', email: '',
      password: '', password_confirmation: '',
      company_id: '', role_id: '', active: true,
    };
  }
}, { immediate: true });

watch(form, (newForm) => {
  if (newForm.first_name) delete errors.value.first_name;
  if (newForm.last_name) delete errors.value.last_name;
  if (newForm.username) delete errors.value.username;
  if (newForm.email) delete errors.value.email;
  if (newForm.password) delete errors.value.password;
  if (newForm.password_confirmation === newForm.password) delete errors.value.password_confirmation;
  if (newForm.role_id) delete errors.value.role_id;
  if (newForm.company_id) delete errors.value.company_id;
}, { deep: true });

function validateForm() {
  errors.value = {};
  let isValid = true;

  if (!form.value.first_name) { errors.value.first_name = 'El nombre es obligatorio'; isValid = false; }
  if (!form.value.last_name) { errors.value.last_name = 'El apellido es obligatorio'; isValid = false; }
  if (!form.value.username) { errors.value.username = 'El nombre de usuario es obligatorio'; isValid = false; }

  if (!form.value.email) {
    errors.value.email = 'El correo es obligatorio';
    isValid = false;
  } else if (!/\S+@\S+\.\S+/.test(form.value.email)) {
    errors.value.email = 'El formato del correo no es válido';
    isValid = false;
  }

  if (!isEditMode.value && !form.value.password) {
    errors.value.password = 'La contraseña es obligatoria';
    isValid = false;
  }

  if (form.value.password !== form.value.password_confirmation) {
    errors.value.password_confirmation = 'Las contraseñas no coinciden';
    isValid = false;
  }

  if (!form.value.role_id) {
    errors.value.role_id = 'Debes seleccionar un rol';
    isValid = false;
  }

  if (Number(form.value.role_id) === 2 && !form.value.company_id) {
    errors.value.company_id = 'Debes asignar una empresa al cliente';
    isValid = false;
  }

  return isValid;
}

function onConfirm() {
  if (!validateForm()) return;

  const payloadToEmit: CreateUser = {
    first_name: form.value.first_name,
    last_name: form.value.last_name,
    username: form.value.username,
    email: form.value.email,
    role_id: Number(form.value.role_id),
    company_id: form.value.company_id ? Number(form.value.company_id) : null,
    active: form.value.active ? 1 : 0
  };

  if (form.value.password) {
    payloadToEmit.password = form.value.password;
  }

  emit('confirm', payloadToEmit);
}

function onCancel() {
  emit('cancel');
}

watch(() => form.value.role_id, (newRoleId) => {
  if (Number(newRoleId) !== 2) {
    form.value.company_id = '';
  }
});
</script>

<style scoped>
.modal-enter-active, .modal-leave-active { transition: opacity 0.3s ease; }
.modal-enter-from, .modal-leave-to { opacity: 0; }
</style>

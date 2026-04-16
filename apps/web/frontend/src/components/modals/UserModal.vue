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

            <BaseInput v-model="form.firstName" id="firstName" label="Nombre" placeholder="Nombre" required :error="errors.firstName" />

            <BaseInput v-model="form.lastName" id="lastName" label="Apellido" placeholder="Apellido" required :error="errors.lastName" />

            <BaseInput v-model="form.username" id="username" label="Nombre de usuario" placeholder="Nombre de usuario" required :error="errors.username" />

            <BaseInput v-model="form.email" type="email" id="email" label="Correo Electrónico" placeholder="Correo Electrónico" required :error="errors.email" />

            <BaseInput v-model="form.password" type="password" id="password" label="Contraseña" placeholder="Contraseña" :required="!isEditMode" :error="errors.password">
              <template #label-suffix v-if="isEditMode">
                <span class="text-gray-400 font-normal text-xs">(Dejar en blanco para no cambiar)</span>
              </template>
            </BaseInput>

            <BaseInput v-model="form.passwordConfirmation" type="password" id="passwordConfirmation" label="Confirmar contraseña" placeholder="Confirmar contraseña" :required="!isEditMode" :error="errors.passwordConfirmation" />

            <BaseDropdown
              v-model="form.roleId"
              id="roleId"
              label="Rol"
              :options="roles"
              value-key="id"
              label-key="name"
              placeholder="Selecciona un rol"
              required

              :error="errors.roleId"
            />

            <BaseDropdown
              v-if="Number(form.roleId) === 2"
              v-model="form.companyId"
              id="companyId"
              label="Empresa"
              :options="companies"
              value-key="ID"
              label-key="NAME"
              placeholder="Selecciona una empresa"
              required
              :error="errors.companyId"
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
import type { User } from '@interfaces/user/user';
import { CompanyService } from '@services/company.service';
import { RoleService } from '@services/roles.service';
import { UserService } from '@services/user.service';
import type { CreateUser } from '@interfaces/user/createUser';
import BaseInput from '@components/base/BaseInput.vue';
import BaseDropdown from '@components/base/BaseDropdown.vue';
import type { Role } from '@interfaces/roles/role';
import type { Company } from '@interfaces/companies/company';

const props = defineProps({
  isOpen: { type: Boolean, required: true },
  userForEdit: { type: Array as PropType<User[] | null>, default: null },
});

const emit = defineEmits(['confirm', 'cancel']);

const form = ref({
  firstName: '',
  lastName: '',
  username: '',
  email: '',
  password: '',
  passwordConfirmation: '',
  companyId: '' as string | number | null,
  roleId: '' as string | number,
  active: true,
});

// Estado para guardar los errores visuales
const errors = ref<Record<string, string>>({});

const companies = ref<Company[]>([]);
const roles = ref<Role[]>([]);
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
  errors.value = {};
  if (newVal && newVal.length > 0) {
    isLoadingUser.value = true;
    try {
      const userId = newVal[0]?.id;
      if (userId) {
        const userDetails = await UserService.getUserById(userId);
        form.value = {
          firstName: userDetails.firstName || '',
          lastName: userDetails.lastName || '',
          username: userDetails.username || '',
          email: userDetails.email || '',
          password: '',
          passwordConfirmation: '',
          companyId: userDetails.company ? userDetails.company.id : '',
          roleId: userDetails.role ? userDetails.role.id : '',
          active: userDetails.active === true,
        };
      }
    } catch (error) {
      console.error("Error obteniendo detalles:", error);
    } finally {
      isLoadingUser.value = false;
    }
  } else {
    form.value = {
      firstName: '', lastName: '', username: '', email: '',
      password: '', passwordConfirmation: '',
      companyId: '', roleId: '', active: true,
    };
  }
}, { immediate: true });

watch(form, (newForm) => {
  if (newForm.firstName) delete errors.value.firstName;
  if (newForm.lastName) delete errors.value.lastName;
  if (newForm.username) delete errors.value.username;
  if (newForm.email) delete errors.value.email;
  if (newForm.password) delete errors.value.password;
  if (newForm.passwordConfirmation === newForm.password) delete errors.value.passwordConfirmation;
  if (newForm.roleId) delete errors.value.roleId;
  if (newForm.companyId) delete errors.value.companyId;
}, { deep: true });

function validateForm() {
  errors.value = {};
  let isValid = true;

  if (!form.value.firstName) { errors.value.firstName = 'El nombre es obligatorio'; isValid = false; }
  if (!form.value.lastName) { errors.value.lastName = 'El apellido es obligatorio'; isValid = false; }
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

  if (form.value.password !== form.value.passwordConfirmation) {
    errors.value.passwordConfirmation = 'Las contraseñas no coinciden';
    isValid = false;
  }

  if (!form.value.roleId) {
    errors.value.roleId = 'Debes seleccionar un rol';
    isValid = false;
  }

  if (Number(form.value.roleId) === 2 && !form.value.companyId) {
    errors.value.companyId = 'Debes asignar una empresa al cliente';
    isValid = false;
  }

  return isValid;
}

function onConfirm() {
  if (!validateForm()) return;

  const payloadToEmit: CreateUser = {
    firstName: form.value.firstName,
    lastName: form.value.lastName,
    username: form.value.username,
    email: form.value.email,
    roleId: Number(form.value.roleId),
    companyId: form.value.companyId ? Number(form.value.companyId) : null,
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

watch(() => form.value.roleId, (newRoleId) => {
  if (Number(newRoleId) !== 2) {
    form.value.companyId = '';
  }
});
</script>

<style scoped>
.modal-enter-active, .modal-leave-active { transition: opacity 0.3s ease; }
.modal-enter-from, .modal-leave-to { opacity: 0; }
</style>

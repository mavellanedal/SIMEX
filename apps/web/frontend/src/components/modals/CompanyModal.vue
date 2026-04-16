<template>
  <div class="fixed inset-0 bg-gray-900/50 backdrop-blur-sm z-40 flex items-center justify-center" @click.self="$emit('close')">
    <div class="bg-white rounded-xl shadow-2xl p-8 w-full max-w-3xl">
      <h2 class="text-2xl font-bold text-[#1E67B6] mb-6">
        {{ isEditing ? 'Modificar Empresa' : 'Añadir nueva empresa' }}
      </h2>

      <form @submit.prevent="handleSubmit">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-x-8 gap-y-4">
          <BaseInput v-model="form.NAME" id="company-name" name="NAME" label="Nombre de la empresa" />
          <BaseInput v-model="form.ICON_PATH" id="company-icon" name="ICON_PATH" label="URL del Icono de la empresa" />
          <BaseInput v-model="form.ADDRESS" id="company-address" name="ADDRESS" label="Dirección" />
          <BaseInput v-model="form.EMAIL" id="company-email" name="EMAIL" label="Correo Electrónico" type="email" />
          <BaseInput v-model="form.PHONE_NUMBER" id="company-phone" name="PHONE_NUMBER" label="Número de teléfono" />
          <BaseInput v-model="form.NIF" id="company-nif" name="NIF" label="NIF" />

          <div>
            <label for="company-type" class="mb-1 block text-sm font-medium text-gray-700">Tipo de Empresa</label>
            <select v-model="form.COMPANY_TYPE_ID" id="company-type" class="h-12 w-full rounded-lg border bg-white px-4 text-base font-semibold text-gray-700 outline-none focus:ring-2 border-gray-300 focus:border-orange-500">
              <option :value="null">-- Selecciona un Tipo --</option>
              <option v-for="type in companyTypes" :key="type.id" :value="type.id">{{ type.name }}</option>
            </select>
          </div>

          <div>
            <label for="country" class="mb-1 block text-sm font-medium text-gray-700">País</label>
            <select v-model="form.COUNTRY_ID" id="country" class="h-12 w-full rounded-lg border bg-white px-4 text-base font-semibold text-gray-700 outline-none focus:ring-2 border-gray-300 focus:border-orange-500">
              <option :value="null">-- Selecciona un País --</option>
              <option v-for="country in allCountries" :key="country.id" :value="country.id">{{ country.name }}</option>
            </select>
          </div>

          <div>
            <label for="region" class="mb-1 block text-sm font-medium text-gray-700">Región</label>
            <select v-model="form.REGION_ID" id="region" class="h-12 w-full rounded-lg border bg-white px-4 text-base font-semibold text-gray-700 outline-none focus:ring-2 border-gray-300 focus:border-orange-500" :disabled="!form.COUNTRY_ID">
              <option :value="null">-- Selecciona una Región --</option>
              <option v-for="region in filteredRegions" :key="region.id" :value="region.id">{{ region.name }}</option>
            </select>
          </div>

          <div>
            <label for="city" class="mb-1 block text-sm font-medium text-gray-700">Ciudad</label>
            <select v-model="form.CITY_ID" id="city" class="h-12 w-full rounded-lg border bg-white px-4 text-base font-semibold text-gray-700 outline-none focus:ring-2 border-gray-300 focus:border-orange-500" :disabled="!form.REGION_ID">
              <option :value="null">-- Selecciona una Ciudad --</option>
              <option v-for="city in filteredCities" :key="city.id" :value="city.id">{{ city.name }}</option>
            </select>
          </div>

          <div class="flex items-center pt-6">
            <input type="checkbox" v-model="form.ACTIVE" id="active" class="h-5 w-5 rounded border-gray-300 text-orange-500 focus:ring-orange-500">
            <label for="active" class="ml-3 text-sm font-medium text-gray-700">Activo</label>
          </div>
        </div>

        <div class="mt-8 flex justify-end gap-4">
          <BaseButton type="button" class="bg-gray-200 hover:bg-gray-300 !text-gray-800" @click="$emit('close')">Cancelar</BaseButton>
          <BaseButton type="submit" class="bg-[#FD8036] hover:bg-orange-600 text-white" :disabled="!isFormValid">{{ isEditing ? 'Guardar Cambios' : 'Crear Empresa' }}</BaseButton>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch, computed } from 'vue';
import api from '@services/api';
import BaseInput from '@components/base/BaseInput.vue';
import BaseButton from '@components/base/BaseButton.vue';

const props = defineProps({
  company: { type: Object, default: null }
});

const emit = defineEmits(['close', 'save']);
const isEditing = ref(false);

// Este es el payload que espera tu validador de Laravel
const form = ref({
  ID: null, NAME: '', ICON_PATH: '', ADDRESS: '', EMAIL: '',
  PHONE_NUMBER: '', NIF: '', ACTIVE: true,
  COMPANY_TYPE_ID: null, COUNTRY_ID: null, REGION_ID: null, CITY_ID: null,
});

const catalogsLoaded = ref(false);
const companyTypes = ref([]);
const allCountries = ref([]);
const allRegions = ref([]);
const allCities = ref([]);

const filteredRegions = ref([]);
const filteredCities = ref([]);

const isFormValid = computed(() => {
  return form.value.NAME &&
         form.value.NIF &&
         form.value.COMPANY_TYPE_ID &&
         form.value.COUNTRY_ID &&
         form.value.REGION_ID &&
         form.value.CITY_ID;
});

const getAuthConfig = () => ({ headers: { 'Authorization': `Bearer ${localStorage.getItem('access_token')}` } });

onMounted(async () => {
  try {
    const [typesRes, countriesRes, regionsRes, citiesRes] = await Promise.all([
      api.get('/company-types', getAuthConfig()),
      api.get('/countries', getAuthConfig()),
      api.get('/regions', getAuthConfig()),
      api.get('/cities', getAuthConfig())
    ]);

    companyTypes.value = typesRes.data;
    allCountries.value = countriesRes.data;
    allRegions.value = regionsRes.data;
    allCities.value = citiesRes.data;

    catalogsLoaded.value = true;
  } catch (error) {
    console.error("Error al cargar catálogos:", error);
  }
});

watch(() => [props.company, catalogsLoaded.value], ([newCompany, loaded]) => {
  if (!loaded) return;

  if (newCompany && newCompany.id) {
    isEditing.value = true;

    // MAPEO CRÍTICO: De camelCase (DTO) a UPPER_SNAKE_CASE (Request Payload)
    form.value = {
      ID: newCompany.id,
      NAME: newCompany.name || '',
      ICON_PATH: newCompany.iconPath || '',
      ADDRESS: newCompany.address || '',
      EMAIL: newCompany.email || '',
      PHONE_NUMBER: newCompany.phoneNumber || '',
      NIF: newCompany.nif || '',
      ACTIVE: newCompany.active ?? true,
      COMPANY_TYPE_ID: newCompany.companyType?.id || null,
      COUNTRY_ID: newCompany.region?.country?.id || null,
      REGION_ID: newCompany.region?.id || null,
      CITY_ID: newCompany.city?.id || null,
    };
  } else {
    isEditing.value = false;
    form.value = {
      ID: null, NAME: '', ICON_PATH: '', ADDRESS: '', EMAIL: '',
      PHONE_NUMBER: '', NIF: '', ACTIVE: true,
      COMPANY_TYPE_ID: null, COUNTRY_ID: null, REGION_ID: null, CITY_ID: null,
    };
  }
}, { immediate: true, deep: true });

watch(() => form.value.COUNTRY_ID, (newCountryId) => {
  if (newCountryId) {
    // Como los catálogos también son DTOs, buscamos dentro de la propiedad 'country.id'
    filteredRegions.value = allRegions.value.filter(r => r.country?.id == newCountryId);
  } else {
    filteredRegions.value = [];
  }
  // Solo reseteamos si el usuario lo ha cambiado a mano, no al cargar datos
  if (form.value.REGION_ID && !filteredRegions.value.find(r => r.id == form.value.REGION_ID)) {
      form.value.REGION_ID = null;
      form.value.CITY_ID = null;
  }
});

watch(() => form.value.REGION_ID, (newRegionId) => {
  if (newRegionId) {
    // Buscamos dentro de la propiedad 'region.id'
    filteredCities.value = allCities.value.filter(c => c.region?.id == newRegionId);
  } else {
    filteredCities.value = [];
  }
  if (form.value.CITY_ID && !filteredCities.value.find(c => c.id == form.value.CITY_ID)) {
      form.value.CITY_ID = null;
  }
});

const handleSubmit = () => {
  emit('save', form.value);
};
</script>

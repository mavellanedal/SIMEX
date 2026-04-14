<template>
  <div class="h-full flex flex-col">

    <header class="mb-4">
      <h1 class="text-3xl font-bold text-gray-800">Empresas</h1>
    </header>

    <div class="mb-6 flex justify-between items-center">
      <div class="w-64">
        <BaseInput 
          v-model="searchQuery" 
          placeholder="Buscar por nombre, NIF..." 
          :icon="'search'"
        />
      </div>
      <BaseButton 
        @click="openModalForCreate"
        class="bg-[#FD8036] hover:bg-orange-600 text-white !py-2 !px-4 !text-sm flex items-center"
      >
        <span class="material-symbols-outlined mr-2 !text-base">add</span>
        Añadir Empresa
      </BaseButton>
    </div>

    <div class="bg-white rounded-xl shadow-lg overflow-hidden">
      <table class="w-full text-left">
<thead class="bg-gray-50 border-b border-gray-200">
  <tr>
    <th class="p-4 font-semibold text-gray-600">Nombre</th>
    <th class="p-4 font-semibold text-gray-600">NIF</th>
    <th class="p-4 font-semibold text-gray-600">País</th>
    <th class="p-4 font-semibold text-gray-600">Región</th>
    <th class="p-4 font-semibold text-gray-600">Ciudad</th>
    <th class="p-4 font-semibold text-gray-600">Dirección</th> <!-- <-- AÑADIR ESTA LÍNEA -->
    <th class="p-4 font-semibold text-gray-600">Teléfono</th>
    <th class="p-4 font-semibold text-gray-600">Tipo</th>
    <th class="p-4 font-semibold text-gray-600">Estado</th>
    <th class="p-4 font-semibold text-gray-600">Acciones</th>
  </tr>
</thead>
        <tbody>
<tr v-for="company in filteredCompanies" :key="company.ID" class="border-b border-gray-100 last:border-b-0">            <td class="p-4">
              <div class="flex items-center gap-3">
                <img :src="company.ICON_PATH || '/placeholder.svg'" class="h-8 w-8 rounded-full object-contain bg-gray-100 p-1">
                <span class="font-semibold text-gray-800">{{ company.NAME }}</span>
              </div>
            </td>
            <td class="p-4 text-gray-600">{{ company.NIF }}</td>
            <td class="p-4 text-gray-600">{{ getCountryName(company.COUNTRY_ID) }}</td>
            <td class="p-4 text-gray-600">{{ company.region?.NAME || 'N/A' }}</td>
            <td class="p-4 text-gray-600">{{ company.city?.NAME || 'N/A' }}</td>
            <td class="p-4 text-gray-600">{{ company.ADDRESS }}</td>
            <td class="p-4 text-gray-600">{{ company.PHONE_NUMBER }}</td>
           <td class="p-4 text-gray-600">{{ company.company_type?.NAME || 'N/A' }}</td>

<!-- Celda para la columna "Estado" -->
<td class="p-4">
  <span 
    v-if="company.ACTIVE" 
    class="px-2 py-1 text-xs font-semibold text-green-800 bg-green-100 rounded-full"
  >
    Activo
  </span>
  <span 
    v-else 
    class="px-2 py-1 text-xs font-semibold text-red-800 bg-red-100 rounded-full"
  >
    Inactivo
  </span>
</td>

<!-- Celda para la columna "Acciones" -->
<td class="p-4">
  <div class="flex gap-2">
    <button @click="openModalForEdit(company)" class="text-blue-600 hover:text-blue-800">
      <span class="material-symbols-outlined">edit</span>
    </button>
    <button @click="deleteCompany(company.ID)" class="text-red-600 hover:text-red-800">
      <span class="material-symbols-outlined">delete</span>
    </button>
  </div>
</td>
          </tr>
        </tbody>
      </table>
    </div>

    <CompanyModal
  v-if="isModalOpen"
  :key="companyToEdit ? companyToEdit.ID : 'new'"
  :company="companyToEdit"
  @close="closeModal"
  @save="saveCompany"
/>

  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import api from '@services/api';
import BaseButton from '@components/base/BaseButton.vue';
import BaseInput from '@components/base/BaseInput.vue';
import CompanyModal from '@components/modals/CompanyModal.vue';


const companies = ref([]);     
const countries = ref([]);      
const searchQuery = ref('');    
const isModalOpen = ref(false); 
const companyToEdit = ref(null);



// Busqueda

const filteredCompanies = computed(() => {
  
  const query = searchQuery.value.toLowerCase().trim();

  if (!query) {
    return companies.value;
  }


  return companies.value.filter ( company => {

    const nameMatch = company.NAME.toLowerCase().includes(query);
    const nifMatch = company.NIF.toLowerCase().includes(query);

    return nameMatch || nifMatch;
  });
  
});


const getAuthConfig = () => {
  const token = localStorage.getItem('access_token');
  return { headers: { 'Authorization': `Bearer ${token}` } };
};


async function loadData() {
  try {
   

    const [companiesRes, countriesRes] = await Promise.all([
      api.get('/companies', getAuthConfig()),
      api.get('/countries', getAuthConfig())
    ]);

    companies.value = companiesRes.data;
    countries.value = countriesRes.data;
  } catch (error) {

    console.error("Error al cargar datos iniciales:", error);

  }
}

function getCountryName(countryId) {

  if (!countryId || !countries.value.length) return 'N/A';

  const country = countries.value.find(c => c.ID == countryId);

  return country ? country.NAME : 'N/A';

}

// onMounted se ejecuta cuando el componente está listo.


onMounted(() => {
  loadData();
});

function openModalForCreate() {
  companyToEdit.value = null; 

  isModalOpen.value = true;
};

function openModalForEdit(company) {
  companyToEdit.value = company; 

  isModalOpen.value = true;
};

function closeModal() {
  isModalOpen.value = false;
};

async function saveCompany(formData) {

  const dataToSend = { ...formData };


  dataToSend.ACTIVE = dataToSend.ACTIVE ? 1 : 0;

  try {

    if (dataToSend.ID) {
      await api.put(`/company/${dataToSend.ID}`, dataToSend, getAuthConfig());
    } else {
      await api.post('/company', dataToSend, getAuthConfig());
    }
    closeModal();
    await loadData(); 

  } catch (error) {
    console.error("Error al guardar la empresa:", error);
  }
};

async function deleteCompany(id) {

  if (confirm('¿Estás seguro de que quieres eliminar esta empresa?')) {

    try {
      await api.delete(`/company/${id}`, getAuthConfig());

      await loadData(); 

    } catch (error) {

      console.error("Error al eliminar la empresa:", error);
    }
  }
};
</script>
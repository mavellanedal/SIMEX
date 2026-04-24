<template>
  <div class="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50">
    <div class="bg-white p-8 rounded-xl shadow-2xl w-full max-w-4xl max-h-[90vh] flex flex-col">
      
      <!-- Paso 1: Selección de Operación -->
      <div v-if="step === 1">
        <h2 class="text-2xl font-bold text-gray-800 mb-6">Seleccionar Operación para DUA</h2>
        
        <div class="mb-4">
          <BaseInput 
            v-model="searchQuery" 
            placeholder="Buscar operación..." 
            :icon="'search'"
          />
        </div>

        <div class="overflow-y-auto flex-1" style="max-height: 60vh;">
          <table class="w-full text-left">
            <thead class="bg-gray-100 sticky top-0">
              <tr>
                <th v-for="header in operationHeaders" :key="header" class="p-4 font-semibold text-gray-600 text-sm">
                  {{ header }}
                </th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="op in filteredOperations" :key="op.id" class="border-b border-gray-100 last:border-b-0 hover:bg-gray-50">
                <td class="p-4 text-sm">{{ op.id }}</td>
                <td class="p-4 text-sm">{{ op.buyer }}</td>
                <td class="p-4 text-sm">{{ op.seller }}</td>
                <td class="p-4 text-sm">{{ op.origin }}</td>
                <td class="p-4 text-sm">{{ op.destination }}</td>
                <td class="p-4 text-sm">{{ op.incoterm }}</td>
                <td class="p-4 text-sm">
                  <button @click="selectOperation(op)" class="text-blue-600 hover:text-blue-800">
                    <span class="material-symbols-outlined">open_in_new</span>
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <div class="mt-8 flex justify-end">
          <BaseButton type="button" @click="closeModal" class="bg-gray-200 hover:bg-gray-300 text-gray-700">
            Cancelar
          </BaseButton>
        </div>
      </div>

      <!-- Paso 2: Formulario de DUA -->
      <div v-if="step === 2">
        <h2 class="text-2xl font-bold text-gray-800 mb-6">Nuevo Documento DUA</h2>
        
        <form @submit.prevent="submitForm">
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <BaseInput v-model="form.origin" label="Origen" placeholder="Placeholder" />
            <BaseInput v-model="form.destination" label="Destino" placeholder="Placeholder" />
            <BaseInput v-model="form.specificDestination" label="Destino Concreto" placeholder="Placeholder" />
            <BaseInput v-model="form.customsAgent" label="Agente Aduanero" placeholder="Placeholder" />
            <BaseInput v-model="form.goodsLocation" label="Localización Mercancía" placeholder="Placeholder" />
            <BaseInput v-model="form.grossWeight" label="Peso Bruto" placeholder="Placeholder" type="number" />
          </div>

          <div class="mt-8 flex justify-between items-center">
            <BaseButton type="button" @click="goBack" class="bg-gray-200 hover:bg-gray-300 text-gray-700">
              Atrás
            </BaseButton>
            <div class="flex gap-4">
              <BaseButton type="button" @click="closeModal" class="bg-gray-200 hover:bg-gray-300 text-gray-700">
                Cancelar
              </BaseButton>
              <BaseButton type="submit" class="bg-[#FD8036] hover:bg-orange-600 text-white">
                Generar Documento
              </BaseButton>
            </div>
          </div>
        </form>
      </div>

    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import BaseInput from '@components/base/BaseInput.vue';
import BaseButton from '@components/base/BaseButton.vue';

const emit = defineEmits(['close', 'save']);

const step = ref(1);
const selectedOperation = ref(null);

// --- Lógica del Paso 1: Selección de Operación ---
const searchQuery = ref('');
const operationHeaders = ['Operación', 'Comprador', 'Vendedor', 'Origen', 'Destino', 'Incoterm', 'Seleccionar'];
const operations = ref([]); // Se cargarán en onMounted

const filteredOperations = computed(() => {
  const query = searchQuery.value.toLowerCase();
  if (!query) return operations.value;
  return operations.value.filter(op => 
    op.id.includes(query) || 
    op.buyer.toLowerCase().includes(query) || 
    op.seller.toLowerCase().includes(query)
  );
});

function selectOperation(operation) {
  selectedOperation.value = operation;
  // Pre-rellenar el formulario con datos de la operación si es necesario
  form.value.origin = operation.origin;
  form.value.destination = operation.destination;
  step.value = 2; // Avanzar al siguiente paso
}

// --- Lógica del Paso 2: Formulario DUA ---
const form = ref({
  origin: '',
  destination: '',
  specificDestination: '',
  customsAgent: '',
  goodsLocation: '',
  grossWeight: null
});

function goBack() {
  step.value = 1;
}

// --- Lógica Común ---
const closeModal = () => {
  emit('close');
};

const submitForm = () => {
  emit('save', { 
    operation: selectedOperation.value, 
    duaData: { ...form.value } 
  });
};

// Cargar datos simulados cuando el componente se monta
onMounted(() => {
  operations.value = [
    { id: '0001', buyer: 'Empresa 1', seller: 'Empresa 2', origin: 'España', destination: 'Japón', incoterm: 'CIF' },
    { id: '0002', buyer: 'TechCorp', seller: 'Global Exports', origin: 'EEUU', destination: 'Alemania', incoterm: 'FOB' },
    { id: '0003', buyer: 'Comercial Sur', seller: 'Asian Imports', origin: 'China', destination: 'España', incoterm: 'CIF' },
    { id: '0004', buyer: 'Empresa 1', seller: 'EuroTrade', origin: 'Francia', destination: 'Italia', incoterm: 'EXW' },
    { id: '0005', buyer: 'Norte S.A.', seller: 'Empresa 2', origin: 'Canadá', destination: 'México', incoterm: 'DDP' },
  ];
});
</script>
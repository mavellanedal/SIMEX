<template>
  <transition name="modal">

    <div v-if="isOpen" class="fixed inset-0 z-50 flex items-center justify-center p-4">
      <div
        class="fixed inset-0 bg-black/40 backdrop-blur-sm transition-opacity"
        @click="onCancel"
      ></div>

      <div class="bg-white rounded-xl shadow-2xl max-w-[95vw] xl:max-w-7xl w-full relative z-10 transform transition-all font-sans flex flex-col">

        <div class="p-6 pb-4">
          <h2 class="text-3xl font-bold text-[#145699] tracking-tight">
            {{ isEditing ? 'Editar operación' : 'Nueva operación' }}
          </h2>
        </div>

        <div class="px-6 flex gap-2 border-b border-gray-100">
          <button
            v-for="tab in tabs"
            :key="tab.id"
            @click="activeTab = tab.id"
            :class="[
              'px-6 py-2.5 font-semibold text-sm rounded-t-lg transition-colors',
              activeTab === tab.id
                ? 'bg-[#29A3E8] text-white'
                : 'bg-gray-100 text-gray-600 hover:bg-gray-200'
            ]"
          >
            {{ tab.name }}
          </button>
        </div>

        <div v-if="isLoadingData" class="flex justify-center items-center py-20 flex-1">
          <span class="material-symbols-outlined animate-spin text-4xl text-[#FD8036]">autorenew</span>
        </div>

        <div v-else class="p-6 flex-1 bg-[#FAFAFA] overflow-visible">
          <form id="operationForm" @submit.prevent="onConfirm" novalidate>

            <div v-show="activeTab === 'informacion'" class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-x-6 gap-y-4">

              <BaseInput v-model="formData.orderReference" id="orderReference" label="Número de referencia" placeholder="Ej: REF-2026-001" required :error="errors.orderReference" />
              <BaseDropdown v-model="formData.importerId" id="importerId" label="Importador" :options="importersExporters" value-key="id" label-key="name" placeholder="Selecciona Importador" required :error="errors.importerId" />
              <BaseInput v-model="formData.pickupData" type="date" id="pickupData" label="Fecha de recogida" required :error="errors.pickupData" />
              <BaseDropdown v-model="formData.incotermId" id="incotermId" label="Incoterm" :options="incoterms" value-key="id" label-key="code" placeholder="Selecciona Incoterm" required :error="errors.incotermId" />
              <BaseInput v-model="formData.etd" type="date" id="etd" label="etd (Fecha de salida barco estimada)" required :error="errors.etd" />
              <BaseDropdown v-model="formData.customsAgentId" id="customsAgentId" label="Agente Aduanero" :options="customsAgents" value-key="id" label-key="fullName" placeholder="Selecciona Agente" :error="errors.customsAgentId" />
              <BaseInput v-model="formData.eta" type="date" id="eta" label="ETA (Fecha llegada estimada)" required :error="errors.eta" />
              <BaseDropdown v-model="formData.documentUserId" id="documentUserId" label="Documentación user" :options="users" value-key="id" label-key="fullName" placeholder="Selecciona Usuario" required :error="errors.documentUserId" />
              <BaseDropdown v-model="formData.exportatorId" id="exportatorId" label="Exportador" :options="importersExporters" value-key="id" label-key="name" placeholder="Selecciona Exportador" required :error="errors.exportatorId" />
              <BaseDropdown v-model="formData.operationUserId" id="operationUserId" label="Operación User" :options="users" value-key="id" label-key="fullName" placeholder="Selecciona Usuario" required :error="errors.operationUserId" />
              <BaseDropdown v-model="formData.salesUserId" id="salesUserId" label="Sales User" :options="users" value-key="id" label-key="fullName" placeholder="Selecciona Usuario" required :error="errors.salesUserId" />

            </div>

            <div v-show="activeTab === 'cargo'" class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-x-6 gap-y-4">

              <BaseInput v-model="formData.containerNumber" id="containerNumber" label="Nº Contenedor" placeholder="Placeholder" :error="errors.containerNumber" />
              <BaseDropdown v-model="formData.containerTypeId" id="containerTypeId" label="Tipo Contenedor" :options="containerTypes" value-key="id" label-key="type" placeholder="Selecciona Tipo" :error="errors.containerTypeId" />
              <BaseInput v-model="formData.hsCode" id="hsCode" label="Código HS" placeholder="Placeholder" :error="errors.hsCode" />
              <BaseDropdown v-model="formData.packageTypeId" id="packageTypeId" label="Tipo de mercancía" :options="filteredPackageTypes" value-key="id" label-key="name" placeholder="Selecciona Tipo" :error="errors.packageTypeId" />
              <BaseInput v-model="formData.packagesNumber" type="number" id="packagesNumber" label="Nº Bultos" placeholder="Placeholder" :error="errors.packagesNumber" />
              <BaseDropdown v-model="formData.packageSubTypeId" id="packageSubTypeId" label="Tipo bultos" :options="filteredPackageSubTypes" value-key="id" label-key="name" placeholder="Selecciona Subtipo" :error="errors.packageSubTypeId" />
              <BaseInput v-model="formData.volume" type="number" step="0.01" id="VOLUME" label="Volumen" placeholder="Placeholder" :error="errors.VOLUME" />
              <BaseInput v-model="formData.kilogram" type="number" step="0.01" id="kilogram" label="Kilos" placeholder="Placeholder" :error="errors.kilogram" />
              <BaseInput v-model="formData.piecesNumber" type="number" id="piecesNumber" label="Nº de piezas" placeholder="Placeholder" :error="errors.piecesNumber" />
              <BaseInput v-model="formData.netWeight" type="number" step="0.01" id="NET_WEIGHT" label="Peso Neto" placeholder="Placeholder" :error="errors.NET_WEIGHT" />

            </div>

            <div v-show="activeTab === 'mbl'" class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-x-6 gap-y-4">

              <BaseInput v-model="formData.mblNumber" id="mblNumber" label="Nº MBL" placeholder="Placeholder" :error="errors.mblNumber" />
              <BaseDropdown v-model="formData.buyerId" id="buyerId" label="Empresa Compradora" :options="importersExporters" value-key="id" label-key="name" placeholder="Selecciona Comprador" :error="errors.buyerId" />
              <BaseDropdown v-model="formData.sellerId" id="sellerId" label="Empresa vendedora" :options="importersExporters" value-key="id" label-key="name" placeholder="Selecciona Vendedor" :error="errors.sellerId" />
              <BaseDropdown v-model="formData.navieraId" id="navieraId" label="Naviera" :options="navieras" value-key="id" label-key="name" placeholder="Selecciona Naviera" :error="errors.navieraId" />
              <BaseInput v-model="formData.cargo" id="CARGO" label="Buque (Cargo)" placeholder="Placeholder" :error="errors.cargo" />
              <BaseDropdown v-model="formData.originPortId" id="originPortId" label="Puerto Origen" :options="ports" value-key="id" label-key="name" placeholder="Selecciona Puerto" :error="errors.originPortId" />
              <BaseInput v-model="formData.orderReference" id="REF_PEDIDO" label="Referencia del pedido" disabled placeholder="Placeholder" />
              <BaseDropdown v-model="formData.destinationPortId" id="destinationPortId" label="Puerto Destino" :options="ports" value-key="id" label-key="name" placeholder="Selecciona Puerto" :error="errors.destinationPortId" />
              <BaseInput type="file" id="UPLOAD_FILES" label="Upload Files" />
              <BaseInput v-model="formData.cargoDescription" id="cargoDescription" label="Descripción de la mercancía" placeholder="Placeholder" :error="errors.CARGO_DESCRIPTION" />

            </div>

            <div v-show="activeTab === 'costes'" class="flex flex-col gap-6">

              <div class="grid grid-cols-1 md:grid-cols-3 gap-6 p-5 bg-white rounded-xl shadow-sm border border-gray-100">
                <BaseInput v-model="formData.totalSale" type="number" step="0.01" id="totalSale" label="Total venta" disabled class="font-bold text-green-700 bg-gray-50" />
                <BaseInput v-model="formData.totalCost" type="number" step="0.01" id="totalCost" label="Total coste" disabled class="font-bold text-red-700 bg-gray-50" />
                <BaseInput v-model="formData.profit" type="number" step="0.01" id="profit" label="Profit" disabled class="font-bold text-blue-700 bg-gray-50" />
              </div>
              <div class="border border-gray-200 rounded-xl bg-white overflow-visible shadow-sm">
                <div class="flex gap-1 border-b border-gray-200 p-2 bg-gray-50 overflow-visible">
                  <button v-for="sc in subCostTabs" :key="sc" type="button"
                    @click="activeSubCostTab = sc"
                    :class="[
                      'px-5 py-2 text-sm font-bold rounded-lg transition-colors whitespace-nowrap',
                      activeSubCostTab === sc ? 'bg-[#29A3E8] text-white shadow-sm' : 'text-gray-500 hover:bg-gray-200 hover:text-gray-700'
                    ]"
                  >
                    {{ sc }}
                  </button>
                </div>

                <div class="p-6 grid grid-cols-1 md:grid-cols-4 gap-x-6 gap-y-4 overflow-visible">
                  <BaseInput v-model="costValues[activeSubCostTab].cost_name" id="cost_name" label="Nombre coste" placeholder="Ej: Flete marítimo" disabled />
                  <BaseDropdown v-model="costValues[activeSubCostTab].currencyTypeId" id="currency" label="Moneda" :options="currencyTypes" value-key="id" label-key="name" placeholder="Moneda" />
                  <BaseDropdown v-model="costValues[activeSubCostTab].sendTypeId" id="tipo_envio" label="Tipo Envio" :options="sendTypes" value-key="id" label-key="name" placeholder="Selecciona..." />

                  <BaseInput v-model="costValues[activeSubCostTab].cost" type="number" id="coste_val" label="Coste" placeholder="0.00" />
                  <BaseInput v-model="costValues[activeSubCostTab].costAmount" type="number" id="cantidad_coste" label="Cantidad" placeholder="1" />
                  <BaseInput v-model="costValues[activeSubCostTab].sale" type="number" id="venta_val" label="Venta" placeholder="0.00" />
                  <BaseInput v-model="costValues[activeSubCostTab].saleAmount" type="number" id="cantidad_venta" label="Cantidad" placeholder="1" />
                </div>
              </div>

            </div>
          </form>
        </div>

        <div class="px-6 py-4 flex justify-end gap-3 bg-white rounded-b-xl border-t border-gray-100">
          <button
            type="button"
            @click="onCancel"
            class="bg-gray-100 hover:bg-gray-200 text-gray-800 font-semibold py-2.5 px-8 rounded-lg transition-colors text-base"
          >
            Cancelar
          </button>
          <button
            type="submit"
            form="operationForm"
            class="bg-[#FD8036] hover:bg-[#145699] text-white font-semibold py-2.5 px-8 rounded-lg transition-colors shadow-sm text-base"
          >
            {{ isEditing ? 'Guardar Cambios' : 'Crear Operación' }}
          </button>
        </div>

      </div>
    </div>
  </transition>
</template>

<script setup lang="ts">
import { ref, reactive, watch, onMounted, computed } from 'vue';
import type { PropType } from 'vue';
import type { Operation } from '@interfaces/operation/operation';
import type { CreateOperation } from '@interfaces/operation/createOperation';

import BaseInput from '@components/base/BaseInput.vue';
import BaseDropdown from '@components/base/BaseDropdown.vue';

import { CatalogService } from '@services/catalog.service';
import { CompanyService } from '@services/company.service';
import { UserService } from '@services/user.service';
import type { Company } from '@interfaces/companies/company';
import type { User } from '@interfaces/user/user';
import type { Incoterm } from '@interfaces/incoterm/incoterm';
import type { Port } from '@interfaces/catalogs/port';
import type { ContainerType } from '@interfaces/catalogs/containerType';
import type { PackageType } from '@interfaces/catalogs/packageType';
import type { CurrencyType } from '@/interfaces/catalogs/currencyTypes';
import type { SendType } from '@/interfaces/costs/sendType';
import type { CostType } from '@/interfaces/costs/costType';

const props = defineProps({
  isOpen: { type: Boolean, required: true },
  operationForEdit: { type: Object as PropType<Operation | null>, default: null },
});

const emit = defineEmits(['cancel', 'confirm']);

const activeTab = ref('informacion');
const tabs = [
  { id: 'informacion', name: 'Información' },
  { id: 'cargo', name: 'Cargo' },
  { id: 'mbl', name: 'MBL' },
  { id: 'costes', name: 'Costes' }
];

const activeSubCostTab = ref('Ocean Freight');
const subCostTabs = ['Ocean Freight', 'Custom Clearence', 'FOB Charges', 'Pick Up Charge'];

const isLoadingData = ref(false);
const errors = ref<Record<string, string>>({});

const importersExporters = ref<Company[]>([]);
const navieras = ref<Company[]>([]);
const users = ref<User[]>([]);
const incoterms = ref<Incoterm[]>([]);
const ports = ref<Port[]>([]);
const containerTypes = ref<ContainerType[]>([]);
const packageTypes = ref<PackageType[]>([]);
const packageSubtypes = ref<PackageType[]>([]);
const customsAgents = ref<User[]>([]);
const currencyTypes = ref<CurrencyType[]>([]);
const costTypes = ref<CostType[]>([]);
const sendTypes = ref<SendType[]>([]);

const isEditing = computed(() => !!props.operationForEdit);

const filteredPackageTypes = computed(() => {
  return packageTypes.value; // en lugar de filtrar aquí, ya vienen bien
});

const filteredPackageSubTypes = computed(() => {
  return packageSubtypes.value; // Se carga de la BD bajo demanda usando un watch
});

const initialFormState: CreateOperation = {
  createUserId: 1,
  orderReference: '',
  importerId: 0,
  pickupData: '',
  incotermId: 0,
  etd: '',
  customsAgentId: 0,
  eta: '',
  exportatorId: 0,
  operationUserId: 0,
  salesUserId: 0,
  documentUserId: 0,
  containerNumber: '',
  containerTypeId: 0,
  hsCode: '',
  packagesNumber: 0,
  packageTypeId: 0,
  packageSubTypeId: 0,
  volume: 0,
  netWeight: 0,
  kilogram: 0,
  piecesNumber: 0,
  mblNumber: '',
  buyerId: 0,
  sellerId: 0,
  navieraId: 0,
  cargo: '',
  originPortId: 0,
  destinationPortId: 0,
  cargoDescription: '',
  totalCost: 0,
  totalSale: 0,
  profit: 0,
  costs: []
};

const formData = reactive<CreateOperation>({ ...initialFormState });

watch(() => formData.packageTypeId, async (newVal) => {
  if (newVal) {
    try {
      packageSubtypes.value = await CatalogService.getPackageSubTypes(newVal);
    } catch {
      packageSubtypes.value = [];
    }
  } else {
    packageSubtypes.value = [];
  }
});

const costValues = reactive<Record<string, { costTypeId: number, cost_name: string, currencyTypeId: number, sendTypeId: number, cost: number, costAmount: number, sale: number, saleAmount: number }>>({
  'Ocean Freight': { costTypeId: 1, cost_name: 'Ocean Freight', currencyTypeId: 0, sendTypeId: 0, cost: 0, costAmount: 1, sale: 0, saleAmount: 1 },
  'Custom Clearence': { costTypeId: 2, cost_name: 'Custom Clearence', currencyTypeId: 0, sendTypeId: 0, cost: 0, costAmount: 1, sale: 0, saleAmount: 1 },
  'FOB Charges': { costTypeId: 3, cost_name: 'FOB Charges', currencyTypeId: 0, sendTypeId: 0, cost: 0, costAmount: 1, sale: 0, saleAmount: 1 },
  'Pick Up Charge': { costTypeId: 4, cost_name: 'Pick Up Charge', currencyTypeId: 0, sendTypeId: 0, cost: 0, costAmount: 1, sale: 0, saleAmount: 1 }
});

watch(costValues, (newVals) => {
  let tc = 0;
  let ts = 0;
  Object.values(newVals).forEach(v => {
    tc += (Number(v.cost) || 0) * (Number(v.costAmount) || 1);
    ts += (Number(v.sale) || 0) * (Number(v.saleAmount) || 1);
  });
  formData.totalCost = tc;
  formData.totalSale = ts;
  formData.profit = ts - tc;
}, { deep: true });

onMounted(async () => {
  isLoadingData.value = true;
  try {
    const [
      impExpData,
      navData,
      usersData,
      incoData,
      portsData,
      containerData,
      packageData,
      customsAgentsData,
      currencyTypesData,
      sendTypesData
    ] = await Promise.all([
      CompanyService.getImportersExportersCompanies(),
      CompanyService.getNavieraCompanies(),
      UserService.getUsers(),
      CatalogService.getIncoterms(),
      CatalogService.getPorts(),
      CatalogService.getContainerTypes(),
      CatalogService.getPackageTypes(),
      UserService.getCustomsAgents(),
      CatalogService.getCurrencyTypes(),
      CatalogService.getSendTypes()
    ]);

    importersExporters.value = impExpData;
    navieras.value = navData;
    incoterms.value = incoData;
    ports.value = portsData;
    containerTypes.value = containerData;
    packageTypes.value = packageData; // packageData ya viene filtrado desde Laravel (parentId = null)
    currencyTypes.value = currencyTypesData;
    sendTypes.value = sendTypesData;

    customsAgents.value = customsAgentsData.map((u: User) => ({
      ...u,
      fullName: `${u.firstName} ${u.lastName}`
    }));

    users.value = usersData.map((u: User) => ({
      ...u,
      fullName: `${u.firstName} ${u.lastName}`
    }));

  } catch (error) {
    console.error("Error cargando catálogos para el modal:", error);
  } finally {
    isLoadingData.value = false;
  }
});


watch(() => props.isOpen, (newVal) => {
  errors.value = {};
  if (newVal) {
    activeTab.value = 'informacion';
    if (props.operationForEdit) {
      Object.assign(formData, {
        orderReference: props.operationForEdit.orderReference,
        importerId: props.operationForEdit.importer?.id || '',
      });
    } else {
      Object.assign(formData, { ...initialFormState });
      formData.importerId = '' as any;
      formData.incotermId = '' as any;
    }
  }
});

function validateForm() {
  errors.value = {};
  let isValid = true;

  if (!formData.orderReference) { errors.value.orderReference = 'La referencia es obligatoria'; isValid = false; }
  if (!formData.importerId) { errors.value.importerId = 'Selecciona un importador'; isValid = false; }
  if (!formData.incotermId) { errors.value.incotermId = 'Selecciona un incoterm'; isValid = false; }

  // Verificar que los datos de coste rellenados tengan moneda y tipo de envío
  Object.values(costValues).forEach((cost) => {
    if (cost.cost > 0 || cost.sale > 0) {
      if (!cost.currencyTypeId || !cost.sendTypeId) {
        isValid = false;
        // Opcional: mostrar error general
      }
    }
  });

  if (!isValid && activeTab.value !== 'informacion') {
    activeTab.value = 'informacion';
  }

  return isValid;
}

function onConfirm() {
  if (!validateForm()) return;

  const costsPayload = Object.values(costValues)
    .filter(cost => cost.cost > 0 || cost.sale > 0)
    .map(cost => ({
      costTypeId: cost.costTypeId,
      currencyTypeId: cost.currencyTypeId,
      sendTypeId: cost.sendTypeId,
      cost: cost.cost,
      costAmount: cost.costAmount,
      sale: cost.sale,
      saleAmount: cost.saleAmount
    }));

  emit('confirm', { ...formData, costs: costsPayload });
}

function onCancel() {
  emit('cancel');
}
</script>

<style scoped>
.modal-enter-active, .modal-leave-active { transition: opacity 0.3s ease; }
.modal-enter-from, .modal-leave-to { opacity: 0; }
</style>

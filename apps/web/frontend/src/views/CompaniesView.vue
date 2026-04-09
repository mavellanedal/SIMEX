<template>
  <div class="h-full flex flex-col">
    <!-- 1. Cabecera: Título y Botón de Añadir -->
    <header class="flex justify-between items-center mb-6">
      <h1 class="text-3xl font-bold text-gray-800">Empresas</h1>
      
      <!-- Botón "Añadir Empresa" con el estilo naranja del Figma -->
      <BaseButton class="bg-[#FD8036] hover:bg-orange-600 text-white">
        <span class="material-symbols-outlined mr-2">add</span>
        Añadir Empresa
      </BaseButton>
    </header>

    <!-- 2. Contenido principal: Lista y Mapa -->
    <main class="flex-1 grid grid-cols-1 lg:grid-cols-3 gap-8 min-h-0">
      
      <!-- Columna Izquierda: Búsqueda y Lista de Empresas -->
      <section class="lg:col-span-1 flex flex-col bg-white rounded-xl p-6 shadow-lg">
        
        <!-- Barra de Búsqueda y Filtro -->
        <div class="flex items-center gap-4 mb-6">
          <div class="relative flex-grow">
            <span class="material-symbols-outlined absolute left-3 top-1/2 -translate-y-1/2 text-gray-400">search</span>
            <input
              type="text"
              placeholder="Buscar empresa"
              class="w-full h-12 pl-10 pr-4 rounded-lg border border-gray-200 bg-gray-50 focus:ring-2 focus:ring-orange-400 focus:border-orange-500 outline-none"
            />
          </div>
          <button class="h-12 w-12 flex-shrink-0 flex items-center justify-center rounded-lg border border-gray-200 bg-gray-50 text-gray-600 hover:bg-gray-100">
            <span class="material-symbols-outlined">filter_list</span>
          </button>
        </div>

        <!-- Lista de Empresas con scroll -->
        <div class="flex-1 overflow-y-auto -mr-2 pr-2 space-y-3">
          <article
            v-for="company in companies"
            :key="company.id"
            @click="selectedCompany = company"
            :class="[
              'p-4 rounded-xl border-2 cursor-pointer transition-all duration-200 flex items-center gap-4',
              selectedCompany?.id === company.id
                ? 'bg-blue-50 border-blue-500 shadow-md'  // Estilo si está seleccionada
                : 'bg-white border-transparent hover:shadow-md' // Estilo normal
            ]"
          >
            <img :src="company.logo" :alt="company.name" class="h-10 w-10 object-contain flex-shrink-0">
            <div class="flex-grow">
              <p class="font-bold text-gray-800">{{ company.name }}</p>
              <p class="text-sm text-gray-500">{{ company.location }}</p>
            </div>
            <button class="text-blue-600 hover:text-blue-800">
              <span class="material-symbols-outlined">edit</span>
            </button>
          </article>
        </div>
      </section>

      <!-- Columna Derecha: Mapa del Mundo -->
      <section class="lg:col-span-2 bg-white rounded-xl p-6 shadow-lg flex items-center justify-center relative overflow-hidden">
        
        <!-- Mapa del Mundo SVG (incluido para que funcione directamente) -->
        <div class="relative w-full max-w-2xl aspect-square">
          <svg viewBox="0 0 1000 1000" class="absolute inset-0 animate-spin-slow">
            <path d="M 500 50 A 450 450 0 1 1 499.9 50" fill="none" stroke="#D1D5DB" stroke-width="2" stroke-dasharray="15 30"/>
            <path d="M 935 325 L 950 300 L 965 325" fill="none" stroke="#D1D5DB" stroke-width="2"/>
          </svg>
          <svg viewBox="0 0 1000 1000" class="absolute inset-0 animate-spin-slower transform scale-75">
            <path d="M 500 125 A 375 375 0 1 0 500.1 125" fill="none" stroke="#E5E7EB" stroke-width="2" stroke-dasharray="15 30"/>
            <path d="M 140 365 L 125 350 L 110 365" fill="none" stroke="#E5E7EB" stroke-width="2"/>
          </svg>
          <img src="https://raw.githubusercontent.com/djaiss/mapsicon/master/continents/png/world-mercator.png" alt="Mapa del mundo" class="w-full h-full object-contain opacity-60 p-24"/>
        </div>
        
        <!-- Pin en el mapa -->
        <div v-if="selectedCompany" class="absolute" style="top: 42%; left: 49%;">
           <div class="relative">
              <div class="absolute bottom-full left-1/2 -translate-x-1/2 mb-3 w-48 bg-white rounded-lg shadow-xl p-3 border text-sm text-left z-10">
                <p class="font-bold text-blue-800">{{ selectedCompany.name }}</p>
                <div class="flex items-center gap-2 mt-1 text-gray-600">
                  <span class="material-symbols-outlined text-base">location_on</span>
                  <span>{{ selectedCompany.sede }}</span>
                </div>
                <div class="flex items-center gap-2 mt-1 text-gray-600">
                  <span class="material-symbols-outlined text-base">local_shipping</span>
                  <span>Envíos activos: {{ selectedCompany.envios }}</span>
                </div>
              </div>
              <div class="w-4 h-4 bg-orange-500 rounded-full mx-auto ring-4 ring-white/50 relative z-0"></div>
           </div>
        </div>
      </section>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import BaseButton from '@components/base/BaseButton.vue'

const companies = ref([
  { id: 1, name: 'DHL Logistics', location: 'Barcelona, España', sede: 'Barcelona', envios: 158, logo: 'https://upload.wikimedia.org/wikipedia/commons/a/a4/DHL_Express_logo.svg' },
  { id: 2, name: 'UPS', location: 'Texas, Estados Unidos', sede: 'Texas', envios: 120, logo: 'https://upload.wikimedia.org/wikipedia/commons/6/6b/United_Parcel_Service_logo_2014.svg' },
  { id: 3, name: 'FedEx', location: 'California, Estados Unidos', sede: 'California', envios: 95, logo: 'https://upload.wikimedia.org/wikipedia/commons/9/9d/FedEx_Express.svg' },
  { id: 4, name: 'XPO', location: 'Paris, Francia', sede: 'Paris', envios: 76, logo: 'https://upload.wikimedia.org/wikipedia/commons/2/2a/XPO_Logistics_logo.svg' },
]);

const selectedCompany = ref(companies.value[0]);
</script>

<style>
/* Añadimos animaciones para las flechas del mapa */
@keyframes spin-slow {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}
@keyframes spin-slower {
  from { transform: rotate(360deg); }
  to { transform: rotate(0deg); }
}
.animate-spin-slow {
  animation: spin-slow 45s linear infinite;
}
.animate-spin-slower {
  animation: spin-slower 60s linear infinite;
}
</style>
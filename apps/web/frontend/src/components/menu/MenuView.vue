<template>
  <div class="flex h-screen w-full bg-gray-50 font-sans">
    <aside class="w-16 bg-[#145699] flex flex-col items-center py-16">
     <nav class="flex flex-col w-full">
      <router-link
        v-for="item in menuItems"
        :key="item.id"
        :to="item.path"  
        v-slot="{ isActive }" 
        :title="item.label"
      >
        <div
          :class="[
            'w-full h-16 flex items-center justify-center transition-colors duration-200',
            isActive ? 'bg-[#FD8036] text-white' : 'text-white hover:bg-white/10'
          ]"
        >
          <span class="material-symbols-outlined text-3xl">{{ item.icon }}</span>
        </div>
      </router-link>
</nav>
    </aside>

    <div class="flex-1 flex flex-col min-w-0">
      <header class="h-16 bg-white border-b border-gray-200 flex items-center justify-between px-6 shrink-0 relative">
        <div class="flex items-center">
          <img
            src="@assets/images/PrimerLogisticsHorizontal.webp"
            alt="Logo Prime Logistics"
            class="h-10 w-auto object-contain"
          >
        </div>

        <div class="flex items-center gap-6">

          <div class="relative">
            <div
              v-if="showNotifications"
              @click="showNotifications = false"
              class="fixed inset-0 z-40"
            ></div>

            <button
              @click="showNotifications = !showNotifications"
              class="relative text-[#145699] hover:text-blue-800 transition focus:outline-none z-50"
            >
              <span class="material-symbols-outlined text-3xl">notifications</span>
              <span
                v-if="hasUnreadNotifications"
                class="absolute top-1 right-1 w-2.5 h-2.5 bg-[#FD8036] rounded-full border border-white"
              ></span>
            </button>

            <div
              v-if="showNotifications"
              class="absolute right-0 mt-3 w-80 bg-white rounded-lg shadow-xl border border-gray-100 z-50 overflow-hidden"
            >
              <div class="bg-gray-50 px-4 py-3 border-b border-gray-100 flex justify-between items-center">
                <h3 class="text-sm font-semibold text-gray-700">Notificaciones</h3>
                <span v-if="hasUnreadNotifications" class="bg-[#FD8036] text-white text-[10px] px-2 py-0.5 rounded-full font-bold">
                  Nuevas
                </span>
              </div>

              <div class="max-h-80 overflow-y-auto">
                <template v-if="activeNotifications.length > 0">
                  <div
                    v-for="notif in activeNotifications"
                    :key="notif.ID"
                    @click="markAsRead(notif)"
                    :class="[
                      'p-4 border-b border-gray-50 transition cursor-pointer relative group',
                      Number(notif.STATE_ID) === 1 ? 'bg-blue-50/50 hover:bg-blue-50' : 'bg-white hover:bg-gray-50'
                    ]"
                  >
                    <div class="flex items-start justify-between pr-6">
                      <p :class="['text-sm', Number(notif.STATE_ID) === 1 ? 'font-bold text-[#145699]' : 'font-semibold text-gray-700']">
                        {{ notif.NAME }}
                      </p>

                      <div class="flex items-center gap-2 absolute right-4 top-4">
                        <div v-if="Number(notif.STATE_ID) === 1" class="w-2 h-2 rounded-full bg-[#FD8036] flex-shrink-0"></div>

                        <button
                          @click.stop="deleteNotification(notif)"
                          class="text-gray-400 hover:text-red-500 opacity-0 group-hover:opacity-100 transition-opacity focus:outline-none"
                          title="Eliminar notificación"
                        >
                          <span class="material-symbols-outlined text-lg leading-none">close</span>
                        </button>
                      </div>
                    </div>
                    <p class="text-xs text-gray-600 mt-1 line-clamp-2 pr-6">{{ notif.DESCRIPTION }}</p>
                  </div>
                </template>

                <div v-else class="p-8 flex flex-col items-center justify-center text-gray-400">
                  <span class="material-symbols-outlined text-5xl mb-2 opacity-50">notifications_paused</span>
                  <p class="text-sm text-center">No tienes ninguna notificación.</p>
                </div>
              </div>
            </div>
          </div>
          <div class="flex items-center gap-2">
            <span class="font-semibold text-gray-800">{{ userName }}</span>
          </div>

          <button
            @click="logout"
            class="text-red-600 hover:text-red-800 transition ml-2"
            title="Cerrar Sesión"
          >
            <span class="material-symbols-outlined text-3xl">logout</span>
          </button>
        </div>
      </header>

      <main class="flex-1 overflow-auto bg-white p-6">
        <router-view></router-view>
      </main>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, computed } from 'vue';
import api from '@services/api';
import router from '@/router';

const activeMenu = ref('home');
const userName = ref('Usuario');
const notifications = ref<any[]>([]);

const showNotifications = ref(false);

<<<<<<< HEAD
=======
// Filtramos las notificaciones para mostrar SOLO las que no tienen logic_remove = 1
>>>>>>> d5a3c4a (Menu with notifications done back and front)
const activeNotifications = computed(() => {
  return notifications.value.filter(notif => Number(notif.LOGIC_REMOVE) !== 1);
});

<<<<<<< HEAD
=======
// El contador de "Nuevas" ahora se basa solo en las notificaciones visibles
>>>>>>> d5a3c4a (Menu with notifications done back and front)
const hasUnreadNotifications = computed(() => {
  return activeNotifications.value.some(notif => Number(notif.STATE_ID) === 1);
});

onMounted(async () => {
  const storedUser = localStorage.getItem('user');
  if (storedUser) {
    try {
      const parsedUser = JSON.parse(storedUser);
      if (parsedUser && parsedUser.nombre) {
        userName.value = parsedUser.nombre;
      }
    } catch (error) {
      console.error('Error parsing user from localStorage:', error);
    }
  }

  await fetchNotifications();
});

async function fetchNotifications() {
  try {
    const token = localStorage.getItem('access_token');
    if (!token) return;

    const response = await api.get('/notifications', {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });

    notifications.value = response.data;
  } catch (error) {
    console.error('Error fetching notifications:', error);
  }
}
<<<<<<< HEAD
xwx
=======

>>>>>>> d5a3c4a (Menu with notifications done back and front)
async function markAsRead(notif: any) {
  if (Number(notif.STATE_ID) === 2) return;

  notif.STATE_ID = 2;
  try {
    const token = localStorage.getItem('access_token');
    await api.put('/notifications/' + notif.ID + '/read', {}, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });
  } catch (error) {
    console.error('Error marking notification as read:', error);
<<<<<<< HEAD
    notif.STATE_ID = 1;xw
=======
    notif.STATE_ID = 1; // Typo corregido aquí
>>>>>>> d5a3c4a (Menu with notifications done back and front)
  }
}

async function deleteNotification(notif: any) {
  if (Number(notif.LOGIC_REMOVE) === 1) return;

  notif.LOGIC_REMOVE = 1;
  try {
    const token = localStorage.getItem('access_token');
    await api.delete('/notifications/' + notif.ID, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });
  } catch (error) {
    console.error('Error deleting notification:', error);
    notif.LOGIC_REMOVE = 0;
  }
}

async function logout() {
  try {
    const token = localStorage.getItem('access_token');

<<<<<<< HEAD
=======
    // Solo intentamos llamar al backend si realmente hay un token
>>>>>>> d5a3c4a (Menu with notifications done back and front)
    if (token) {
      await api.post('/logout', {}, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
    }
  } catch (error) {
    console.error('Logout failed:', error);
  } finally {
    localStorage.clear();

    sessionStorage.clear();

    router.replace('/login');

    window.location.reload();
  }
}

const menuItems = [
  { id: 'dashboard', icon: 'home', label: 'Dashboard', path: '/dashboard' },
  { id: 'documents', icon: 'upload_file', label: 'Subir Documentos', path: '/documents' },
  { id: 'ships', icon: 'directions_boat', label: 'Operaciones Marítimas', path: '/ships' },
  { id: 'companies', icon: 'domain', label: 'Empresas / Almacenes', path: '/companies' },
  { id: 'users', icon: 'manage_accounts', label: 'Ajustes de Usuario', path: '/users' },
  { id: 'support', icon: 'support_agent', label: 'Soporte / Agente', path: '/support' },
];
</script>

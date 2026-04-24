<template>
  <div class="flex h-screen w-full bg-gray-50 font-sans">

    <aside class="w-16 bg-[#145699] flex flex-col items-center py-16 shrink-0">
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
              class="relative text-[#145699] hover:text-blue-800 transition focus:outline-none z-50 flex items-center"
            >
              <span class="material-symbols-outlined text-3xl">notifications</span>
              <span
                v-if="hasUnreadNotifications"
                class="absolute top-0 right-0 w-3 h-3 bg-[#FD8036] rounded-full border-2 border-white"
              ></span>
            </button>

            <div
              v-if="showNotifications"
              class="absolute right-0 mt-3 w-80 bg-white rounded-lg shadow-xl border border-gray-100 z-50 overflow-hidden flex flex-col max-h-[400px]"
            >
              <div class="bg-gray-50 px-4 py-3 border-b border-gray-100 flex justify-between items-center shrink-0">
                <h3 class="text-sm font-semibold text-gray-700">Notificaciones</h3>
                <span v-if="hasUnreadNotifications" class="bg-[#FD8036] text-white text-[10px] px-2 py-0.5 rounded-full font-bold">
                  Nuevas
                </span>
              </div>

              <div class="overflow-y-auto flex-1">
                <template v-if="activeNotifications.length > 0">
                  <div
                    v-for="notif in activeNotifications"
                    :key="notif.id"
                    @click="markAsRead(notif)"
                    :class="[
                      'p-4 border-b border-gray-50 transition cursor-pointer relative group',
                      notif.state.id === 1 ? 'bg-blue-50/50 hover:bg-blue-50' : 'bg-white hover:bg-gray-50'
                    ]"
                  >
                    <div class="flex items-start justify-between pr-6">
                      <p :class="['text-sm', notif.state.id === 1 ? 'font-bold text-[#145699]' : 'font-semibold text-gray-700']">
                        {{ notif.name }}
                      </p>

                      <div class="flex items-center gap-2 absolute right-4 top-4">
                        <div v-if="notif.state.id === 1" class="w-2 h-2 rounded-full bg-[#FD8036] flex-shrink-0"></div>

                        <button
                          @click.stop="deleteNotification(notif)"
                          class="text-gray-400 hover:text-red-500 opacity-0 group-hover:opacity-100 transition-opacity focus:outline-none"
                          title="Eliminar notificación"
                        >
                          <span class="material-symbols-outlined text-lg leading-none">close</span>
                        </button>
                      </div>
                    </div>
                    <p class="text-xs text-gray-600 mt-1 line-clamp-2 pr-6">{{ notif.description }}</p>
                  </div>
                </template>

                <div v-else class="p-8 flex flex-col items-center justify-center text-gray-400">
                  <span class="material-symbols-outlined text-5xl mb-2 opacity-50">notifications_paused</span>
                  <p class="text-sm text-center">No tienes ninguna notificación.</p>
                </div>
              </div>
            </div>
          </div>
          <div class="flex items-center gap-2 border-l pl-6 border-gray-200">
            <span class="font-semibold text-gray-800">{{ userName }}</span>
            <button
              @click="logout"
              class="text-red-500 hover:text-red-700 transition ml-2 flex items-center"
              title="Cerrar Sesión"
            >
              <span class="material-symbols-outlined text-2xl">logout</span>
            </button>
          </div>
        </div>
      </header>

      <main class="flex-1 overflow-auto bg-gray-50 p-6">
        <router-view></router-view>
      </main>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, computed } from 'vue';
import api from '@services/api';
import { useRouter } from 'vue-router';
import type { Notification } from '@/interfaces/notification/notification';

const router = useRouter();
const userName = ref('Usuario');
const notifications = ref<Notification[]>([]);
const showNotifications = ref(false);

// Filtramos las notificaciones para mostrar SOLO las que no tienen logic_remove = 1
const activeNotifications = computed(() => {
  return notifications.value.filter(notif => !notif.logicRemove);
});

// El contador de "Nuevas" ahora se basa solo en las notificaciones visibles no leídas (estado 1)
const hasUnreadNotifications = computed(() => {
  return activeNotifications.value.some(notif => Number(notif.state.id) === 1);
});

onMounted(async () => {
  const storedUser = localStorage.getItem('user');
  if (storedUser) {
    try {
      const parsedUser = JSON.parse(storedUser);
      const name = parsedUser.firstName || parsedUser.first_name || parsedUser.nombre;
      const lastName = parsedUser.lastName || parsedUser.last_name || '';

      if (name) {
        userName.value = `${name} ${lastName}`.trim();
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
      headers: { Authorization: `Bearer ${token}` }
    });

    notifications.value = response.data;
  } catch (error) {
    console.error('Error fetching notifications:', error);
  }
}

async function markAsRead(notif: Notification) {
  if (Number(notif.state.id) === 2) return;

  const previousState = notif.state.id;
  notif.state.id = 2; // Actualización optimista

  try {
    const token = localStorage.getItem('access_token');
    await api.put('/notifications/' + notif.id + '/read', {}, {
      headers: { Authorization: `Bearer ${token}` }
    });
  } catch (error) {
    console.error('Error marking notification as read:', error);
    notif.state.id = previousState; // Revertir si falla
  }
}

async function deleteNotification(notif: Notification) {
  if (notif.logicRemove) return;

  notif.logicRemove = true; // Actualización optimista en UI

  try {
    const token = localStorage.getItem('access_token');
    await api.delete('/notifications/' + notif.id, {
      headers: { Authorization: `Bearer ${token}` }
    });
  } catch (error) {
    console.error('Error deleting notification:', error);
    notif.logicRemove = false; // Revertir si falla
  }
}

async function logout() {
  try {
    const token = localStorage.getItem('access_token');

    // Solo intentamos llamar al backend si realmente hay un token
    if (token) {
      await api.post('/logout', {}, {
        headers: { Authorization: `Bearer ${token}` }
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

const allMenuItems = [
  { id: 'dashboard', icon: 'home', label: 'Dashboard', path: '/dashboard', routeName: 'dashboard' },
  { id: 'documents', icon: 'upload_file', label: 'Subir Documentos', path: '/documents', routeName: 'documents' },
  { id: 'ships', icon: 'directions_boat', label: 'Operaciones Marítimas', path: '/operations', routeName: 'operations' },
  { id: 'companies', icon: 'domain', label: 'Empresas / Almacenes', path: '/companies', routeName: 'companies' },
  { id: 'users', icon: 'manage_accounts', label: 'Ajustes de Usuario', path: '/users', routeName: 'users' },
  { id: 'support', icon: 'support_agent', label: 'Soporte / Agente', path: '/support', routeName: 'support' },
];

const menuItems = computed(() => {
  const userStr = localStorage.getItem('user');
  let roleId = 0;
  if (userStr) roleId = JSON.parse(userStr).role?.id || 0;

   return allMenuItems.filter(item => {
    if (roleId === 1) return true;
    if (roleId === 2) return item.id === 'dashboard' || item.id === 'ships';
    if (roleId === 3) return item.id !== 'users' && item.id !== 'support';
    if (roleId === 4) return item.id === 'dashboard' || item.id === 'companies';
    if (roleId === 5) return true;
    return false;
  });
});
</script>

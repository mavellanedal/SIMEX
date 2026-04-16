import api from '@services/api';
import type { CreateUser } from '@/interfaces/user/createUser';

const getAuthHeaders = () => {
  const token = localStorage.getItem('access_token');
  return { headers: { Authorization: `Bearer ${token}` } };
}

export const UserService = {
  async getUsers() {
    const response = await api.get('/users', getAuthHeaders());
    return response.data.data || response.data;
  },

  async getUserById(id: number) {
    const response = await api.get(`/user/${id}`, getAuthHeaders());
    return response.data.data || response.data;
  },

  async createUser(user: CreateUser) {
    const response = await api.post('/user', user, getAuthHeaders());
    return response.data.data || response.data;
  },

  async updateUser(id: number, user: CreateUser) {
    const response = await api.put(`/user/${id}`, user, getAuthHeaders());
    return response.data.data || response.data;
  },

  async deleteUsers(ids: number[]) {
    const response = await api.post('/users/bulk-delete', { ids }, getAuthHeaders());
    return response.data.data || response.data;
  },

  async getCustomsAgents() {
    const response = await api.get('/users/customs-agents', getAuthHeaders());
    return response.data.data || response.data;
  }
}

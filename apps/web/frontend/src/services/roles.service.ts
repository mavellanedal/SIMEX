import api from '@services/api';

const getAuthHeaders = () => {
  const token = localStorage.getItem('access_token');
  return { headers: { Authorization: `Bearer ${token}` } };
}

export const RoleService = {
  async getRoles() {
    const response = await api.get('/roles', getAuthHeaders());
    return response.data.data || response.data;
  }
}

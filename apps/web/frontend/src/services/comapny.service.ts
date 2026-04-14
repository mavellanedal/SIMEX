import api from '@services/api'

const getAuthHeaders = () => {
  const token = localStorage.getItem('access_token');
  return { headers: { Authorization: `Bearer ${token}` } };
}

export const CompanyService = {

  async getCompanies() {
    const response = await api.get('/companies', getAuthHeaders());
    return response.data.data || response.data;
  }
}

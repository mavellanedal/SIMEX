import api from '@services/api'

const getAuthHeaders = () => {
  const token = localStorage.getItem('access_token');
  return { headers: { Authorization: `Bearer ${token}` } };
}

export const CompanyService = {

  async getCompanies() {
    const response = await api.get('/companies', getAuthHeaders());
    return response.data.data || response.data;
  },

  async getImportersExportersCompanies() {
    const response = await api.get('/company/getImportersExportersCompanies', getAuthHeaders());
    return response.data.data || response.data;
  },

  async getNavieraCompanies() {
    const response = await api.get('/company/getNavieraCompanies', getAuthHeaders());
    return response.data.data || response.data;
  }
}

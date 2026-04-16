import api from '@services/api'

const getAuthHeaders = () => {
  const token = localStorage.getItem('access_token');
  return { headers: { Authorization: `Bearer ${token}` } };
}

export const CatalogService = {

  async getCompanyTypes() {
    const response = await api.get('/operations', getAuthHeaders());
    return response.data.data || response.data;
  },

  async getIncoterms() {
    const response = await api.get('/incoterms', getAuthHeaders());
    return response.data.data || response.data;
  },

  async getContainerTypes() {
    const response = await api.get('/container-types', getAuthHeaders());
    return response.data.data || response.data;
  },

  async getPackageTypes() {
    const response = await api.get('/package-types', getAuthHeaders());
    return response.data.data || response.data;
  },

  async getPackageSubTypes(packageTypeId: number) {
    const response = await api.get('/package-sub-types/' + packageTypeId, getAuthHeaders());
    return response.data.data || response.data;
  },

  async getPorts() {
    const response = await api.get('/ports', getAuthHeaders());
    return response.data.data || response.data;
  },

  async getCurrencyTypes() {
    const response = await api.get('/currency-types', getAuthHeaders());
    return response.data.data || response.data;
  },

  async getSendTypes() {
    const response = await api.get('/send-types', getAuthHeaders());
    return response.data.data || response.data;
  }
}

import api from '@services/api'
import type { Operation } from '@interfaces/operation/operation';

const getAuthHeaders = () => {
  const token = localStorage.getItem('access_token');
  return { headers: { Authorization: `Bearer ${token}` } };
}

export const OperationService = {

  async getOperations() {
    const response = await api.get('/operations', getAuthHeaders());
    return response.data.data || response.data;
  },

  async getOperation(id: number) {
    const response = await api.get(`/operations/${id}`, getAuthHeaders());
    return response.data.data || response.data;
  },

  async createOperation(operationData: Operation) {
    const response = await api.post('/operations', operationData, getAuthHeaders());
    return response.data.data || response.data;
  },

  async updateOperation(id: number, operationData: Operation) {
    const response = await api.put(`/operations/${id}`, operationData, getAuthHeaders());
    return response.data.data || response.data;
  },

  async changeOperationState(id: number, newStateId: number) {
    const response = await api.put(`/operations/${id}/state`, { newState: newStateId }, getAuthHeaders());
    return response.data.data || response.data;
  }
}

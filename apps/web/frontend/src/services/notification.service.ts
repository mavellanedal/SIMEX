import api from './api';
import type { Notification } from '@interfaces/notification/notification';

const getAuthHeaders = () => {
  const token = localStorage.getItem('access_token');
  return { headers: { Authorization: `Bearer ${token}` } };
}

const NotificationService = {
  async getNotifications(): Promise<Notification[]> {
    const response = await api.get<Notification[]>('/notifications', getAuthHeaders());
    return response.data;
  },

  async markAsRead(id: number): Promise<void> {
    await api.put(`/notifications/${id}/read`, {}, getAuthHeaders());
  },

  async deleteNotification(id: number): Promise<void> {
    await api.delete(`/notifications/${id}`, getAuthHeaders());
  }
};

export default NotificationService;

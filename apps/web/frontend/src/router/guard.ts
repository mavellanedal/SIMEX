import type { RouteLocationNormalized } from "vue-router";

export function authGuard(to: RouteLocationNormalized, from: RouteLocationNormalized) {
  const isAuthenticated = !!localStorage.getItem('access_token');
  const userStr = localStorage.getItem('user');
  let roleId = 0;

  if (userStr) {
    try {
      const user = JSON.parse(userStr);
      roleId = user.role?.id || 0;
    } catch (error) {
      console.error('Error parsing user from localStorage:', error);
    }
  }
  if (to.meta.requiresAuth && !isAuthenticated) {
    return { name: 'login' };
  }

  if (to.meta.requiresGuest && isAuthenticated) {
    return { name: 'dashboard' };
  }

  if (isAuthenticated && to.meta.requiresAuth) {
    const routeName = to.name as string;

    if (roleId === 2 && (routeName === 'users' || routeName === 'companies')) {
      return { name: 'dashboard' };
    }

    if (roleId === 3 && (routeName === 'users' || routeName === 'support')) {
      return { name: 'dashboard' };
    }

    if (roleId === 4 && routeName !== 'dashboard' && routeName !== 'companies') {
      return { name: 'dashboard' };
    }
  }

  return true;
}

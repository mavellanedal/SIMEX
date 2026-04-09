import type { RouteLocationNormalized } from "vue-router";

export function authGuard(to: RouteLocationNormalized, from: RouteLocationNormalized) {
  const isAuthenticated = !!localStorage.getItem('access_token');

  if (to.meta.requiresAuth && !isAuthenticated) {
    return { name: 'login' };
  }

  if (to.meta.requiresGuest && isAuthenticated) {
    return { name: 'dashboard' };
  }

  return true;
}

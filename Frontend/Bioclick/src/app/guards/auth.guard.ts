import { inject } from '@angular/core';
import { CanActivateFn, Router, ActivatedRouteSnapshot, UrlTree } from '@angular/router';

export const authGuard: CanActivateFn = (
  route: ActivatedRouteSnapshot
): boolean | UrlTree => {
  const router = inject(Router);
  const token = localStorage.getItem('token');
  if (!token) {
    return router.createUrlTree(['/register']);
  }
  const expectedRoles = route.data['roles'] as string[] | undefined;
  if (expectedRoles && expectedRoles.length > 0) {
    const userRole = localStorage.getItem('role');
    if (!userRole || !expectedRoles.includes(userRole)) {
      return router.createUrlTree(['/home']);
    }
  }
  return true;
};
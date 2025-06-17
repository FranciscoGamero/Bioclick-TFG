import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { DetailUser } from '../../models/user/detail-user.interface';
import { Router } from '@angular/router';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent implements OnInit {
  user: DetailUser | undefined;
  constructor(private userService: UserService, private router: Router) {
    this.user = undefined;
  }

  ngOnInit(): void {
    this.userService.getMe().subscribe(user => {
      this.user = user;
    });
  }
  haveAccess(role: string): boolean {
    if (role === 'ROLE_ADMIN' || role === 'ROLE_MANAGER') {
      return true;
    }
    return false;
  }
  limpiarUrlFoto(url: string | undefined | null): string {
    if (!url) return '';

    if (url.includes('randomuser.me')) {
      return url.replace(`${environment.apiBaseUrl}/download/`, '');
    }

    if (url.startsWith('http')) return url;

    return `${environment.apiBaseUrl}/download/${url}`;
  }
  toPanels() {
    this.router.navigate(['/all-list']);
  }
  logout() {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }
}

import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { DetailUser } from '../../models/user/detail-user.interface';
import { Router } from '@angular/router';

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
      return url.replace('http://localhost:8080/download/', '');
    }

    if (url.startsWith('http')) return url;

    return `http://localhost:8080/download/${url}`;
  }
  toPanels() {
    this.router.navigate(['/user-list']);
  }
  logout() {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }
}

import { Component } from '@angular/core';
import { LoginService } from '../../services/login.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-LoginForm',
  templateUrl: './LoginForm.component.html',
  styleUrls: ['./LoginForm.component.css']
})
export class LoginformComponent {
  randomBgNumber = Math.floor(Math.random() * 4) + 1;

  username: string = '';
  password: string = '';

  constructor(private loginService: LoginService, private router: Router) { }
  loginUser(): void {
    this.loginService.loginUser(this.username, this.password).subscribe({
      next: (response) => {
        localStorage.setItem('token', response.token);
        localStorage.setItem('role', response.role);
        if (response.role === 'ROLE_ADMIN') {
          this.router.navigate(['/admin-list']);
        } else if (response.role === 'ROLE_MANAGER') {
          this.router.navigate(['/products-list']);
        }
      },
      error: (error) => {
        console.error('Error en el login', error);
      }
    });
  }
}
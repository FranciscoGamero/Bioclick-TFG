import { Component } from '@angular/core';
import { LoginService } from '../../services/login.service';

@Component({
  selector: 'app-LoginForm',
  templateUrl: './LoginForm.component.html',
  styleUrls: ['./LoginForm.component.css']
})
export class LoginformComponent {
  randomBgNumber = Math.floor(Math.random() * 4) + 1;

  username: string = '';
  password: string = '';

  constructor(private loginService: LoginService) { }
  loginUser(): void {
    this.loginService.loginUser(this.username, this.password).subscribe({
      next: (response) => {
        localStorage.setItem('token', response.token);
      },
      error: (error) => {
        console.error('Error en el login', error);
      }
    });
  }
}
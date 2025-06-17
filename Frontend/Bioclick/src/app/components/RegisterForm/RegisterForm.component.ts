import { Component, OnInit } from '@angular/core';
import { RegisterService } from '../../services/register.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-RegisterForm',
  templateUrl: './RegisterForm.component.html',
  styleUrl: './RegisterForm.component.css'
})
export class RegisterFormComponent implements OnInit {
  randomBgNumber = Math.floor(Math.random() * 4) + 1;

  username = 'prueba'
  email = 'emailprueba@gmail.com'
  password = 'aA12345678'
  confirmPassword = 'aA12345678'

  selectedFile: File | null = null;
  alertMessage: string | null = null;


  constructor(private registerService: RegisterService, private router: Router) { }

  ngOnInit() {

    fetch('assets/images/perfil-de-usuario.jpg')
      .then(res => res.blob())
      .then(blob => {
        this.selectedFile = new File([blob], 'perfil-de-usuario.jpg', { type: 'image/jpg' });
      })
      .catch(err => {
        console.error('No se pudo cargar la imagen por defecto', err);
        this.selectedFile = null;
      });
  }

  onFileSelected(event: any) {
    if (event && event.target.files.length > 0) {
      this.selectedFile = event.target.files[0];
    }
  }
  registerUser() {
    this.alertMessage = null;

    if (!this.username || !this.email || !this.password || !this.confirmPassword) {
      this.alertMessage = 'Todos los campos son obligatorios.';
      return;
    }
    if (!this.isEmailValid(this.email)) {
      this.alertMessage = 'El email no es válido.';
      return;
    }
    if (this.password.length < 8) {
      this.alertMessage = 'La contraseña debe tener al menos 8 caracteres.';
      return;
    }
    if (this.password !== this.confirmPassword) {
      this.alertMessage = 'Las contraseñas no coinciden.';
      return;
    }
    if (!this.selectedFile) {
      this.alertMessage = 'Debes seleccionar una foto de perfil.';
      return;
    }

    this.registerService.registerUser(this.username, this.email, this.password, this.confirmPassword, this.selectedFile)
      .subscribe({
        next: () => {
          this.router.navigate(['/verify']);
        },
        error: () => {
          this.alertMessage = 'Error en el registro. Inténtalo de nuevo.';
        }
      });
  }
  isEmailValid(email: string): boolean {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
  }
  goToLogin() {
  this.router.navigate(['/login']);
}
}

import { Component, OnInit } from '@angular/core';
import { RegisterService } from '../../services/register.service';

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
  isMobile: boolean = false;


  constructor(private registerService: RegisterService) { }

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
    if (this.selectedFile) {
      this.registerService.registerUser(this.username, this.email, this.password, this.confirmPassword, this.selectedFile)
        .subscribe({
          next: (response) => {
            console.log('Registration successful', response);
          },
          error: (error) => {
            console.error('Registration failed', error);
          }
        });
    }
  }
}

import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class RegisterService {

  constructor(private http: HttpClient) { }

  registerUser(username: string, email: string, password: string, verifyPassword: string, file: File) {
    const url = `${environment.apiBaseUrl}/register`;
    const formData = new FormData();
    
    const userData = {
      username: username,
      password: password,
      verifyPassword: verifyPassword,
      correo: email,
    };

    formData.append('file', file);
    formData.append('register', new Blob([JSON.stringify(userData)], { type: 'application/json' }));

    return this.http.post(url, formData);
  }
}

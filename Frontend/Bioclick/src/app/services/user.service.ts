import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';
import { AllUsersResponse } from '../models/user/get-all-users-interface';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  getAllUsers(page: number): Observable<AllUsersResponse> {
    const url = `${environment.apiBaseUrl}/admin/get/users`;
    const header = {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    }
    const params = new HttpParams()
      .set('page', page)
    return this.http.get<AllUsersResponse>(url, { headers: header, params });
  }

  async updateUser(userId: string, username: string, correo: string, password: string, file: File | null,
    fotoPerfilUrl: string
  ): Promise<Observable<any>> {
    const url = `${environment.apiBaseUrl}/admin/edit/user/${userId}`;
    const formData = new FormData();
    const editData = { username, correo, password };

    let fileToSend = file;

    // Si no hay archivo nuevo, intenta descargar la imagen actual
    if (!fileToSend && fotoPerfilUrl) {
      try {
        const response = await fetch(fotoPerfilUrl);
        if (!response.ok) throw new Error('No se pudo descargar la imagen');
        const blob = await response.blob();
        fileToSend = new File([blob], 'profile.jpg', { type: blob.type });
      } catch (e) {
        alert('No se puede mantener la imagen actual porque está alojada en un servidor externo. Por favor, selecciona una nueva foto de perfil.');
        throw e; // Detén el proceso, el usuario debe subir una imagen
      }
    }

    formData.append('file', fileToSend!);

    formData.append('edit', new Blob([JSON.stringify(editData)], { type: 'application/json' }));
    console.log('FormData:', formData);
    const header = {
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    };

    return this.http.put(url, formData, { headers: header });
  }
}

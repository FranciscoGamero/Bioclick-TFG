import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';
import { AllAdminsResponse } from '../models/user/get-all-admins-interface';
import { AllFoundResponse } from '../models/user/get-all-found';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  constructor(private http: HttpClient) { }

  getAllAdmins(page: number): Observable<AllAdminsResponse> {
    const url = `${environment.apiBaseUrl}/admin/get/admins`;
    const header = {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    }
    const params = new HttpParams()
      .set('page', page)
    return this.http.get<AllAdminsResponse>(url, { headers: header, params });
  }
  getAll(page: number): Observable<AllFoundResponse> {
    const url = `${environment.apiBaseUrl}/admin/get/all`;
    const header = {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    }
    const params = new HttpParams()
      .set('page', page)
    return this.http.get<AllFoundResponse>(url, { headers: header, params });
  }
  async updateManager(
    managerId: string,
    username: string,
    correo: string,
    password: string,
    file: File | null,
    fotoPerfilUrl: string
  ): Promise<Observable<any>> {
    const url = `${environment.apiBaseUrl}/admin/edit/manager/${managerId}`;
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

    // Siempre añade el archivo (el backend lo exige)
    formData.append('file', fileToSend!);

    formData.append('editar', new Blob([JSON.stringify(editData)], { type: 'application/json' }));

    const header = {
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    };

    return this.http.put(url, formData, { headers: header });
  }
}

import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';
import { AllAdminsResponse } from '../models/user/get-all-admins-interface';
import { AllFoundResponse } from '../models/user/get-all-found';
import { ProductsMoreValoratedResponse } from '../models/user/graphics/products-more-valorated.interface';
import { UsersMoreValorationsResponse } from '../models/user/graphics/users-with-more-valorations.interface';
import { AllUsersResponse } from '../models/user/get-all-users-interface';

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
  createManager(username: string, correo: string, password: string, verifyPassword: string, file: File): Observable<any> {
    const url = `${environment.apiBaseUrl}/manager/create`;
    const formData = new FormData();
    const crearData = { username, correo, password, verifyPassword };

    formData.append('file', file);
    formData.append('crear', new Blob([JSON.stringify(crearData)], { type: 'application/json' }));

    const header = {
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    };

    return this.http.post(url, formData, { headers: header });
  }
  createAdmin(username: string, correo: string, password: string, verifyPassword: string, file: File): Observable<any> {
    const url = `${environment.apiBaseUrl}/admin/create`;
    const formData = new FormData();
    const crearData = { username, correo, password, verifyPassword };

    formData.append('file', file);
    formData.append('crear', new Blob([JSON.stringify(crearData)], { type: 'application/json' }));

    const header = {
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    };

    return this.http.post(url, formData, { headers: header });
  }
  async updateManager(managerId: string, username: string, correo: string, password: string, file: File | null,
    fotoPerfilUrl: string
  ): Promise<Observable<any>> {
    const url = `${environment.apiBaseUrl}/admin/edit/manager/${managerId}`;
    const formData = new FormData();
    const editData = { username, correo, password };

    let fileToSend = file;

    if (!fileToSend && fotoPerfilUrl) {
      try {
        const response = await fetch(fotoPerfilUrl);
        if (!response.ok) throw new Error('No se pudo descargar la imagen');
        const blob = await response.blob();
        fileToSend = new File([blob], 'profile.jpg', { type: blob.type });
      } catch (e) {
        alert('No se puede mantener la imagen actual porque está alojada en un servidor externo. Por favor, selecciona una nueva foto de perfil.');
        throw e;
      }
    }
    formData.append('file', fileToSend!);

    formData.append('editar', new Blob([JSON.stringify(editData)], { type: 'application/json' }));

    const header = {
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    };

    return this.http.put(url, formData, { headers: header });
  }

  deleteManager(managerId: string): Observable<any> {
    const url = `${environment.apiBaseUrl}/admin/delete/manager/${managerId}`;
    const header = {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    }
    return this.http.delete(url, { headers: header });
  }

  getTotalProducts(): Observable<number> {
    const url = `${environment.apiBaseUrl}/admin/graphics/total-products`;
    const header = {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    }
    return this.http.get<number>(url, { headers: header });
  }
  getTotalC02(): Observable<number> {
    const url = `${environment.apiBaseUrl}/admin/graphics/total-c02`;
    const header = {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    }
    return this.http.get<number>(url, { headers: header });
  }
  getTotalUsersValidated(): Observable<number> {
    const url = `${environment.apiBaseUrl}/admin/graphics/users-validated`;
    const header = {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    }
    return this.http.get<number>(url, { headers: header });
  }
  getProductsMoreValorated(): Observable<ProductsMoreValoratedResponse> {
    const url = `${environment.apiBaseUrl}/admin/graphics/products-more-valorated`;
    const header = {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    }
    return this.http.get<ProductsMoreValoratedResponse>(url, { headers: header });
  }
  getUsersWithMoreValorations(): Observable<UsersMoreValorationsResponse> {
    const url = `${environment.apiBaseUrl}/admin/graphics/users-with-more-valorations`;
    const header = {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    }
    return this.http.get<UsersMoreValorationsResponse>(url, { headers: header });
  }
    getUsersByName(name: string, page: number, size: number): Observable<AllUsersResponse> {
      const url = `${environment.apiBaseUrl}/admin/get/users-by-name`;
      const headers = {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      };
  
      const params = new HttpParams()
        .set('nombre', name)
        .set('page', page)
        .set('size', size);

        console.log(url, params.toString());
      return this.http.get<AllUsersResponse>(url, { headers, params });
    } 
}

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
    
    formData.append('file', fileToSend!);

    formData.append('edit', new Blob([JSON.stringify(editData)], { type: 'application/json' }));
    console.log('FormData:', formData);
    const header = {
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    };

    return this.http.put(url, formData, { headers: header });
  }
  deleteUser(userId: string): Observable<any> {
    const url = `${environment.apiBaseUrl}/user/delete/${userId}`;
    const header = {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    }
    console.log(url, header)
    return this.http.delete(url, { headers: header });
  }
}

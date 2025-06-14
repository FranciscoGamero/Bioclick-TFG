import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';
import { AllUsersResponse } from '../models/user/get-all-users-interface';
import { DetailUser } from '../models/user/detail-user.interface';
import { Comentario } from '../models/user/get-all-comments';

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
    return this.http.delete(url, { headers: header });
  }
  verifyUser(codigo: string): Observable<DetailUser> {
    const url = `${environment.apiBaseUrl}/auth/verify`;
    const header = {
      'Content-Type': 'application/json',
    }
    const body = {
      code: codigo
    }
    return this.http.post<DetailUser>(url, body, { headers: header });
  }
  getMe(): Observable<DetailUser> {
    const url = `${environment.apiBaseUrl}/get/me`;
    const header = {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    }
    return this.http.get<DetailUser>(url, { headers: header });
  }
  getUser(userId: string): Observable<DetailUser> {
    const url = `${environment.apiBaseUrl}/admin/get/${userId}`;
    const header = {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    }
    return this.http.get<DetailUser>(url, { headers: header });
  }
  async editMe(username: string, correo: string, password: string, file: File | null): Promise<Observable<DetailUser>> {
    const url = `${environment.apiBaseUrl}/edit/me`;
    const formData = new FormData();
    const editData = { username, correo, password };


    if (file instanceof File) {
      formData.append('file', file, file.name);
    } else {
      formData.append('file', new Blob([], { type: 'application/octet-stream' }), '');
    }
    formData.append('edit', new Blob([JSON.stringify(editData)], { type: 'application/json' }));

    const header = {
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    };

    return this.http.put<DetailUser>(url, formData, { headers: header });
  }

  addComment(userId: string, comment: string, productoId: string): Observable<Comentario> {
    const url = `${environment.apiBaseUrl}/comment/add`;
    const header = {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    }
    const body = {
      comentario: comment,
      userId: userId,
      productoId: productoId
    }
    return this.http.post<Comentario>(url, body, { headers: header });
  }
  editComment(commentId: string, comment: string): Observable<Comentario> {
    const url = `${environment.apiBaseUrl}/comment/edit/${commentId}`;
    const header = {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    }
    const body = {
      comentarioCambiado: comment
    }
    return this.http.put<Comentario>(url, body, { headers: header });
  }
  deleteComment(commentId: string): Observable<any> {
    const url = `${environment.apiBaseUrl}/comment/delete/${commentId}`;
    const header = {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    }
    return this.http.delete(url, { headers: header });
  }
}

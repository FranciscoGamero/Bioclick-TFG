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
}

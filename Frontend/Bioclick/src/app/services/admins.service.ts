import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';
import { AllAdminsResponse } from '../models/user/get-all-admins-interface';

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
}


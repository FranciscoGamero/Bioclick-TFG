import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';
import { AllManagersResponse } from '../models/user/get-all-managers-interface';

@Injectable({
  providedIn: 'root'
})
export class ManagerService {

  constructor(private http: HttpClient) { }

  getAllManagers(page: number): Observable<AllManagersResponse> {
    const url = `${environment.apiBaseUrl}/admin/get/managers`;
    const header = {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    }
    const params = new HttpParams()
      .set('page', page)
    return this.http.get<AllManagersResponse>(url, { headers: header, params });
  }
}

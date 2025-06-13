import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';
import { AllValorationsResponse } from '../models/user/get-all-valorations';
import { Valoration } from '../models/user/get-all-valorations';

@Injectable({
  providedIn: 'root'
})
export class ValorationsService {

  constructor(private http: HttpClient) { }

  addValoration(puntuacion: number, user_id: string, producto_id: string): Observable<Valoration> {
    const url = `${environment.apiBaseUrl}/valoration/create`;
    const headers = {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    };
    const data = {
      puntuacion,
      user_id,
      producto_id
    };
    return this.http.post<Valoration>(url, data, { headers });
  }
  
  deleteValoration(valoracionId: string): Observable<any> {
    const url = `${environment.apiBaseUrl}/valoration/delete/${valoracionId}`;
    const headers = {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    };
    return this.http.delete(url, { headers });
  }

  getValorationsByProduct(productId: string, page: number = 0): Observable<AllValorationsResponse> {
    const url = `${environment.apiBaseUrl}/valoration/get/${productId}`;
    const headers = {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    };
    const params = { page };
    return this.http.get<AllValorationsResponse>(url, { headers, params });
  }

  getMyValorations(page: number = 0): Observable<AllValorationsResponse> {
    const url = `${environment.apiBaseUrl}/valoration/get/me`;
    const headers = {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    };
    const params = { page };
    return this.http.get<AllValorationsResponse>(url, { headers, params });
  }
}
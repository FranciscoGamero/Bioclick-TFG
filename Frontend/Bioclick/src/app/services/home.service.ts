import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AllProductsResponse } from '../models/user/get-all-products.interface';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class HomeService {

  constructor(private http: HttpClient) { }

  getMoreLiked(page: number, size: number): Observable<AllProductsResponse> {
    const url = `${environment.apiBaseUrl}/product/get/more-liked`;
    const headers = {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    };

    const params = new HttpParams()
      .set('page', page)
      .set('size', size);

    return this.http.get<AllProductsResponse>(url, { headers, params });
  }

  getMoreValorated(page: number, size: number): Observable<AllProductsResponse> {
    const url = `${environment.apiBaseUrl}/product/get/more-valorated`;
    const headers = {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    };

    const params = new HttpParams()
      .set('page', page)
      .set('size', size);

    return this.http.get<AllProductsResponse>(url, { headers, params });
  }
}

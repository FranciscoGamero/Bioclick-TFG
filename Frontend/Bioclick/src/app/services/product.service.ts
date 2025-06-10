import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../environments/environment';
import { AllProductsResponse } from '../models/user/get-all-products.interface';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  constructor(private http: HttpClient) { }

  getAllProducts(page: number): Observable<AllProductsResponse> {
    const url = `${environment.apiBaseUrl}/product/get/all`;
    const header = {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    }
    const params = new HttpParams()
      .set('page', page)
    return this.http.get<AllProductsResponse>(url, { headers: header, params });
  }

}

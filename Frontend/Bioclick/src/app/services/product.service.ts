import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../environments/environment';
import { AllProductsResponse, Producto } from '../models/user/get-all-products.interface';
import { Observable } from 'rxjs';
import { AllCommentsResponse } from '../models/user/get-all-comments';

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
  getProductDetail(id: string): Observable<Producto> {
    const url = `${environment.apiBaseUrl}/product/get/${id}`;
    const header = {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    }
    const params = new HttpParams()
    return this.http.get<Producto>(url, { headers: header, params });
  }
  getAllComments(id: string, page: number): Observable<AllCommentsResponse> {
    const url = `${environment.apiBaseUrl}/comment/product/${id}`;
    const header = {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    }
    const params = new HttpParams()
      .set('page', page)
    return this.http.get<AllCommentsResponse>(url, { headers: header, params });
  }
}

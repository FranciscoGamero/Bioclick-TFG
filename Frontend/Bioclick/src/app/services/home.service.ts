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
  getProductsByName(name: string, page: number, size: number): Observable<AllProductsResponse> {
    const url = `${environment.apiBaseUrl}/productos/get/product-by-name`;
    const headers = {
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    };

    const params = new HttpParams()
      .set('nombre', name)
      .set('page', page)
      .set('size', size);

    return this.http.get<AllProductsResponse>(url, { headers, params });
  }
  getProductosFiltrados(
    categoria: string,
    precioMin: number | null,
    precioMax: number | null,
    page: number,

  ): Observable<AllProductsResponse> {
    const url = `${environment.apiBaseUrl}/product/get-by-filter`;
    const headers = {
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    };
    let params = new HttpParams()
      .set('page', page)
      .set('size', 12);

    if (categoria) params = params.set('nombreCategoria', categoria);
    if (precioMin !== null && precioMin !== undefined) params = params.set('precioMin', precioMin);
    if (precioMax !== null && precioMax !== undefined) params = params.set('precioMax', precioMax);
    return this.http.get<AllProductsResponse>(url, { headers, params });

  }
}

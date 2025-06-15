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
  createProduct(nombreProducto: string, descripcion: string, precioProducto: number,
    fabricante: string, estado: string, nombreCategoria: string, file: File
  ): Observable<any> {
    const url = `${environment.apiBaseUrl}/product/create`;
    const formData = new FormData();
    const crearData = {
      nombreProducto,
      descripcion,
      precioProducto,
      fabricante,
      estado,
      idCategoria: nombreCategoria // <-- usa idCategoria, no nombreCategoria
    };

    formData.append('file', file);
    formData.append('crear', new Blob([JSON.stringify(crearData)], { type: 'application/json' }));

    const headers = {
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    };

    return this.http.post<Producto>(url, formData, { headers });
  }
  editProduct(productId: string, nombreProducto: string, descripcion: string,
    precioProducto: number, estado: string, file: File | null
  ): Observable<Producto> {
    const url = `${environment.apiBaseUrl}/product/edit/${productId}`;
    const formData = new FormData();
    const editData = {
      nombreProducto,
      descripcion,
      precioProducto,
      estado,
      fotoPerfilUrl: file ? file.name : null
    };
    console.log('Editing product with data:', editData);
    if (file) {
      formData.append('file', file);
    }
    formData.append('editar', new Blob([JSON.stringify(editData)], { type: 'application/json' }));

    const headers = {
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    };

    return this.http.put<Producto>(url, formData, { headers });
  }
  deleteProduct(productId: string): Observable<any> {
    const url = `${environment.apiBaseUrl}/product/delete/${productId}`;
    const header = {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    }
    return this.http.delete(url, { headers: header });
  }
}
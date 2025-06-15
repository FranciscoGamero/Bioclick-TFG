import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AllCategoryResponse, Categoria } from '../models/user/get-all-categories';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';
import { CategoriaDialogData } from '../components/Dialog/CommonDialog/category-dialog';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  constructor(private http: HttpClient) { }

  getAllCategories(page: number): Observable<AllCategoryResponse> {
    const url = `${environment.apiBaseUrl}/category/get/all`;
    const header = {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    }
    const params = new HttpParams()
      .set('page', page)
    return this.http.get<AllCategoryResponse>(url, { headers: header, params });
  }
  createCategory(nombreCategoria: string, subcategoriaIds: string[], categoriaPadreId?: string): Observable<Categoria> {
    const url = `${environment.apiBaseUrl}/category/create`;
    const header = {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    }
    const body = {
      nombreCategoria,
      categoriaPadreId,
      subcategoriaIds
    };
    return this.http.post<Categoria>(url, body, { headers: header });
  }
  editCategory(id: string, nombreCategoria: string, subcategoriaIds: string[], categoriaPadreId?: string): Observable<Categoria> {
    const url = `${environment.apiBaseUrl}/category/edit/${id}`;
    const header = {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    }
    const body = {
      nombreCategoria,
      categoriaPadreId,
      subcategoriaIds
    };
    return this.http.put<Categoria>(url, body, { headers: header });
  }
  deleteCategory(id: string): Observable<any> {
    const url = `${environment.apiBaseUrl}/category/delete/${id}`;
    const header = {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    }
    return this.http.delete(url, { headers: header });
  }
}

import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AllCategoryResponse } from '../models/user/get-all-categories';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';

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
}

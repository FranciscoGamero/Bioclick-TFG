import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../environments/environment';
import { Observable } from 'rxjs';
import { AllCategoryResponse } from '../models/user/get-all-categories';
import { AllFavoritesResponse, Favorito } from '../models/user/favorites.interface';

@Injectable({
  providedIn: 'root'
})
export class FavoriteService {

  constructor(private http: HttpClient) { }

  addToFavorite(productId: string): Observable<Favorito> {
    const url = `${environment.apiBaseUrl}/favorite/add/${productId}`;
    const headers = {
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    };
    return this.http.post<Favorito>(url, null, { headers });
  }
  removeFromFavorites(productId: string): Observable<Favorito> {
    const url = `${environment.apiBaseUrl}/favorite/delete/${productId}`;
    const headers = {
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    };
    return this.http.delete<Favorito>(url, { headers });
  }

  getFavorites(): Observable<AllFavoritesResponse> {
    const url = `${environment.apiBaseUrl}/favorite/get/me`;
    const header = {
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    };
    return this.http.get<AllFavoritesResponse>(url, { headers: header });
  }
}

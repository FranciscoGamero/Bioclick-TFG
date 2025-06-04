import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from '../environments/environment';
import { userLoginResponse } from '../models/user/user-login.interface';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(private http: HttpClient) {}

  loginUser(username: string, password: string): Observable<userLoginResponse> {
    let url = `${environment.apiBaseUrl}/login`;
    let body = {
      username: username,
      password: password
    };
    return this.http.post<userLoginResponse>(url, body);
  }
}

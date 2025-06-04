import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { LoginformComponent } from './components/LoginForm/LoginForm.component';
import { provideHttpClient } from '@angular/common/http';
import { MatFormFieldModule } from '@angular/material/form-field';
import { RegisterFormComponent } from './components/RegisterForm/RegisterForm.component';
import { FormsModule } from '@angular/forms';
import { SidebarComponent } from './shared/sidebar/sidebar.component';
import { UserPannelComponent } from './components/Admin/user-pannel/user-pannel.component';
import { MatInputModule } from '@angular/material/input';
import { MatCardModule } from '@angular/material/card';
import { NgbDropdownModule } from '@ng-bootstrap/ng-bootstrap';
import { AdminPannelComponent } from './components/Admin/admin-pannel/admin-pannel.component';
@NgModule({
  declarations: [
    AppComponent,
    LoginformComponent,
    RegisterFormComponent,
    SidebarComponent,
    UserPannelComponent,
    AdminPannelComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    MatFormFieldModule,
    FormsModule,
    MatInputModule,
    MatCardModule,
    NgbDropdownModule
  ],
  providers: [provideHttpClient(), provideAnimationsAsync()],
  bootstrap: [AppComponent]
})
export class AppModule { }

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
import { MatInputModule } from '@angular/material/input';
import { MatCardModule } from '@angular/material/card';
import { NgbDropdownModule } from '@ng-bootstrap/ng-bootstrap';
import { AdminPannelComponent } from './components/Admin/admin-pannel/admin-pannel.component';
import { AllPannelComponent } from './components/Admin/all-pannel/all-pannel.component';
import {MatDialogModule} from '@angular/material/dialog';
import { ProductPannelComponent } from './components/Common/product-pannel/product-pannel.component';
import { CategoryPannelComponent } from './components/Common/category-pannel/category-pannel.component';
import { ManagerPannelComponent } from './components/Admin/manager-pannel/manager-pannel.component';
import { DeleteManagerDialogComponent, EditManagerDialogComponent } from './components/Dialog/ManagerDialog/manager-dialog';
import { UserPannelComponent } from './components/Admin/user-pannel/user-pannel.component';
import { EditUserDialogComponent, DeleteUserDialogComponent } from './components/Dialog/UserDialog/user-dialog';
import { GraphicsPannelComponent } from './components/Common/graphics-pannel/graphics-pannel.component';
import { VerifyAccountComponent } from './components/Common/verify-account/verify-account.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginformComponent,
    RegisterFormComponent,
    SidebarComponent,
    UserPannelComponent,
    AdminPannelComponent,
    ManagerPannelComponent,
    AllPannelComponent,
    EditUserDialogComponent,
    EditManagerDialogComponent,
    DeleteManagerDialogComponent,
    DeleteUserDialogComponent,
    ProductPannelComponent,
    CategoryPannelComponent,
    GraphicsPannelComponent,
    VerifyAccountComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    MatFormFieldModule,
    FormsModule,
    MatInputModule,
    MatCardModule,
    NgbDropdownModule,
    MatDialogModule
  ],
  providers: [provideHttpClient(), provideAnimationsAsync()],
  bootstrap: [AppComponent]
})
export class AppModule { }

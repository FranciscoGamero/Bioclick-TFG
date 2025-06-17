import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginformComponent } from './components/LoginForm/LoginForm.component';
import { RegisterFormComponent } from './components/RegisterForm/RegisterForm.component';
import { UserPannelComponent } from './components/Admin/user-pannel/user-pannel.component';
import { AdminPannelComponent } from './components/Admin/admin-pannel/admin-pannel.component';
import { ManagerPannelComponent } from './components/Admin/manager-pannel/manager-pannel.component';
import { AllPannelComponent } from './components/Admin/all-pannel/all-pannel.component';
import { ProductPannelComponent } from './components/Common/product-pannel/product-pannel.component';
import { CategoryPannelComponent } from './components/Common/category-pannel/category-pannel.component';
import { GraphicsPannelComponent } from './components/Common/graphics-pannel/graphics-pannel.component';
import { VerifyAccountComponent } from './components/Common/verify-account/verify-account.component';
import { MyProfileComponent } from './components/Common/my-profile/my-profile.component';
import { UserDetailComponent } from './components/Admin/user-detail/user-detail.component';
import { HomeComponent } from './components/User/home/home.component';
import { ProductDetailComponent } from './components/User/product-detail/product-detail.component';
import { AllProductsComponent } from './components/User/all-products/all-products.component';
import { authGuard } from './guards/auth.guard';

const routes: Routes = [
  { path: '', redirectTo: '/register', pathMatch: 'full' },
  { path: 'login', component: LoginformComponent },
  { path: 'register', component: RegisterFormComponent },
  { path: 'verify', component: VerifyAccountComponent },
  { path: 'user-list', component: UserPannelComponent, canActivate: [authGuard], data: { roles: ['ROLE_ADMIN', 'ROLE_MANAGER'] } },
  { path: 'admin-list', component: AdminPannelComponent, canActivate: [authGuard], data: { roles: ['ROLE_ADMIN'] } },
  { path: 'manager-list', component: ManagerPannelComponent, canActivate: [authGuard], data: { roles: ['ROLE_ADMIN', 'ROLE_MANAGER'] } },
  { path: 'all-list', component: AllPannelComponent, canActivate: [authGuard] },
  { path: 'product-list', component: ProductPannelComponent, canActivate: [authGuard] },
  { path: 'category-list', component: CategoryPannelComponent, canActivate: [authGuard] },
  { path: 'graphics', component: GraphicsPannelComponent, canActivate: [authGuard] },
  { path: 'my-profile', component: MyProfileComponent, canActivate: [authGuard] },
  { path: 'user-detail/:id', component: UserDetailComponent, canActivate: [authGuard], data: { roles: ['ROLE_ADMIN', 'ROLE_MANAGER'] } },
  { path: 'home', component: HomeComponent, canActivate: [authGuard] },
  { path: 'product-detail/:id', component: ProductDetailComponent, canActivate: [authGuard] },
  { path: 'all-products', component: AllProductsComponent, canActivate: [authGuard] },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

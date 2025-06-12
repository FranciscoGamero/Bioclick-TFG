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
const routes: Routes = [
  {path: '', redirectTo: '/login', pathMatch: 'full'},
  {path: 'login', component: LoginformComponent},
  {path: 'register', component: RegisterFormComponent},
  {path: 'verify', component: VerifyAccountComponent},
  {path: 'user-list', component: UserPannelComponent},
  {path: 'admin-list', component: AdminPannelComponent},
  {path: 'manager-list', component: ManagerPannelComponent},
  {path: 'all-list', component: AllPannelComponent},
  {path: 'product-list', component: ProductPannelComponent},
  {path: 'category-list', component: CategoryPannelComponent},
  {path: 'graphics', component: GraphicsPannelComponent},
  {path: 'my-profile', component: MyProfileComponent},
  {path: 'user-detail/:id', component: UserDetailComponent},

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

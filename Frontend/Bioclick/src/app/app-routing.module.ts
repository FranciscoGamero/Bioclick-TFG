import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginformComponent } from './components/LoginForm/LoginForm.component';
import { RegisterFormComponent } from './components/RegisterForm/RegisterForm.component';
import { UserPannelComponent } from './components/Admin/user-pannel/user-pannel.component';
import { AdminPannelComponent } from './components/Admin/admin-pannel/admin-pannel.component';
import { ManagerPannelComponent } from './components/Admin/manager-pannel/manager-pannel.component';
import { AllPannelComponent } from './components/Admin/all-pannel/all-pannel.component';
const routes: Routes = [
  {path: '', redirectTo: '/login', pathMatch: 'full'},
  {path: 'login', component: LoginformComponent},
  {path: 'register', component: RegisterFormComponent},
  {path: 'user-list', component: UserPannelComponent},
  {path: 'admin-list', component: AdminPannelComponent},
  {path: 'manager-list', component: ManagerPannelComponent},
  {path: 'all-list', component: AllPannelComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

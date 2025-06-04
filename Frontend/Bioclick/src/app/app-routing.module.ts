import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginformComponent } from './components/LoginForm/LoginForm.component';
import { RegisterFormComponent } from './components/RegisterForm/RegisterForm.component';
import { UserPannelComponent } from './components/Admin/user-pannel/user-pannel.component';
const routes: Routes = [
  {path: '', redirectTo: '/sidebar', pathMatch: 'full'},
  {path: 'login', component: LoginformComponent},
  {path: 'register', component: RegisterFormComponent},
  {path: 'sidebar', component: UserPannelComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

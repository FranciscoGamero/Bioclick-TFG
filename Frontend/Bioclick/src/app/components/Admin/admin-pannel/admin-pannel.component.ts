import { Component } from '@angular/core';
import { AdminService } from '../../../services/admins.service';
import { AllAdminsResponse } from '../../../models/user/get-all-admins-interface';

@Component({
  selector: 'app-admin-pannel',
  templateUrl: './admin-pannel.component.html',
  styleUrl: './admin-pannel.component.scss'
})
export class AdminPannelComponent {
  isExpanded: boolean = false;
  name: string = '';
  usersFound: AllAdminsResponse | undefined = undefined;
  page: number = 1;
  ngOnInit(): void {
    this.getAdmins();
  }
  constructor(private adminService: AdminService) { }

  limpiarUrlFoto(url: string | undefined | null): string {
  const prefix = "http://localhost:8080/download/";
  if (!url) {
    return '';
  }
  if (url.startsWith(prefix)) {
    return url.substring(prefix.length);
  }
  return url;
}
  getAdmins() {
    this.adminService.getAllAdmins(this.page-1).subscribe({
      next: (response) => {
        this.usersFound = response;
        console.log(JSON.stringify(this.usersFound?.contenido[0]));

      },
      error: (error) => {
        console.error('Error fetching users:', error);
      }
    });
  }

  handleSidebarToggle() {
    this.isExpanded = !this.isExpanded;
  }
  onCardClick(userId: string) {
    console.log('Card clicked for user ID:', userId);
  }
}

import { Component, inject } from '@angular/core';
import { AdminService } from '../../../services/admins.service';
import { AllAdminsResponse } from '../../../models/user/get-all-admins-interface';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { CreateManagerDialogComponent } from '../../Dialog/ManagerDialog/manager-dialog';

@Component({
  selector: 'app-admin-pannel',
  templateUrl: './admin-pannel.component.html',
  styleUrl: './admin-pannel.component.scss'
})
export class AdminPannelComponent {
    readonly dialog = inject(MatDialog);
  
  isExpanded: boolean = true;
  name: string = '';
  adminsFound: AllAdminsResponse | undefined = undefined;
  page: number = 1;
  errorCreateAdmin: boolean = false;
  ngOnInit(): void {
    this.getAdmins();
  }
  constructor(private adminService: AdminService, private router: Router) { }

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
    this.adminService.getAllAdmins(this.page - 1).subscribe({
      next: (response) => {
        this.adminsFound = response;
        console.log(JSON.stringify(this.adminsFound?.contenido[0]));

      },
      error: (error) => {
        console.error('Error fetching users:', error);
      }
    });
  }
  openCreateDialog(): void {

    const dialogRef = this.dialog.open(CreateManagerDialogComponent, {
      width: '800px',
      data: {
        username: '',
        correo: '',
        password: '',
        verifyPassword: '',
        fotoPerfilUrl: ''
      }
    });
    console.log(dialogRef.componentInstance.data);


    dialogRef.afterClosed().subscribe(result => {
      console.log('Dialog closed with result:', result);
      if (result && result.selectedFile) {
        this.adminService.createAdmin(
          result.username,
          result.correo,
          result.password,
          result.verifyPassword,
          result.selectedFile
        ).subscribe({
          next: () => {
            this.errorCreateAdmin = false;
            this.getAdmins();
          },
          error: (error: Error) => {
            console.log(error)
            this.errorCreateAdmin = true;
          }
        });
      }
    });
  }

  handleSidebarToggle() {
    this.isExpanded = !this.isExpanded;
  }
  onCardClick(userId: string) {
    this.router.navigate(['/user-detail', userId]);
  }
}

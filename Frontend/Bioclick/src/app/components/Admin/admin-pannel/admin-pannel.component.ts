import { Component, inject } from '@angular/core';
import { AdminService } from '../../../services/admins.service';
import { Admin, AllAdminsResponse } from '../../../models/user/get-all-admins-interface';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { CreateManagerDialogComponent } from '../../Dialog/ManagerDialog/manager-dialog';
import { Usuario } from '../../../models/user/get-all-users-interface';
import { delay } from 'rxjs';
import { environment } from '../../../environments/environment';

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
  adminsPorNombre: Usuario[] = [];
  page: number = 0;

  showBuscados: boolean = false;
  nombreAdmin: string = '';
  pageBuscada: number = 0;

  showAlert: boolean = false;
  alertMessage: string = '';
  ngOnInit(): void {
    this.page = 0;
    this.pageBuscada = 0;
    this.getAdmins();
  }
  constructor(private adminService: AdminService, private router: Router) { }

  limpiarUrlFoto(url: string | undefined | null): string {
    if (!url) return '';

    if (url.includes('randomuser.me')) {
      return url.replace(`${environment.apiBaseUrl}/download/`, '');
    }

    if (url.startsWith('http')) return url;

    return `${environment.apiBaseUrl}/download/${url}`;
  }
  getAdmins(page: number = 0) {
    this.adminService.getAllAdmins(page).subscribe({
      next: (response) => {
        this.adminsFound = response;
        this.page = page;
      },
      error: (error) => {
        console.error('Error fetching admins:', error);
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
    dialogRef.afterClosed().subscribe(result => {
      if (result && result.selectedFile) {
        this.adminService.createAdmin(
          result.username,
          result.correo,
          result.password,
          result.verifyPassword,
          result.selectedFile
        ).subscribe({
          next: () => {
            this.showAlert = false;
            this.getAdmins();
          },
          error: (error) => {
            this.showAlert = true;
            if (error.error && error.error['invalid-params'] && error.error['invalid-params'].length > 0) {
              this.alertMessage = error.error['invalid-params'][0].message;
            } else if (error.error && error.error.detail) {
              this.alertMessage = error.error.detail;
            } else {
              this.alertMessage = 'Error al crear el administrador.';
            }
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
  buscarPorNombre(): void {
    this.pageBuscada = 0;
    this.showBuscados = true;
    this.adminService.getUsersByName(this.nombreAdmin, this.pageBuscada, 12).subscribe({
      next: (response) => {
        this.showAlert = false;
        this.adminsPorNombre = response.contenido.filter((manager: Usuario) => manager.role === 'ROLE_ADMIN');
        this.pageBuscada++;
      },
      error: (error) => {
        this.showAlert = true;
        if (error.error && error.error['invalid-params'] && error.error['invalid-params'].length > 0) {
          this.alertMessage = error.error['invalid-params'][0].message;
        } else if (error.error && error.error.detail) {
          this.alertMessage = error.error.detail;
        } else {
          this.alertMessage = 'Error al crear el administrador.';
        }
      }
    });
  }
  loadPannel(): void {
    this.showBuscados = false;
    this.nombreAdmin = '';
    this.ngOnInit();
  }
}

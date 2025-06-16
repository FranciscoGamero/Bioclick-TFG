import { Component, inject } from '@angular/core';
import { AdminService } from '../../../services/admins.service';
import { Admin, AllAdminsResponse } from '../../../models/user/get-all-admins-interface';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { CreateManagerDialogComponent } from '../../Dialog/ManagerDialog/manager-dialog';
import { Usuario } from '../../../models/user/get-all-users-interface';

@Component({
  selector: 'app-admin-pannel',
  templateUrl: './admin-pannel.component.html',
  styleUrl: './admin-pannel.component.scss'
})
export class AdminPannelComponent {
  readonly dialog = inject(MatDialog);

  isExpanded: boolean = true;
  name: string = '';
  adminsFound: Usuario[] = [];
  page: number = 0;
  errorCreateAdmin: boolean = false;

  showBuscados: boolean = false;
  nombreAdmin: string = '';
  pageBuscada: number = 0;
  ngOnInit(): void {
    this.page = 0;
    this.pageBuscada = 0;
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
    this.adminService.getAllAdmins(this.page).subscribe({
      next: (response) => {
              this.adminsFound = (response.contenido as Admin[]).map(manager => ({
                ...manager,
                fechaRegistro: (manager as any).fechaRegistro ?? '',
                role: (manager as any).role ?? 'ROLE_MANAGER'
              })) as Usuario[];
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
            this.errorCreateAdmin = false;
            this.getAdmins();
          },
          error: (error: Error) => {
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
  buscarPorNombre(): void {
    this.pageBuscada = 0;
    this.showBuscados = true;
    this.adminService.getUsersByName(this.nombreAdmin, this.pageBuscada, 12).subscribe({
      next: (response) => {
        this.adminsFound = response.contenido.filter((manager: Usuario) => manager.role === 'ROLE_ADMIN');;
        this.pageBuscada++;
      },
      error: (error) => {
        console.error('Error fetching products by name:', error);
      }
    });
  }
  loadPannel(): void {
    this.showBuscados = false;
    this.nombreAdmin = '';
    this.ngOnInit();
  }
}

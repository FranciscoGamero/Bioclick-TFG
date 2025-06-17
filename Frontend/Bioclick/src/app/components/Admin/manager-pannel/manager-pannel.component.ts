import { Component, Inject, inject } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { AllManagersResponse, Manager } from '../../../models/user/get-all-managers-interface';
import { ManagerService } from '../../../services/manager.service';
import { AdminService } from '../../../services/admins.service';
import { EditManagerDialogComponent, DeleteManagerDialogComponent, CreateManagerDialogComponent } from '../../Dialog/ManagerDialog/manager-dialog';
import { Router } from '@angular/router';
import { Usuario } from '../../../models/user/get-all-users-interface';
import { environment } from '../../../environments/environment';


@Component({
  selector: 'app-manager-pannel',
  templateUrl: './manager-pannel.component.html',
  styleUrl: './manager-pannel.component.scss'
})
export class ManagerPannelComponent {
  readonly dialog = inject(MatDialog);

  isExpanded: boolean = true;
  name: string = '';
  managersFound: Usuario[] = [];
  page: number = 0;

  showBuscados: boolean = false;
  nombreManager: string = '';
  pageBuscada: number = 0;

  showAlert: boolean = false;
  alertMessage: string = '';
  ngOnInit(): void {
    this.getManagers();
  }
  constructor(private managerService: ManagerService, private adminService: AdminService, private router: Router) { }

  limpiarUrlFoto(url: string | undefined | null): string {
    if (!url) return '';
    if (url.includes('randomuser.me')) {
      return url.replace(`${environment.apiBaseUrl}/download/`, '');
    }
    return url;
  }
  getManagers() {
    this.showBuscados = false;
    this.managerService.getAllManagers(this.page - 1).subscribe({
      next: (response) => {
        this.managersFound = (response.contenido as Manager[]).map(manager => ({
          ...manager,
          fechaRegistro: (manager as any).fechaRegistro ?? '',
          role: (manager as any).role ?? 'ROLE_MANAGER'
        })) as Usuario[];
      },
      error: (error) => {
        this.showAlert = true;
        if (error.error && error.error['invalid-params'] && error.error['invalid-params'].length > 0) {
          this.alertMessage = error.error['invalid-params'][0].message;
        } else if (error.error && error.error.detail) {
          this.alertMessage = error.error.detail;
        } else {
          this.alertMessage = 'Error al crear la categoría.';
        }
      }
    });
  }

  handleSidebarToggle() {
    this.isExpanded = !this.isExpanded;
  }
  onCardClick(userId: string) {
    this.router.navigate(['/user-detail', userId]);

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
        this.adminService.createManager(
          result.username,
          result.correo,
          result.password,
          result.verifyPassword,
          result.selectedFile
        ).subscribe({
          next: () => {
            this.showAlert = false;
            this.getManagers();
          },
          error: (error) => {
            this.showAlert = true;
            if (error.error && error.error['invalid-params'] && error.error['invalid-params'].length > 0) {
              this.alertMessage = error.error['invalid-params'][0].message;
            } else if (error.error && error.error.detail) {
              this.alertMessage = error.error.detail;
            } else {
              this.alertMessage = 'Error al crear el manager.';
            }
          }
        });
      }
    });
  }
  openEditDialog(manager: { id: string; username: string; correo: string; password: string; fotoPerfilUrl: string }): void {
    const dialogRef = this.dialog.open(EditManagerDialogComponent, {
      width: '800px',
      data: { id: manager.id, username: manager.username, correo: manager.correo, password: manager.password, fotoPerfilUrl: manager.fotoPerfilUrl }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.adminService.updateManager(
          result.id,
          result.username,
          result.correo,
          result.password,
          result.selectedFile,
          result.fotoPerfilUrl
        ).then((observable: any) => {
          observable.subscribe({
            next: (): void => {
              this.showAlert = false;
              this.getManagers();
            },
            error: (error: {
              error?: {
                'invalid-params'?: { message: string }[];
                detail?: string;
              };
            }): void => {
              this.showAlert = true;
              if (error.error && error.error['invalid-params'] && error.error['invalid-params'].length > 0) {
                this.alertMessage = error.error['invalid-params'][0].message;
              } else if (error.error && error.error.detail) {
                this.alertMessage = error.error.detail;
              } else {
                this.alertMessage = 'Error al editar el manager.';
              }
            }
          });
        })
      }
    });
  }
  openDeleteDialog(manager: { id: string; }): void {
    const dialogRef = this.dialog.open(DeleteManagerDialogComponent, {
      width: '800px',
      data: { id: manager.id }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.adminService.deleteManager(
          result.id
        ).subscribe({
          next: () => {
            this.getManagers();
          }
        });
      }
    });
  }
  buscarPorNombre(): void {
    this.pageBuscada = 0;
    this.showBuscados = true;
    this.adminService.getUsersByName(this.nombreManager, this.pageBuscada, 12).subscribe({
      next: (response) => {
        this.showAlert = false;
        const managers = response.contenido.filter((manager: Usuario) => manager.role === 'ROLE_MANAGER');
        this.managersFound = managers;
        this.pageBuscada++;
      },
      error: (error) => {
        this.showAlert = true;
        if (error.error && error.error['invalid-params'] && error.error['invalid-params'].length > 0) {
          this.alertMessage = error.error['invalid-params'][0].message;
        } else if (error.error && error.error.detail) {
          this.alertMessage = error.error.detail;
        }
      }
    });
  }
  loadPannel(): void {
    this.showBuscados = false;
    this.nombreManager = '';
    this.ngOnInit();
  }
}
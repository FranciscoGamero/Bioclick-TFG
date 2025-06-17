import { Component, inject, OnInit } from '@angular/core';
import { AllFoundResponse } from '../../../models/user/get-all-found';
import { AdminService } from '../../../services/admins.service';
import { DeleteManagerDialogComponent, EditManagerDialogComponent } from '../../Dialog/ManagerDialog/manager-dialog';
import { DeleteUserDialogComponent } from '../../Dialog/UserDialog/user-dialog';
import { MatDialog } from '@angular/material/dialog';
import { UserService } from '../../../services/user.service';
import { Router } from '@angular/router';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-all-pannel',
  templateUrl: './all-pannel.component.html',
  styleUrl: './all-pannel.component.scss'
})
export class AllPannelComponent implements OnInit {
  readonly dialog = inject(MatDialog);
  isExpanded: boolean = true;
  name: string = '';
  allFound: AllFoundResponse | undefined = undefined;
  page: number = 0;
  errorEditManager: boolean = false;

  showBuscados: boolean = false;
  nombreUsuario: string = '';
  pageBuscada: number = 0;

  showAlert: boolean = false;
  alertMessage: string = '';
  ngOnInit(): void {
    this.foundAll();
  }

  constructor(private adminService: AdminService, private userService: UserService, private router: Router) { }

  limpiarUrlFoto(url: string | undefined | null): string {
    if (!url) return '';

    if (url.includes('randomuser.me')) {
      return url.replace(`${environment.apiBaseUrl}/download/`, '');
    }

    if (url.startsWith('http')) return url;

    return `${environment.apiBaseUrl}/download/${url}`;
  }
  foundAll() {
    this.adminService.getAll(this.page - 1).subscribe({
      next: (response) => {
        this.showAlert = false;
        this.allFound = response;
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

  isUser(role: String): boolean {
    return role === 'ROLE_USUARIO';
  }
  isManager(role: String): boolean {
    return role === 'ROLE_MANAGER';
  }
  isAdmin(role: String): boolean {
    return role === 'ROLE_ADMIN';
  }

  handleSidebarToggle() {
    this.isExpanded = !this.isExpanded;
  }
  onCardClick(userId: string) {
    this.router.navigate(['/user-detail', userId]);
  }
  openUserEditDialog(manager: { id: string; username: string; correo: string; password: string; fotoPerfilUrl: string }): void {
    const dialogRef = this.dialog.open(EditManagerDialogComponent, {
      width: '800px',
      data: { id: manager.id, username: manager.username, correo: manager.correo, password: manager.password, fotoPerfilUrl: manager.fotoPerfilUrl }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.userService.updateUser(
          result.id,
          result.username,
          result.correo,
          result.password,
          result.selectedFile,
        ).then((observable: any) => {
          observable.subscribe({
            next: () => {
              this.foundAll();
              this.showAlert = false;
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
                this.alertMessage = 'Error al editar el usuario.';
              }
            }
          });
        })
      }
    });
  }
  openUserDeleteDialog(user: { id: string; }): void {
    const dialogRef = this.dialog.open(DeleteUserDialogComponent, {
      width: '800px',
      data: { id: user.id }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.userService.deleteUser(
          result.id
        ).subscribe({
          next: () => {
            this.foundAll();
          },
        });
      }
    });
  }
  openManagerEditDialog(manager: { id: string; username: string; correo: string; password: string; fotoPerfilUrl: string }): void {
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
            next: () => {
              this.showAlert = false;
              this.foundAll();
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

  openManagerDeleteDialog(manager: { id: string; }): void {
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
            this.foundAll();
          },
        });
      }
    });
  }
  buscarPorNombre(): void {
    this.pageBuscada = 0;
    this.showBuscados = true;
    this.adminService.getUsersByName(this.nombreUsuario, this.pageBuscada, 12).subscribe({
      next: (response) => {
        this.showAlert = false;
        this.allFound = response;
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
  loadPannel(): void {
    this.showBuscados = false;
    this.nombreUsuario = '';
    this.ngOnInit();
  }
}

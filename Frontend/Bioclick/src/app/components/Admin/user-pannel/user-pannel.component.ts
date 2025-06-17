import { Component, Inject, inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialog } from '@angular/material/dialog';
import { AllUsersResponse } from '../../../models/user/get-all-users-interface';
import { UserService } from '../../../services/user.service';
import { AdminService } from '../../../services/admins.service';

import { DeleteUserDialogComponent, EditUserDialogComponent } from '../../Dialog/UserDialog/user-dialog';
import { Router } from '@angular/router';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-user-pannel',
  templateUrl: './user-pannel.component.html',
  styleUrls: ['./user-pannel.component.scss']
})
export class UserPannelComponent implements OnInit {
  readonly dialog = inject(MatDialog);
  isExpanded: boolean = true;

  usersFound: AllUsersResponse | undefined = undefined;
  page: number = 1;

  errorEditUser: boolean = false;

  showBuscados: boolean = false;
  nombreUser: string = '';
  pageBuscada: number = 1;

  showAlert: boolean = false;
  alertMessage: string = '';
  ngOnInit(): void {
    this.getUsers();
  }

  constructor(private userService: UserService, private router: Router, private adminService: AdminService) { }

  limpiarUrlFoto(url: string | undefined | null): string {
    if (!url) return '';
    if (url.includes('randomuser.me')) {
      return url.replace(`${environment.apiBaseUrl}/download/`, '');
    }
    return url;
  }
  getUsers() {
    this.showBuscados = false;
    this.userService.getAllUsers(this.page - 1).subscribe({
      next: (response) => {
        this.showAlert = false;
        this.usersFound = response;
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

  handleSidebarToggle() {
    this.isExpanded = !this.isExpanded;
  }
  onCardClick(userId: string) {
    this.router.navigate(['/user-detail', userId]);
  }

  openEditDialog(user: { id: string; username: string; correo: string; password: string; fotoPerfilUrl: string }): void {
    const dialogRef = this.dialog.open(EditUserDialogComponent, {
      width: '800px',
      data: { id: user.id, username: user.username, correo: user.correo, password: user.password, fotoPerfilUrl: user.fotoPerfilUrl }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.userService.updateUser(
          result.id,
          result.username,
          result.correo,
          result.password,
          result.file,
        ).then((observable: any) => {
          observable.subscribe({
            next: () => {
              this.showAlert = false;
              this.getUsers();
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
  openDeleteDialog(user: { id: string; }): void {
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
            this.getUsers();
          },
        });
      }
    });
  }
  buscarPorNombre(): void {
    this.showBuscados = true;
    this.pageBuscada = 0;
    this.adminService.getUsersByName(this.nombreUser, this.pageBuscada, 12).subscribe({
      next: (response) => {
        this.showAlert = false;
        this.usersFound = response;
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
    this.nombreUser = '';
    this.ngOnInit();
  }
}
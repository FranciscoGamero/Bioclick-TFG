import { Component, Inject, inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialog } from '@angular/material/dialog';
import { AllUsersResponse } from '../../../models/user/get-all-users-interface';
import { UserService } from '../../../services/user.service';
import { AdminService } from '../../../services/admins.service';

import { DeleteUserDialogComponent, EditUserDialogComponent } from '../../Dialog/UserDialog/user-dialog';

@Component({
  selector: 'app-user-pannel',
  templateUrl: './user-pannel.component.html',
  styleUrls: ['./user-pannel.component.scss']
})
export class UserPannelComponent implements OnInit {
  readonly dialog = inject(MatDialog);
  isExpanded: boolean = true;
  name: string = '';
  usersFound: AllUsersResponse | undefined = undefined;
  page: number = 1;
  ngOnInit(): void {
    this.getUsers();
  }

  constructor(private userService: UserService, private adminService: AdminService) { }

  limpiarUrlFoto(url: string | undefined | null): string {
    if (!url) return '';

    if (url.includes('randomuser.me')) {
      return url.replace('http://localhost:8080/download/', '');
    }

    if (url.startsWith('http')) return url;

    return `http://localhost:8080/download/${url}`;
  }
  getUsers() {
    this.userService.getAllUsers(this.page - 1).subscribe({
      next: (response) => {
        this.usersFound = response;
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

  openEditDialog(user: { id: string; username: string; correo: string; password: string; fotoPerfilUrl: string }): void {
    const dialogRef = this.dialog.open(EditUserDialogComponent, {
      width: '800px',
      data: { id: user.id, username: user.username, correo: user.correo, password: user.password, fotoPerfilUrl: user.fotoPerfilUrl }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        console.log('Dialog closed with result:', result);
        this.userService.updateUser(
          result.id,
          result.username,
          result.correo,
          result.password,
          result.file,
          result.fotoPerfilUrl
        ).then((observable: any) => {
          observable.subscribe({
            next: () => {
              this.getUsers();
            },
            error: (error: Error) => {
              console.error(error);
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
          error: (error: Error) => {
            console.error(error);
          }
        });
      }
    });
  }
}
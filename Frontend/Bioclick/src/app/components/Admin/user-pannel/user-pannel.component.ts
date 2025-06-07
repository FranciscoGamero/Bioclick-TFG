import { Component, Inject, inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialog } from '@angular/material/dialog';
import { AllUsersResponse } from '../../../models/user/get-all-users-interface';
import { UserService } from '../../../services/user.service';
import { EditManagerDialogComponent } from '../manager-pannel/manager-pannel.component';
import { AdminService } from '../../../services/admins.service';

export interface EditUserData {
  id: string;
  username: string;
  correo: string;
  password: string;
  fotoPerfilUrl?: string;
}
export interface DeleteUserData {
  id: string;
}

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

  openEditDialog(manager: { id: string; username: string; correo: string; password: string; fotoPerfilUrl: string }): void {
    const dialogRef = this.dialog.open(EditManagerDialogComponent, {
      width: '800px',
      data: { id: manager.id, username: manager.username, correo: manager.correo, password: manager.password, fotoPerfilUrl: manager.fotoPerfilUrl }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        console.log('Dialog closed with result:', result);
        this.userService.updateUser(
          result.id,
          result.username,
          result.correo,
          result.password,
          result.selectedFile,
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
@Component({
  selector: 'app-edit-user-dialog',
  template: `
    <h2 mat-dialog-title>Editar Usuario</h2>
    <mat-dialog-content>
      <form class="w-100 d-flex flex-column align-items-center justify-content-center">
        <div class="mb-3" style="width: 350px;">
          <label for="username" class="form-label">Username</label>
          <input type="text" class="form-control" id="username" name="username"
            placeholder="Ingresa tu username" [(ngModel)]="data.username">
        </div>
        <div class="mb-3" style="width: 350px;">
          <label for="email" class="form-label">Correo</label>
          <input type="email" class="form-control" id="email" name="email"
            placeholder="Ingresa tu email" [(ngModel)]="data.correo">
        </div>
        <div class="mb-3" style="width: 350px;">
          <label for="formFile" class="form-label">Foto de Perfil</label>
          <input class="form-control" type="file" id="formFile" (change)="onFileSelected($event)">
        </div>
      </form>
    </mat-dialog-content>
    <mat-dialog-actions align="center">
      <button class="btn btn-danger mx-auto" (click)="onCancel()">Cancelar</button>
      <button class="btn btn-success mx-auto" (click)="onSave()">Guardar</button>
    </mat-dialog-actions>
  `
})
export class EditUserDialogComponent {
  selectedFile: File | null = null;

  constructor(
    public dialogRef: MatDialogRef<EditUserDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: EditUserData
  ) { }

  onCancel(): void {
    this.dialogRef.close();
  }

  onSave(): void {
    this.dialogRef.close({
      ...this.data,
      selectedFile: this.selectedFile
    });
  }
  onFileSelected(event: any) {
    if (event && event.target.files.length > 0) {
      this.selectedFile = event.target.files[0];
    }
  }
}
@Component({
  selector: 'app-delete-user-dialog',
  template: `
    <h2 mat-dialog-title>Eliminar Usuario</h2>
    <mat-dialog-content>
      <form class="w-100 d-flex flex-column align-items-center justify-content-center">
        <p>¿Estás seguro de que deseas eliminar este usuario?</p>
      </form>
    </mat-dialog-content>
    <mat-dialog-actions align="center">
      <button class="btn btn-danger mx-auto" (click)="onCancel()">Cancelar</button>
      <button class="btn btn-success mx-auto" (click)="onSave()">Eliminar</button>
    </mat-dialog-actions>
  `
})

export class DeleteUserDialogComponent {
  constructor(
    public dialogRef: MatDialogRef<DeleteUserDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DeleteUserData
  ) { }

  onCancel(): void {
    this.dialogRef.close();
  }

  onSave(): void {
    this.dialogRef.close({
      ...this.data,
    });
  }
}

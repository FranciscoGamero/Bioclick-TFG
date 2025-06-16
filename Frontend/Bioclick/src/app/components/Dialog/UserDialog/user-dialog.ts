import { Component, Inject } from "@angular/core";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";

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
    file: this.selectedFile
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
@Component({
  selector: 'app-change-password-dialog',
  template: `
    <h2 mat-dialog-title>Cambiar Contraseña</h2>
    <mat-dialog-content>
      <form class="w-100 d-flex flex-column align-items-center justify-content-center">
        <div class="mb-3" style="width: 350px;">
          <label for="currentPassword" class="form-label">Contraseña actual</label>
          <input type="password" class="form-control" id="currentPassword" name="currentPassword"
            placeholder="Introduce tu contraseña actual" [(ngModel)]="currentPassword">
        </div>
        <div class="mb-3" style="width: 350px;">
          <label for="newPassword" class="form-label">Nueva contraseña</label>
          <input type="password" class="form-control" id="newPassword" name="newPassword"
            placeholder="Introduce la nueva contraseña" [(ngModel)]="newPassword">
        </div>
        <div class="mb-3" style="width: 350px;">
          <label for="repeatPassword" class="form-label">Repite la nueva contraseña</label>
          <input type="password" class="form-control" id="repeatPassword" name="repeatPassword"
            placeholder="Repite la nueva contraseña" [(ngModel)]="repeatPassword">
        </div>
        <div *ngIf="error" class="alert alert-danger w-100 text-center">
          {{error}}
        </div>
      </form>
    </mat-dialog-content>
    <mat-dialog-actions align="center">
      <button class="btn btn-danger mx-auto" (click)="onCancel()">Cancelar</button>
      <button class="btn btn-success mx-auto" (click)="onSave()">Guardar</button>
    </mat-dialog-actions>
  `
})
export class ChangePasswordDialogComponent {
  currentPassword: string = '';
  newPassword: string = '';
  repeatPassword: string = '';
  error: string = '';

  constructor(
    public dialogRef: MatDialogRef<ChangePasswordDialogComponent>
  ) {}

  onCancel(): void {
    this.dialogRef.close();
  }

  onSave(): void {
    if (!this.currentPassword || !this.newPassword || !this.repeatPassword) {
      this.error = 'Todos los campos son obligatorios';
      return;
    }
    if (this.newPassword !== this.repeatPassword) {
      this.error = 'Las nuevas contraseñas no coinciden';
      return;
    }
    this.dialogRef.close({
      currentPassword: this.currentPassword,
      newPassword: this.newPassword
    });
  }
}
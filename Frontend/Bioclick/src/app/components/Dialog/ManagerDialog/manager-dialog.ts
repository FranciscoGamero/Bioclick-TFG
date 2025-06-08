import { Inject, Component } from "@angular/core";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";

export interface EditManagerData {
    id: string;
    username: string;
    correo: string;
    password: string;
    fotoPerfilUrl?: string;
}
export interface DeleteManagerData {
    id: string;
}

@Component({
    selector: 'app-edit-manager-dialog',
    template: `
    <h2 mat-dialog-title>Editar Manager</h2>
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
export class EditManagerDialogComponent {
    selectedFile: File | null = null;

    constructor(
        public dialogRef: MatDialogRef<EditManagerDialogComponent>,
        @Inject(MAT_DIALOG_DATA) public data: EditManagerData
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
    selector: 'app-delete-manager-dialog',
    template: `
    <h2 mat-dialog-title>Eliminar Manager</h2>
    <mat-dialog-content>
      <form class="w-100 d-flex flex-column align-items-center justify-content-center">
        <p>¿Estás seguro de que deseas eliminar este manager?</p>
      </form>
    </mat-dialog-content>
    <mat-dialog-actions align="center">
      <button class="btn btn-danger mx-auto" (click)="onCancel()">Cancelar</button>
      <button class="btn btn-success mx-auto" (click)="onSave()">Eliminar</button>
    </mat-dialog-actions>
  `
})
export class DeleteManagerDialogComponent {
    constructor(
        public dialogRef: MatDialogRef<DeleteManagerDialogComponent>,
        @Inject(MAT_DIALOG_DATA) public data: DeleteManagerData
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
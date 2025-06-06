import { Component, Inject, inject } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { AllManagersResponse } from '../../../models/user/get-all-managers-interface';
import { ManagerService } from '../../../services/manager.service';
import { AdminService } from '../../../services/admins.service';

export interface EditManagerData {
  id: string;
  username: string;
  correo: string;
  password: string;
  fotoPerfilUrl?: string;
}
@Component({
  selector: 'app-manager-pannel',
  templateUrl: './manager-pannel.component.html',
  styleUrl: './manager-pannel.component.scss'
})
export class ManagerPannelComponent {
  readonly dialog = inject(MatDialog);

  isExpanded: boolean = true;
  name: string = '';
  managersFound: AllManagersResponse | undefined = undefined;
  page: number = 1;

  ngOnInit(): void {
    this.getManagers();
  }
  constructor(private managerService: ManagerService, private adminService: AdminService) { }

limpiarUrlFoto(url: string | undefined | null): string {
  if (!url) return '';
  if (url.includes('randomuser.me')) {
    return url.replace('http://localhost:8080/download/', '');
  }
  if (url.startsWith('http')) return url;
  return `http://localhost:8080/download/${url}`;
}
  getManagers() {
    this.managerService.getAllManagers(this.page - 1).subscribe({
      next: (response) => {
        this.managersFound = response;
        console.log(this.managersFound.contenido[0].fotoPerfilUrl);
      },
      error: (error) => {
        console.error('Error fetching managers:', error);
      }
    });
  }

  handleSidebarToggle() {
    this.isExpanded = !this.isExpanded;
  }
  onCardClick(userId: string) {
  }

  openEditDialog(manager: { id: string; username: string; correo: string; password: string; fotoPerfilUrl: string }): void {
    const dialogRef = this.dialog.open(EditManagerDialogComponent, {
      width: '800px',
      data: { id: manager.id, username: manager.username, correo: manager.correo, password: manager.password, fotoPerfilUrl: manager.fotoPerfilUrl }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        console.log('Dialog closed with result:', result);
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
              this.getManagers();
            },
            error: (error: Error) => {
              console.error(error.name);
            }
          });
        })
      }
    });
  }
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
      selectedFile: this.selectedFile // será null si no seleccionó nada
    });
  }
  onFileSelected(event: any) {
    if (event && event.target.files.length > 0) {
      this.selectedFile = event.target.files[0];
    }
  }
}
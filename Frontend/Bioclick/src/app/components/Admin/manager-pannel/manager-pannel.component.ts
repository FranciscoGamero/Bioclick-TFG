import { Component, Inject, inject } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { AllManagersResponse } from '../../../models/user/get-all-managers-interface';
import { ManagerService } from '../../../services/manager.service';
import { AdminService } from '../../../services/admins.service';
import { EditManagerDialogComponent, DeleteManagerDialogComponent } from '../../Dialog/ManagerDialog/manager-dialog';
import { Router } from '@angular/router';


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
  constructor(private managerService: ManagerService, private adminService: AdminService, private router: Router) { }

  limpiarUrlFoto(url: string | undefined | null): string {
    if (!url) return '';
    if (url.includes('randomuser.me') || url.includes('localhost:8080/download/')) {
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
    this.router.navigate(['/user-detail', userId]);

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
          },
          error: (error: Error) => {
            console.error(error);
          }
        });
      }
    });
  }
}
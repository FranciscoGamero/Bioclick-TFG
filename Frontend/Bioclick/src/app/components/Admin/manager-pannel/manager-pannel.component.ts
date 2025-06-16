import { Component, Inject, inject } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { AllManagersResponse, Manager } from '../../../models/user/get-all-managers-interface';
import { ManagerService } from '../../../services/manager.service';
import { AdminService } from '../../../services/admins.service';
import { EditManagerDialogComponent, DeleteManagerDialogComponent, CreateManagerDialogComponent } from '../../Dialog/ManagerDialog/manager-dialog';
import { Router } from '@angular/router';
import { Usuario } from '../../../models/user/get-all-users-interface';


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
  errorEditManager: boolean = false;
  errorCreateManager: boolean = false;
  ngOnInit(): void {
    this.getManagers();
  }
  constructor(private managerService: ManagerService, private adminService: AdminService, private router: Router) { }

  limpiarUrlFoto(url: string | undefined | null): string {
    if (!url) return '';
    if (url.includes('randomuser.me')) {
      return url.replace('http://localhost:8080/download/', '');
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
            this.errorCreateManager = false;
            this.getManagers();
          },
          error: (error: Error) => {
            this.errorCreateManager = true;
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
            next: () => {
              this.errorEditManager = false;
              this.getManagers();
            },
            error: (error: Error) => {
              this.errorEditManager = true;
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
        const managers = response.contenido.filter((manager: Usuario) => manager.role === 'ROLE_MANAGER');
        this.managersFound = managers;
        console.log(this.managersFound);
        this.pageBuscada++;
      },
      error: (error) => {
        console.error('Error fetching products by name:', error);
      }
    });
  }
      loadPannel(): void {
    this.showBuscados = false;
    this.nombreManager = '';
    this.ngOnInit();
  }
}
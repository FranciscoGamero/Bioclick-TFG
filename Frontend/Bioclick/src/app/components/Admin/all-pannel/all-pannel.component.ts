import { Component, inject, OnInit } from '@angular/core';
import { AllFoundResponse } from '../../../models/user/get-all-found';
import { AdminService } from '../../../services/admins.service';
import { DeleteManagerDialogComponent, EditManagerDialogComponent } from '../../Dialog/ManagerDialog/manager-dialog';
import { DeleteUserDialogComponent } from '../../Dialog/UserDialog/user-dialog';
import { MatDialog } from '@angular/material/dialog';
import { UserService } from '../../../services/user.service';

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
  page: number = 1;
  ngOnInit(): void {
    this.foundAll();
  }

  constructor(private adminService: AdminService, private userService: UserService) { }

  limpiarUrlFoto(url: string | undefined | null): string {
    if (!url) return '';

    if (url.includes('randomuser.me')) {
      return url.replace('http://localhost:8080/download/', '');
    }

    if (url.startsWith('http')) return url;

    return `http://localhost:8080/download/${url}`;
  }
  foundAll() {
    this.adminService.getAll(this.page - 1).subscribe({
      next: (response) => {
        this.allFound = response;
      },
      error: (error) => {
        console.error('Error fetching users:', error);
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
    console.log('Card clicked for user ID:', userId);
  }
  openUserEditDialog(manager: { id: string; username: string; correo: string; password: string; fotoPerfilUrl: string }): void {
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
              this.foundAll();
            },
            error: (error: Error) => {
              console.error(error);
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
          error: (error: Error) => {
            console.error(error);
          }
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
                this.foundAll();
              },
              error: (error: Error) => {
                console.error(error.name);
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
            error: (error: Error) => {
              console.error(error);
            }
          });
        }
      });
    }
}

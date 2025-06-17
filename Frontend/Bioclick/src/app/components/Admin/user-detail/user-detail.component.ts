import { Component, inject } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { DetailUser } from '../../../models/user/detail-user.interface';
import { UserService } from '../../../services/user.service';
import { EditUserDialogComponent } from '../../Dialog/UserDialog/user-dialog';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-user-detail',
  templateUrl: './user-detail.component.html',
  styleUrl: './user-detail.component.css'
})
export class UserDetailComponent {
  readonly dialog = inject(MatDialog);
  user: DetailUser | undefined = undefined;

  errorEditUser: boolean = false;

  constructor(private userService: UserService, private activatedRoute: ActivatedRoute, private router: Router) { }

  ngOnInit(): void {
    const userId = this.activatedRoute.snapshot.paramMap.get('id');

    if (userId) {
      this.userService.getUser(userId).subscribe(user => {
        this.user = user;
      });
    } else {
      console.error('El id es Nulo o Indefinido');
    }
  }
  limpiarUrlFoto(url: string | undefined | null): string {
    if (!url) return '';

    if (url.includes('randomuser.me')) {
      return url.replace(`${environment.apiBaseUrl}/download/`, '');
    }

    if (url.startsWith('http')) return url;

    return `${environment.apiBaseUrl}/download/${url}`;
  }
  openEditDialog(user: { id: string; username: string; correo: string; password: string; fotoPerfilUrl: string }): void {
    const dialogRef = this.dialog.open(EditUserDialogComponent, {
      width: '800px',
      data: { id: user.id, username: user.username, correo: user.correo, password: user.password, fotoPerfilUrl: user.fotoPerfilUrl }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {

        this.userService.updateUser(
          this.user!.id,
          result.username,
          result.correo,
          this.user!.password,
          result.file,
        ).then((observable: any) => {
          observable.subscribe({
            next: () => {
              this.errorEditUser = false;
              this.router.navigate(['/user-detail', user.id]);
            },
            error: (error: Error) => {
              this.errorEditUser = true;
            }
          });
        });
      }
    });
  }
  volverAlPanel() {
    switch (this.user?.role) {
      case 'ROLE_ADMIN':
        this.router.navigate(['/admin-list']);
        break;
      case 'ROLE_MANAGER':
        this.router.navigate(['/manager-list']);
        break;
      case 'ROLE_USUARIO':
        this.router.navigate(['/user-list']);
        break;
      default:
        this.router.navigate(['/']);
        break;
    }
  }
}

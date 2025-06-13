import { Component, inject, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { UserService } from '../../../services/user.service';
import { Router } from '@angular/router';
import { DetailUser } from '../../../models/user/detail-user';
import { EditUserDialogComponent } from '../../Dialog/UserDialog/user-dialog';
import { FavoriteService } from '../../../services/favorite.service';
import { Favorito } from '../../../models/user/favorites.interface';

@Component({
  selector: 'app-my-profile',
  templateUrl: './my-profile.component.html',
  styleUrl: './my-profile.component.scss'
})
export class MyProfileComponent implements OnInit {
  readonly dialog = inject(MatDialog);
  user: DetailUser | undefined = undefined;
  showFavorites = false;
  favorites: Favorito[] = [];

  constructor(private userService: UserService, private route: Router, private favoriteService: FavoriteService) { }

  ngOnInit(): void {
    this.userService.getMe().subscribe(user => {
      this.user = user;
    });
  }
  limpiarUrlFoto(url: string | undefined | null): string {
    if (!url) return '';

    if (url.includes('randomuser.me')) {
      return url.replace('http://localhost:8080/download/', '');
    }

    if (url.startsWith('http')) return url;

    return `http://localhost:8080/download/${url}`;
  }
  openEditDialog(user: { id: string; username: string; correo: string; password: string; fotoPerfilUrl: string }): void {
    const dialogRef = this.dialog.open(EditUserDialogComponent, {
      width: '800px',
      data: { id: user.id, username: user.username, correo: user.correo, password: user.password, fotoPerfilUrl: user.fotoPerfilUrl }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {

        this.userService.editMe(
          result.username,
          result.correo,
          this.user!.password,
          result.file
        ).then((observable: any) => {
          observable.subscribe({
            next: () => {
              this.route.navigate(['/my-profile']);
            },
            error: (error: Error) => {
              console.error(error);
            }
          });
        });
      }
    });
  }
  toggleFavorites(): void {
    console.log(this.showFavorites);
    this.showFavorites = !this.showFavorites;
    if (this.showFavorites) {
      this.favoriteService.getFavorites().subscribe({
        next: (resp) => this.favorites = resp.contenido,
        error: (err) => {
          this.favorites = [];
          console.error('Error al cargar favoritos', err);
        }
      });
    }
  }
  goToProductDetail(productId: string): void {
    this.route.navigate(['/product-detail', productId]);
  }
}
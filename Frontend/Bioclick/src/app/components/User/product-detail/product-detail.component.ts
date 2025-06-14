import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../../services/product.service';
import { Producto } from '../../../models/user/get-all-products.interface';
import { Comentario } from '../../../models/user/get-all-comments';
import { UserService } from '../../../services/user.service';
import { Favorito } from '../../../models/user/favorites.interface';
import { FavoriteService } from '../../../services/favorite.service';
import { ActivatedRoute } from '@angular/router';
import { ValorationsService } from '../../../services/valoration.service';
import { Valoration } from '../../../models/user/get-all-valorations';
import { Location } from '@angular/common';

@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrl: './product-detail.component.scss'
})
export class ProductDetailComponent implements OnInit {
  product: Producto | undefined;
  listaComentarios: Comentario[] = [];

  isFavorite: boolean = false;
  favorites: Favorito[] = [];

  rating: number = 0;
  valorations: Valoration[] = [];
  myValoration: Valoration | null = null;
  showValorationForm = false;
  userRating = 0;

  newComment: string = '';
  currentUserId: string | null = null;
  currentUserRole: string | null = null;
  showEditError = false;
  editingCommentId: string | null = null;
  editedComment: string = '';
  constructor(private productService: ProductService, private userService: UserService,
    private favoriteService: FavoriteService, private route: ActivatedRoute,
    private valorationService: ValorationsService, private location: Location) { }

  ngOnInit(): void {
    const productId = this.route.snapshot.paramMap.get('id');
    if (productId) {
      this.getProductDetail(productId);
      this.loadValorations(productId);
    }
    this.loadFavorites();
    this.rating = this.getAverageValoration();
    this.currentUserId = localStorage.getItem('userId');
    this.currentUserRole = localStorage.getItem('role');
  }
  limpiarUrlFoto(url: string | undefined | null): string {
    if (!url) return '';

    if (url.includes('assets/')) {
      return url.replace('http://localhost:8080/download/', '');
    }

    if (url.startsWith('http')) return url;

    return `http://localhost:8080/download/${url}`;
  }
  getProductDetail(productId: string): void {
    this.productService.getProductDetail(productId).subscribe({
      next: (response) => {
        this.product = response;
      },
      error: (error) => {
        console.error('Error fetching product detail:', error);
      }
    });
    this.productService.getAllComments(productId, 0).subscribe({
      next: (response) => {
        this.listaComentarios = response.contenido;
      },
      error: (error) => {
        console.error('Error fetching comments:', error);
      }
    });
  }
  formatDate(dateString: string) {
    const options: Intl.DateTimeFormatOptions = { year: 'numeric', month: 'long', day: 'numeric' };
    return new Date(dateString).toLocaleDateString('es-ES', options);
  }
  loadFavorites() {
    this.favoriteService.getFavorites().subscribe({
      next: (resp) => {
        this.favorites = resp.contenido;
        if (this.product) {
          this.isFavorite = this.favorites.some(fav => fav.idProducto === this.product?.id);
        }
      },
      error: () => this.favorites = []
    });
  }
  toggleFavorite(): void {
    if (!this.product) return;
    if (this.isFavorite) {
      this.favoriteService.removeFromFavorites(this.product.id).subscribe({
        next: () => {
          this.isFavorite = false;
          this.loadFavorites();
        }
      });
    } else {
      this.favoriteService.addToFavorite(this.product.id).subscribe({
        next: () => {
          this.isFavorite = true;
          this.loadFavorites();
        }
      });
    }
  }
  getIsValorated() {
    this.valorationService.getMyValorations().subscribe({
      next: (resp) => {
        this.myValoration = resp.contenido.find(val => val.productoId === this.product?.id) || null;
        if (this.myValoration) {
          this.userRating = this.myValoration.puntuacion || 0;
        } else {
          this.userRating = 0;
        }
      }
    });
  }

  addValoration(puntuacion: number, user_id: string, producto_id: string) {
    this.valorationService.addValoration(puntuacion, user_id, producto_id).subscribe({
      next: () => {
        this.loadValorations(producto_id);
      },
      error: (err) => {
        console.error('Error al añadir valoración', err);
      }
    });
  }
  loadValorations(productId: string): void {
    this.valorationService.getValorationsByProduct(productId).subscribe({
      next: (resp) => {
        this.valorations = resp.contenido;
        this.userService.getMe().subscribe((currentUser) => {
          this.myValoration = this.valorations.find(
            v => v.nombreUsuario === currentUser.username
          ) || null;
        });
      }
    });
  }
  deleteValoration(valoracionId: string, producto_id: string): void {
    this.valorationService.deleteValoration(valoracionId).subscribe({
      next: () => {
        this.loadValorations(producto_id);
      },
      error: (err: Error) => {
        console.error('Error al eliminar valoración', err);
      }
    });
  }
  getAverageValoration(): number {
    if (!this.valorations.length) return 0;
    const sum = this.valorations.reduce((acc, val) => acc + (val.puntuacion || 0), 0);
    return +(sum / this.valorations.length).toFixed(1);
  }
  getRatingColorClass(): string {
    const avg = this.getAverageValoration();
    if (avg < 1.5) return 'text-danger';
    if (avg < 3.5) return 'text-warning';
    return 'text-success';
  }
  openValorationForm() {
    this.showValorationForm = true;
  }
  submitValoration(puntuacion: number, producto_id: string) {
    const userId = localStorage.getItem('userId');
    if (userId) {
      this.addValoration(puntuacion, userId, producto_id);
    } else {
      console.error('User ID not found in localStorage');
    }
    this.showValorationForm = false;
  }
  goBack(): void {
    this.location.back();
  }
  addComment(comment: string): void {
    if (!this.product) return;
    const userId = localStorage.getItem('userId');
    if (userId) {
      this.userService.addComment(userId, comment, this.product.id).subscribe({
        next: (resp: Comentario) => {
          this.listaComentarios.push(resp);
        },
        error: (err: Error) => {
          console.error('Error adding comment:', err);
        }
      });
    } else {
      console.error('User ID not found in localStorage');
    }
  }
  startEditComment(comment: Comentario) {
    this.editingCommentId = comment.id;
    this.editedComment = comment.comentario;
  }

  cancelEditComment() {
    this.editingCommentId = null;
    this.editedComment = '';
  }

  saveEditComment(commentId: string) {
    this.userService.editComment(commentId, this.editedComment).subscribe({
      next: (updated) => {
        this.showEditError = false;
        const idx = this.listaComentarios.findIndex(c => c.id === commentId);
        if (idx !== -1) {
          this.listaComentarios[idx].comentario = updated.comentario;
        }
        this.cancelEditComment();
      },
      error: () => {
        this.showEditError = true;
      }
    });
  }
  deleteComment(commentId: string) {
    this.userService.deleteComment(commentId).subscribe({
      next: () => {
        this.listaComentarios = this.listaComentarios.filter(c => c.id !== commentId);
      },
      error: (err: Error) => {
        console.error('Error deleting comment:', err);
      }
    });
  }
  canDeleteComment(comentario: Comentario): boolean {
    return (
      comentario.usuarioId === this.currentUserId ||
      this.currentUserRole === 'ROLE_ADMIN' ||
      this.currentUserRole === 'ROLE_MANAGER'
    );
  }
}


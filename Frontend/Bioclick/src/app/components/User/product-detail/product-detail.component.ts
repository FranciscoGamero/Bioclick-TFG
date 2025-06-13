import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../../services/product.service';
import { Producto } from '../../../models/user/get-all-products.interface';
import { Comentario } from '../../../models/user/get-all-comments';
import { UserService } from '../../../services/user.service';
import { Favorito } from '../../../models/user/favorites.interface';
import { FavoriteService } from '../../../services/favorite.service';
import { ActivatedRoute } from '@angular/router';

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
  constructor(private productService: ProductService, private userService: UserService, private favoriteService: FavoriteService, private route: ActivatedRoute) { }

  ngOnInit(): void {
    const productId = this.route.snapshot.paramMap.get('id');
    if (productId) {
      this.getProductDetail(productId);
    }
    this.loadFavorites();
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
}

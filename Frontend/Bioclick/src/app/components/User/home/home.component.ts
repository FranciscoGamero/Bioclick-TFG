import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { HomeService } from '../../../services/home.service';
import { Producto } from '../../../models/user/get-all-products.interface';
import { Favorito } from '../../../models/user/favorites.interface';
import { FavoriteService } from '../../../services/favorite.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent implements OnInit {
  showLiked: boolean = true;
  pageLiked: number = 0;
  totalPagesLiked: number = 1;
  moreLikedProducts: Producto[] = [];

  showValorated: boolean = true;
  pageValorated: number = 0;
  totalPagesValorated: number = 1;
  moreValoratedProducts: Producto[] = [];

  favorites: Favorito[] = [];

  nombreProducto: string = '';
  showBuscados: boolean = false;
  pageBuscada: number = 0;
  totalPagesBuscada: number = 1;
  totalBuscados: Producto[] = [];

  constructor(private homeService: HomeService, private router: Router, private favoriteService: FavoriteService) { }

  ngOnInit(): void {
    this.pageLiked = 0;
    this.pageValorated = 0;
    this.pageBuscada = 0;
    this.loadMoreLikedProducts();
    this.loadMoreValoratedProducts();
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
  loadMoreLikedProducts(): void {
    this.homeService.getMoreLiked(this.pageLiked, 6).subscribe({
      next: (response) => {
        this.moreLikedProducts = [...this.moreLikedProducts, ...response.contenido];
        this.totalPagesLiked = response.paginasTotales;
        this.showLiked = true;
        this.showValorated = true;
        this.showBuscados = false;
        this.pageLiked++;
      },
      error: (error) => {
        console.error('Error fetching more liked products:', error);
      }
    });
  }

  loadMoreValoratedProducts(): void {
    this.homeService.getMoreValorated(this.pageValorated, 6).subscribe({
      next: (response) => {
        this.moreValoratedProducts = [...this.moreValoratedProducts, ...response.contenido];
        this.totalPagesValorated = response.paginasTotales;
        this.showLiked = true;
        this.showValorated = true;
        this.showBuscados = false;
        this.pageValorated++;
      },
      error: (error) => {
        console.error('Error fetching more valorated products:', error);
      }
    });
  }
  goToProductDetail(productId: string): void {
    this.router.navigate(['/product-detail', productId]);
  }
  loadFavorites(): void {
    this.favoriteService.getFavorites().subscribe({
      next: (resp) => this.favorites = resp.contenido,
      error: () => this.favorites = []
    });
  }

  isFavorite(producto: Producto): boolean {
    return this.favorites.some(fav => fav.nombreProducto === producto.nombreProducto);
  }

  toggleFavorite(producto: Producto): void {
    if (this.isFavorite(producto)) {
      this.favoriteService.removeFromFavorites(producto.id).subscribe({
        next: () => {
          this.favorites = this.favorites.filter(fav => fav.nombreProducto !== producto.nombreProducto);
        }
      });
    } else {
      this.favoriteService.addToFavorite(producto.id).subscribe({
        next: () => {
          this.favorites = [
            ...this.favorites,
            {
              nombreProducto: producto.nombreProducto, idProducto: producto.id,
              nombreUsuario: this.favorites.at(0)!.nombreUsuario, idUsuario: this.favorites.at(0)!.idUsuario,
              fechaFavorito: new Date().toISOString()
            }
          ];
        }
      });
    }
  }
  buscarPorNombre(): void {
    this.pageBuscada = 0;
    this.homeService.getProductsByName(this.nombreProducto, this.pageBuscada, 12).subscribe({
      next: (response) => {
        this.showLiked = false;
        this.showValorated = false;
        this.totalBuscados = response.contenido;
        this.showBuscados = true;
        this.pageBuscada++;
        this.moreLikedProducts = [];
        this.moreValoratedProducts = [];
      },
      error: (error) => {
        console.error('Error fetching products by name:', error);
      }
    });
  }
  loadHome(): void {
    this.showLiked = true;
    this.showValorated = true;
    this.showBuscados = false;
    this.nombreProducto = '';
    this.ngOnInit();
  }
}
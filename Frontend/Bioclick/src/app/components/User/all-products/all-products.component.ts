import { Component, OnInit } from '@angular/core';
import { Favorito } from '../../../models/user/favorites.interface';
import { AllProductsResponse, Producto } from '../../../models/user/get-all-products.interface';
import { FavoriteService } from '../../../services/favorite.service';
import { ProductService } from '../../../services/product.service';
import { Router } from '@angular/router';
import { HomeService } from '../../../services/home.service';
import { Categoria } from '../../../models/user/get-all-categories';
import { CategoryService } from '../../../services/category.service';

@Component({
  selector: 'app-all-products',
  templateUrl: './all-products.component.html',
  styleUrl: './all-products.component.scss'
})
export class AllProductsComponent implements OnInit {
  page: number = 0;
  totalPages: number = 1;
  allProducts: Producto[] = [];
  favorites: Favorito[] = [];

  nombreProducto: string = '';
  showBuscados: boolean = false;
  pageBuscada: number = 0;
  totalPagesBuscada: number = 1;

  categorias: Categoria[] = [];
  filtroCategoria: string = '';
  precioMin: number | null = null;
  precioMax: number | null = null;
  constructor(private productService: ProductService, private favoriteService: FavoriteService,
    private router: Router, private homeService: HomeService, private categoryService: CategoryService) { }
  ngOnInit(): void {
    this.loadCategorias();
    this.loadAllProducts();
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
  loadAllProducts(): void {
    this.productService.getAllProducts(this.page).subscribe({
      next: (response) => {
        this.allProducts = [...this.allProducts, ...response.contenido];
        this.totalPages = response.paginasTotales;
        this.page++;
      }
    });
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
  goToProductDetail(productId: string): void {
    this.router.navigate(['/product-detail', productId]);
  }
  buscarPorNombre(): void {
    this.homeService.getProductsByName(this.nombreProducto, this.pageBuscada, 12).subscribe({
      next: (response) => {
        this.allProducts = response.contenido;
      },
      error: (error) => {
        console.error('Error fetching products by name:', error);
      }
    });
  }
  loadHome(): void {
    this.showBuscados = false;
    this.nombreProducto = '';
    this.ngOnInit();
  }
  loadCategorias(): void {
    this.categoryService.getAllCategories(0).subscribe({
      next: resp => { this.categorias = resp.contenido; },
      error: () => this.categorias = []
    });
  }
  aplicarFiltros() {
    this.page = 0;
    this.homeService.getProductosFiltrados(
      this.filtroCategoria,
      this.precioMin,
      this.precioMax,
      this.page
    ).subscribe(response => {
      this.allProducts = response.contenido;
    });
  }
  formatCategoriaName(nombreCategoria: string): string {
    let conEspacios = nombreCategoria.replace("_", " ")
    return conEspacios.charAt(0).toUpperCase() + conEspacios.slice(1);
  }
}

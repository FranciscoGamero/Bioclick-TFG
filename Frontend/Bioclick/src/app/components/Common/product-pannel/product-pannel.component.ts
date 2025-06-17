import { Component, inject } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';

import { ProductService } from '../../../services/product.service';
import { AllProductsResponse } from '../../../models/user/get-all-products.interface';
import { CreateProductoDialogComponent, DeleteProductoDialogComponent, EditProductoDialogComponent } from '../../Dialog/CommonDialog/product-dialog';
import { Router } from '@angular/router';
import { HomeService } from '../../../services/home.service';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-product-pannel',
  templateUrl: './product-pannel.component.html',
  styleUrl: './product-pannel.component.scss'
})
export class ProductPannelComponent {
  readonly dialog = inject(MatDialog);
  isExpanded: boolean = true;
  productsFound: AllProductsResponse | undefined = undefined;
  page: number = 1;

  pageBuscada: number = 0;
  nombreProducto: string = '';
  showBuscados: boolean = false;

  showAlert: boolean = false;
  alertMessage: string = '';
  ngOnInit(): void {
    this.getProducts();
  }

  constructor(private productService: ProductService, private router: Router, private homeService: HomeService) { }

  limpiarUrlFoto(url: string | undefined | null): string {
    if (!url) return '';

    if (url.includes('assets/')) {
      return url.replace(`${environment.apiBaseUrl}/download/`, '');
    }

    if (url.startsWith('http')) return url;

    return `${environment.apiBaseUrl}/download/${url}`;
  }
  getProducts() {
    this.productService.getAllProducts(this.page - 1).subscribe({
      next: (response) => {
        this.productsFound = response;
      },
      error: (error) => {
        console.error('Error fetching users:', error);
      }
    });
  }

  handleSidebarToggle() {
    this.isExpanded = !this.isExpanded;
  }
  onCardClick(productId: string) {
    this.router.navigate(['/product-detail', productId]);
  }
  openCreateDialog(): void {
    const dialogRef = this.dialog.open(CreateProductoDialogComponent, {
      width: '800px',
      data: {
        nombreProducto: '',
        descripcion: '',
        precioProducto: 0,
        fabricante: '',
        estado: '',
        idCategoria: ''
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result && result.selectedFile) {
        this.productService.createProduct(
          result.nombreProducto,
          result.descripcion,
          result.precioProducto,
          result.fabricante,
          result.estado,
          result.idCategoria,
          result.selectedFile
        ).subscribe({
          next: () => {
            this.showAlert = false;
            this.getProducts();
          },
            error: (error: {
              error?: {
                'invalid-params'?: { message: string }[];
                detail?: string;
              };
            }): void => {
              this.showAlert = true;
              if (error.error && error.error['invalid-params'] && error.error['invalid-params'].length > 0) {
                this.alertMessage = error.error['invalid-params'][0].message;
              } else if (error.error && error.error.detail) {
                this.alertMessage = error.error.detail;
              } else {
                this.alertMessage = 'Error al crear el manager.';
              }
            }
        });
      }
    });
  }
  openEditDialog(product: { id: string; nombreProducto: string; descripcion: string; precioProducto: number; estado: string; fotoPerfilUrl: string }): void {
    const dialogRef = this.dialog.open(EditProductoDialogComponent, {
      width: '800px',
      data: { id: product.id, nombreProducto: product.nombreProducto, descripcion: product.descripcion, precioProducto: product.precioProducto, estado: product.estado, fotoPerfilUrl: product.fotoPerfilUrl }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.productService.editProduct(
          result.id,
          result.nombreProducto,
          result.descripcion,
          result.precioProducto,
          result.estado,
          result.selectedFile,
        ).subscribe({
          next: () => {
            this.showAlert = false;
            this.getProducts();
          },
          error: (error) => {
            this.showAlert = true;
            if (error.error && error.error['invalid-params'] && error.error['invalid-params'].length > 0) {
              this.alertMessage = error.error['invalid-params'][0].message;
            } else if (error.error && error.error.detail) {
              this.alertMessage = error.error.detail;
            } else {
              this.alertMessage = 'Error al crear la categoría.';
            }

          }
        });
      }
    });
  }
  openDeleteDialog(product: { id: string; }): void {
    const dialogRef = this.dialog.open(DeleteProductoDialogComponent, {
      width: '800px',
      data: { id: product.id }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.productService.deleteProduct(
          result.id
        ).subscribe({
          next: () => {
            this.getProducts();
          }
        });
      }
    });
  }
  buscarPorNombre(): void {
    this.pageBuscada = 0;
    this.homeService.getProductsByName(this.nombreProducto, this.pageBuscada, 12).subscribe({
      next: (response) => {
        this.showAlert = false;
        this.productsFound = response;
        this.showBuscados = true;
        this.pageBuscada++;
      },
      error: (error) => {
        this.showAlert = true;
        if (error.error && error.error['invalid-params'] && error.error['invalid-params'].length > 0) {
          this.alertMessage = error.error['invalid-params'][0].message;
        } else if (error.error && error.error.detail) {
          this.alertMessage = error.error.detail;
        }
      }
    });
  }
  loadPannel(): void {
    this.showBuscados = false;

    this.nombreProducto = '';
    this.ngOnInit();
  }
}
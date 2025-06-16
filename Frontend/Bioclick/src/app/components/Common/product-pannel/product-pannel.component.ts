import { Component, inject } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';

import { ProductService } from '../../../services/product.service';
import { AllProductsResponse } from '../../../models/user/get-all-products.interface';
import { CreateProductoDialogComponent, DeleteProductoDialogComponent, EditProductoDialogComponent } from '../../Dialog/CommonDialog/product-dialog';
import { Router } from '@angular/router';

@Component({
  selector: 'app-product-pannel',
  templateUrl: './product-pannel.component.html',
  styleUrl: './product-pannel.component.scss'
})
export class ProductPannelComponent {
  readonly dialog = inject(MatDialog);
  isExpanded: boolean = true;
  name: string = '';
  productsFound: AllProductsResponse | undefined = undefined;
  page: number = 1;

  errorCreateProducto: boolean = false;
  errorEditProduct: boolean = false;
  ngOnInit(): void {
    this.getProducts();
  }

  constructor(private productService: ProductService, private router: Router) { }

  limpiarUrlFoto(url: string | undefined | null): string {
    if (!url) return '';

    if (url.includes('assets/')) {
      return url.replace('http://localhost:8080/download/', '');
    }

    if (url.startsWith('http')) return url;

    return `http://localhost:8080/download/${url}`;
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
    this.router.navigate(['/product-detail', productId ]);
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
            this.errorCreateProducto = false;
            this.getProducts();
          },
          error: (error: Error) => {
            this.errorCreateProducto = true;
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
            this.errorEditProduct = false;
            this.getProducts();
          },
          error: (error) => {
            console.error('Error editing product:', error);
            this.errorEditProduct = true;
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
}
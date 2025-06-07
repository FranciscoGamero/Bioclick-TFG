import { Component, inject } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';

import { ProductService } from '../../../services/product.service';
import { AllProductsResponse } from '../../../models/user/get-all-products.interface';

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
  ngOnInit(): void {
    this.getProducts();
  }

  constructor(private productService: ProductService) { }

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
  onCardClick(userId: string) {
    console.log('Card clicked for user ID:', userId);
  }

}
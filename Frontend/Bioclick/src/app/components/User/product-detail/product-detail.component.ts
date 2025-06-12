import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../../services/product.service';
import { Producto } from '../../../models/user/get-all-products.interface';
import { Comentario } from '../../../models/user/get-all-comments';
import { UserService } from '../../../services/user.service';

@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrl: './product-detail.component.scss'
})
export class ProductDetailComponent implements OnInit {
  product: Producto | undefined;
  listaComentarios: Comentario[] = [];
  constructor(private productService: ProductService, private userService: UserService) { }

  ngOnInit(): void {
    this.getProductDetail();
  }

  limpiarUrlFoto(url: string | undefined | null): string {
    if (!url) return '';

    if (url.includes('assets/')) {
      return url.replace('http://localhost:8080/download/', '');
    }

    if (url.startsWith('http')) return url;

    return `http://localhost:8080/download/${url}`;
  }
  getProductDetail() {
    this.productService.getProductDetail('def45678-9012-3456-ab78-901234567890').subscribe({
      next: (response) => {
        this.product = response;
      },
      error: (error) => {
        console.error('Error fetching product detail:', error);
      }
    });
    this.productService.getAllComments('def45678-9012-3456-ab78-901234567890', 0).subscribe({
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
}

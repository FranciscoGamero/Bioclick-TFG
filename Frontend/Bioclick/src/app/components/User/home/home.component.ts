import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { HomeService } from '../../../services/home.service';
import { Producto } from '../../../models/user/get-all-products.interface';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent implements OnInit {
  pageLiked: number = 0;
  totalPagesLiked: number = 1;
  moreLikedProducts: Producto[] = [];

  pageValorated: number = 0;
  totalPagesValorated: number = 1;
  moreValoratedProducts: Producto[] = [];

  constructor(private homeService: HomeService, private router: Router) { }

  ngOnInit(): void {
    this.loadMoreLikedProducts();
    this.loadMoreValoratedProducts();
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
        this.pageValorated++;
      },
      error: (error) => {
        console.error('Error fetching more valorated products:', error);
      }
    });
  }
}

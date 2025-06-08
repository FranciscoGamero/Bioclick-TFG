import { Component, inject } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { AllCategoryResponse } from '../../../models/user/get-all-categories';
import { CategoryService } from '../../../services/category.service';

@Component({
  selector: 'app-category-pannel',
  templateUrl: './category-pannel.component.html',
  styleUrl: './category-pannel.component.scss'
})
export class CategoryPannelComponent {
readonly dialog = inject(MatDialog);
  isExpanded: boolean = true;
  name: string = '';
  categoriesFound: AllCategoryResponse | undefined = undefined;
  page: number = 1;
  ngOnInit(): void {
    this.getCategories();
  }

  constructor(private categoryService: CategoryService) { }

  getCategories() {
    this.categoryService.getAllCategories(this.page - 1).subscribe({
      next: (response) => {
        this.categoriesFound = response;
      },
      error: (error) => {
        console.error('Error fetching categories:', error);
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

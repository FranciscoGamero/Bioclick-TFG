import { Component, inject } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { AllCategoryResponse, Categoria } from '../../../models/user/get-all-categories';
import { CategoryService } from '../../../services/category.service';
import { CreateCategoriaDialogComponent, DeleteCategoriaDialogComponent, EditCategoriaDialogComponent } from '../../Dialog/CommonDialog/category-dialog';

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
  errorCreateCategoria: boolean = false;
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
  openCreateDialog(): void {
    const dialogRef = this.dialog.open(CreateCategoriaDialogComponent, {
      width: '800px',
      data: {
        nombreCategoria: '',
        categoriaPadreId: '',
        subcategoriaIds: []
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result && result.nombreCategoria) {  // O la propiedad que estés usando para validar
        this.categoryService.createCategory(
          result.nombreCategoria,
          result.subcategoriaIds,
          result.categoriaPadreId
        ).subscribe({
          next: () => {
            this.errorCreateCategoria = false;
            this.getCategories();
          },
          error: (error: Error) => {
            console.log(error);
            this.errorCreateCategoria = true;
          }
        });
      }
    });
  }
  openEditDialog(categoria: Categoria): void {
    console.log(categoria);
    const dialogRef = this.dialog.open(EditCategoriaDialogComponent, {
      width: '800px',
      data: {
        id: categoria.id,
        nombreCategoria: categoria.nombreCategoria,
        categoriaPadreId: categoria.idCategoriaPadre ?? '',
        subcategoriaIds: categoria.listaIdSubcategorias ?? [] // <-- aquí el mapeo correcto
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result && result.selectedFile) {
        this.categoryService.editCategory(
          categoria.id,
          result.nombreCategoria,
          result.subcategoriaIds,
          result.categoriaPadreId
        ).subscribe({
          next: () => {
            this.errorCreateCategoria = false;
            this.getCategories();
          },
          error: (error: Error) => {
            console.log(error)
            this.errorCreateCategoria = true;
          }
        });
      }
    });
  }
  openDeleteDialog(categoria: { idCategoria: string; }): void {
    const dialogRef = this.dialog.open(DeleteCategoriaDialogComponent, {
      width: '800px',
      data: { id: categoria.idCategoria }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.categoryService.deleteCategory(
          result.id
        ).subscribe({
          next: () => {
            this.getCategories();
          }
        });
      }
    });
  }
}

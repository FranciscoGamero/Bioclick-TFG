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

  showError: boolean = false;
  alertMessage: string = '';
  ngOnInit(): void {
    this.getCategories();
  }

  constructor(private categoryService: CategoryService) { }

  getCategories() {
    this.categoryService.getAllCategories(this.page - 1).subscribe({
      next: (response) => {
        this.showError = false;
        this.categoriesFound = response;
      },
      error: (error) => {
        this.showError = true;
        if (error.error && error.error['invalid-params'] && error.error['invalid-params'].length > 0) {
          this.alertMessage = error.error['invalid-params'][0].message;
        } else if (error.error && error.error.detail) {
          this.alertMessage = error.error.detail;
        } else {
          this.alertMessage = 'Error al obtener las categorías.';
        }
      }
    });
  }
  formatCategoriaName(nombreCategoria: string): string {
    let conEspacios = nombreCategoria.replace("_", " ")
    return conEspacios.charAt(0).toUpperCase() + conEspacios.slice(1);
  }
  handleSidebarToggle() {
    this.isExpanded = !this.isExpanded;
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
            this.showError = false;
            this.getCategories();
          },
          error: (error) => {
            this.showError = true;
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
  openEditDialog(categoria: Categoria): void {
    const dialogRef = this.dialog.open(EditCategoriaDialogComponent, {
      width: '800px',
      data: {
        id: categoria.id,
        nombreCategoria: categoria.nombreCategoria,
        categoriaPadreId: categoria.idCategoriaPadre ?? '',
        subcategoriaIds: categoria.listaIdSubcategorias ?? []
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
            this.showError = false;
            this.getCategories();
          },
          error: (error) => {
            if (error.error && error.error['invalid-params'] && error.error['invalid-params'].length > 0) {
              this.alertMessage = error.error['invalid-params'][0].message;
            } else if (error.error && error.error.detail) {
              this.alertMessage = error.error.detail;
            } else {
              this.alertMessage = 'Error al editar la categoria.';
            }
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

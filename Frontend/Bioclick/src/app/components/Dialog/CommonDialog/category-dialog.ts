import { Inject, Component, OnInit } from "@angular/core";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { Categoria } from "../../../models/user/get-all-categories";
import { CategoryService } from "../../../services/category.service";

export interface CategoriaDialogData {
  id?: string;
  nombreCategoria: string;
  categoriaPadreId?: string;
  subcategoriaIds: string[];
}
@Component({
  selector: 'app-create-categoria-dialog',
  template: `
    <h2 mat-dialog-title>Crear Categoría</h2>
    <mat-dialog-content>
      <form class="w-100 d-flex flex-column align-items-center justify-content-center">
        <div class="mb-3" style="width: 350px;">
          <label for="nombreCategoria" class="form-label">Nombre de la Categoría</label>
          <input type="text" class="form-control" id="nombreCategoria" name="nombreCategoria"
            placeholder="Ingresa el nombre de la categoría" [(ngModel)]="data.nombreCategoria">
        </div>
        <div class="mb-3" style="width: 350px;">
          <label for="categoriaPadreId" class="form-label">Categoria Padre</label>
          <select class="form-select" [(ngModel)]="data.categoriaPadreId" name="categoriaPadreId">
            <option *ngFor="let cat of categorias" [value]="cat.id">{{ formatCategoriaName(cat.nombreCategoria) }}</option>
          </select>
        </div>
        <div class="mb-3" style="width: 350px;">
          <label class="form-label fw-bold">Subcategorías</label>
          <div class="subcategorias-checkbox-list">
            <div
              class="form-check"
              *ngFor="let cat of categorias"
              [hidden]="cat.id === data.categoriaPadreId"
            >
              <input
                class="form-check-input"
                type="checkbox"
                [id]="'subcat-' + cat.id"
                [value]="cat.id"
                [checked]="data.subcategoriaIds.includes(cat.id)"
                (change)="onSubcategoriaChange($event, cat.id)"
              />
              <label class="form-check-label" [for]="'subcat-' + cat.id">
                {{ formatCategoriaName(cat.nombreCategoria) }}
              </label>
            </div>
          </div>
        </div>
      </form>
    </mat-dialog-content>
    <mat-dialog-actions align="center">
      <button class="btn btn-danger mx-auto" (click)="onCancel()">Cancelar</button>
      <button class="btn btn-success mx-auto" (click)="onSave()">Guardar</button>
    </mat-dialog-actions>
    `
})
export class CreateCategoriaDialogComponent implements OnInit {
  selectedFile: File | null = null;
  categorias: Categoria[] = [];
  page: number = 0;
  constructor(
    public dialogRef: MatDialogRef<CreateCategoriaDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: CategoriaDialogData,
    private categoriaService: CategoryService
  ) { }
  ngOnInit() {
    this.categorias = [];
    this.page = 0;
    this.cargarCategoriasPaginadas(this.page);
  }
  cargarCategoriasPaginadas(page: number) {
    this.categoriaService.getAllCategories(page).subscribe({
      next: resp => {
        this.categorias.push(...resp.contenido);
        if (page < resp.paginasTotales - 1) {
          this.cargarCategoriasPaginadas(page + 1);
        }
      },
      error: () => this.categorias = []
    });
  }
  formatCategoriaName(nombreCategoria: string): string {
    let conEspacios = nombreCategoria.replace("_", " ")
    return conEspacios.charAt(0).toUpperCase() + conEspacios.slice(1);
  }
  onSubcategoriaChange(event: any, id: string) {
    if (event.target.checked) {
      this.data.subcategoriaIds.push(id);
    } else {
      this.data.subcategoriaIds = this.data.subcategoriaIds.filter(x => x !== id);
    }
  }
  onCancel(): void {
    this.dialogRef.close();
  }

  onSave(): void {
    this.dialogRef.close({
      ...this.data,
      selectedFile: this.selectedFile
    });
  }
  onFileSelected(event: any) {
    if (event && event.target.files.length > 0) {
      this.selectedFile = event.target.files[0];
    }
  }
}
@Component({
  selector: 'app-create-categoria-dialog',
  template: `
    <h2 mat-dialog-title>Crear Categoría</h2>
    <mat-dialog-content>
      <form class="w-100 d-flex flex-column align-items-center justify-content-center">
        <div class="mb-3" style="width: 350px;">
          <label for="nombreCategoria" class="form-label">Nombre de la Categoría</label>
          <input type="text" class="form-control" id="nombreCategoria" name="nombreCategoria"
            placeholder="Ingresa el nombre de la categoría" [(ngModel)]="data.nombreCategoria">
        </div>
        <div class="mb-3" style="width: 350px;">
          <label for="categoriaPadreId" class="form-label">Categoria Padre</label>
          <select class="form-select" [(ngModel)]="data.categoriaPadreId" name="categoriaPadreId">
            <option *ngFor="let cat of categorias" [value]="cat.id">{{ formatCategoriaName(cat.nombreCategoria) }}</option>
          </select>
        </div>
        <div class="mb-3" style="width: 350px;">
          <label class="form-label fw-bold">Subcategorías</label>
          <div class="subcategorias-checkbox-list">
            <div
              class="form-check"
              *ngFor="let cat of categorias"
              [hidden]="cat.id === data.categoriaPadreId"
            >
              <input
              class="form-check-input"
              type="checkbox"
              [id]="'subcat-' + cat.id"
              [value]="cat.id"
              [checked]="data.subcategoriaIds.includes(cat.id)"
              (change)="onSubcategoriaChange($event, cat.id)"
            />
              <label class="form-check-label" [for]="'subcat-' + cat.id">
                {{ formatCategoriaName(cat.nombreCategoria) }}
              </label>
            </div>
          </div>
        </div>
      </form>
    </mat-dialog-content>
    <mat-dialog-actions align="center">
      <button class="btn btn-danger mx-auto" (click)="onCancel()">Cancelar</button>
      <button class="btn btn-success mx-auto" (click)="onSave()">Guardar</button>
    </mat-dialog-actions>
    `
})
export class EditCategoriaDialogComponent {
  selectedFile: File | null = null;
  categorias: Categoria[] = [];
  page: number = 0;
  constructor(
    public dialogRef: MatDialogRef<EditCategoriaDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: CategoriaDialogData,
    private categoriaService: CategoryService
  ) { }

  ngOnInit() {
    if (!this.data.subcategoriaIds) {
      this.data.subcategoriaIds = [];
    }
    this.categorias = [];
    this.page = 0;
    this.cargarCategoriasPaginadas(this.page);
  }
  cargarCategoriasPaginadas(page: number) {
    this.categoriaService.getAllCategories(page).subscribe({
      next: resp => {
        this.categorias.push(...resp.contenido);
        if (page < resp.paginasTotales - 1) {
          this.cargarCategoriasPaginadas(page + 1);
        }
      },
      error: () => this.categorias = []
    });
  }
  formatCategoriaName(nombreCategoria: string): string {
    let conEspacios = nombreCategoria.replace("_", " ")
    return conEspacios.charAt(0).toUpperCase() + conEspacios.slice(1);
  }
  onSubcategoriaChange(event: any, id: string) {
    if (event.target.checked) {
      this.data.subcategoriaIds.push(id);
    } else {
      this.data.subcategoriaIds = this.data.subcategoriaIds.filter(x => x !== id);
    }
  }
  onCancel(): void {
    this.dialogRef.close();
  }

  onSave(): void {
    this.dialogRef.close({
      ...this.data,
      selectedFile: this.selectedFile
    });
  }
  onFileSelected(event: any) {
    if (event && event.target.files.length > 0) {
      this.selectedFile = event.target.files[0];
    }
  }
}

@Component({
  selector: 'app-delete-categoria-dialog',
  template: `
    <h2 mat-dialog-title>Eliminar categoria</h2>
    <mat-dialog-content>
      <form class="w-100 d-flex flex-column align-items-center justify-content-center">
        <p>¿Estás seguro de que deseas eliminar este categoria?</p>
      </form>
    </mat-dialog-content>
    <mat-dialog-actions align="center">
      <button class="btn btn-danger mx-auto" (click)="onCancel()">Cancelar</button>
      <button class="btn btn-success mx-auto" (click)="onSave()">Eliminar</button>
    </mat-dialog-actions>
  `
})
export class DeleteCategoriaDialogComponent {
  constructor(
    public dialogRef: MatDialogRef<DeleteCategoriaDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: CategoriaDialogData
  ) { }

  onCancel(): void {
    this.dialogRef.close();
  }

  onSave(): void {
    this.dialogRef.close({
      ...this.data,
    });
  }
}
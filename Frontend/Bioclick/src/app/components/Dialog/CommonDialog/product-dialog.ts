import { Inject, Component, OnInit } from "@angular/core";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { Categoria } from "../../../models/user/get-all-categories";
import { CategoryService } from "../../../services/category.service";

export interface CreateProductoData {
  nombreProducto: string;
  descripcion: string;
  precioProducto: number;
  fabricante: string;
  estado: string;
  idCategoria: string; // <-- cambia aquí también
  fotoPerfilUrl?: string;
}
export interface EditProductoData {
  id: string;
  nombreProducto: string;
  descripcion: string;
  precioProducto: number;
  estado: string;
  fotoPerfilUrl?: string;
}
export interface DeleteProductoData {
  id: string;
}
@Component({
  selector: 'app-create-product-dialog',
  template: `
    <h2 mat-dialog-title>Crear Producto</h2>
    <mat-dialog-content>
      <form class="w-100 d-flex flex-column align-items-center justify-content-center">
        <div class="mb-3" style="width: 350px;">
          <label for="nombreProducto" class="form-label">Nombre del Producto</label>
          <input type="text" class="form-control" id="nombreProducto" name="nombreProducto"
            placeholder="Ingresa el nombre del producto" [(ngModel)]="data.nombreProducto">
        </div>
        <div class="mb-3" style="width: 350px;">
          <label for="descripcion" class="form-label">Descripción</label>
          <input type="text" class="form-control" id="descripcion" name="descripcion"
            placeholder="Ingresa la descripción" [(ngModel)]="data.descripcion">
        </div>
        <div class="mb-3" style="width: 350px;">
          <label for="precioProducto" class="form-label">Precio</label>
          <input type="number" class="form-control" id="precioProducto" name="precioProducto"
            placeholder="Ingresa el precio del producto" [(ngModel)]="data.precioProducto">
        </div>
        <div class="mb-3" style="width: 350px;">
          <label for="fabricante" class="form-label">Fabricante</label>
          <input type="text" class="form-control" id="fabricante" name="fabricante"
            placeholder="Ingresa el fabricante" [(ngModel)]="data.fabricante">
        </div>
        <div class="mb-3" style="width: 350px;">
          <label class="form-label">Estado</label>
          <select class="form-select" [(ngModel)]="data.estado" name="estado">
            <option *ngFor="let est of estados" [value]="est">{{ est }}</option>
          </select>
        </div>
        <div class="mb-3" style="width: 350px;">
          <label class="form-label">Categoría</label>
          <select class="form-select" [(ngModel)]="data.idCategoria" name="idCategoria">
            <option *ngFor="let cat of categorias" [value]="cat.id">{{ formatCategoriaName(cat.nombreCategoria) }}</option>
          </select>
        </div>
        <div class="mb-3" style="width: 350px;">
          <label for="formFile" class="form-label">Foto del Producto</label>
          <input class="form-control" type="file" id="formFile" (change)="onFileSelected($event)">
        </div>
      </form>
    </mat-dialog-content>
    <mat-dialog-actions align="center">
      <button class="btn btn-danger mx-auto" (click)="onCancel()">Cancelar</button>
      <button class="btn btn-success mx-auto" (click)="onSave()">Guardar</button>
    </mat-dialog-actions>
    `
})
export class CreateProductoDialogComponent implements OnInit {
  selectedFile: File | null = null;
  estados = ['Reacondicionado', 'Reciclado'];
  categorias: Categoria[] = [];

  constructor(
    public dialogRef: MatDialogRef<CreateProductoDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: CreateProductoData,
    private categoryService: CategoryService
  ) { }
  ngOnInit() {
    this.categoryService.getAllCategories(0).subscribe({
      next: resp => this.categorias = resp.contenido,
      error: () => this.categorias = []
    });
  }
  formatCategoriaName(nombreCategoria: string): string {
    let conEspacios = nombreCategoria.replace("_", " ")
    return conEspacios.charAt(0).toUpperCase() + conEspacios.slice(1);
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
  selector: 'app-edit-product-dialog',
  template: `
    <h2 mat-dialog-title>Editar Producto</h2>
    <mat-dialog-content>
      <form class="w-100 d-flex flex-column align-items-center justify-content-center">
        <div class="mb-3" style="width: 350px;">
          <label for="nombreProducto" class="form-label">Nombre del Producto</label>
          <input type="text" class="form-control" id="nombreProducto" name="nombreProducto"
            placeholder="Ingresa el nombre del producto" [(ngModel)]="data.nombreProducto">
        </div>
        <div class="mb-3" style="width: 350px;">
          <label for="descripcion" class="form-label">Descripción</label>
          <input type="text" class="form-control" id="descripcion" name="descripcion"
            placeholder="Ingresa la descripción" [(ngModel)]="data.descripcion">
        </div>
        <div class="mb-3" style="width: 350px;">
          <label for="precioProducto" class="form-label">Precio</label>
          <input type="number" class="form-control" id="precioProducto" name="precioProducto"
            placeholder="Ingresa el precio del producto" [(ngModel)]="data.precioProducto">
        </div>
        <div class="mb-3" style="width: 350px;">
          <label class="form-label">Estado</label>
          <select class="form-select" [(ngModel)]="data.estado" name="estado">
            <option *ngFor="let est of estados" [value]="est">{{ est }}</option>
          </select>
        </div>
        <div class="mb-3" style="width: 350px;">
          <label for="formFile" class="form-label">Foto del Producto</label>
          <input class="form-control" type="file" id="formFile" (change)="onFileSelected($event)">
        </div>
      </form>
    </mat-dialog-content>
    <mat-dialog-actions align="center">
      <button class="btn btn-danger mx-auto" (click)="onCancel()">Cancelar</button>
      <button class="btn btn-success mx-auto" (click)="onSave()">Guardar</button>
    </mat-dialog-actions>
    `
})
export class EditProductoDialogComponent {
  selectedFile: File | null = null;
  estados = ['Reacondicionado', 'Reciclado'];

  constructor(
    public dialogRef: MatDialogRef<EditProductoDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: EditProductoData
  ) { }

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
  selector: 'app-delete-producto-dialog',
  template: `
    <h2 mat-dialog-title>Eliminar Producto</h2>
    <mat-dialog-content>
      <form class="w-100 d-flex flex-column align-items-center justify-content-center">
        <p>¿Estás seguro de que deseas eliminar este producto?</p>
      </form>
    </mat-dialog-content>
    <mat-dialog-actions align="center">
      <button class="btn btn-danger mx-auto" (click)="onCancel()">Cancelar</button>
      <button class="btn btn-success mx-auto" (click)="onSave()">Eliminar</button>
    </mat-dialog-actions>
  `
})
export class DeleteProductoDialogComponent {
  constructor(
    public dialogRef: MatDialogRef<DeleteProductoDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DeleteProductoData
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
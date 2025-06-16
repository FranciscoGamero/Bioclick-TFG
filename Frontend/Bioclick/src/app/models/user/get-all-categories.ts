export interface AllCategoryResponse {
  numPagina: number
  tamanioPagina: number
  elementosEncontrados: number
  paginasTotales: number
  contenido: Categoria[]
}

export interface Categoria {
  id: string
  nombreCategoria: string
  nombreCategoriaPadre: string
  idCategoriaPadre: string | null
  nombresSubcategorias: string[]
  listaIdSubcategorias: string[]
  listaProductos: Producto[]
}

export interface Producto {
  id: string
  nombreProducto: string
  descripcion: string
  imagenProducto: string
  precioProducto: number
  estado: string
}

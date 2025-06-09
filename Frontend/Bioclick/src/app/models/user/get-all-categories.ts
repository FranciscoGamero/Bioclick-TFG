export interface AllCategoryResponse {
  numPagina: number
  tamanioPagina: number
  elementosEncontrados: number
  paginasTotales: number
  contenido: Contenido[]
}

export interface Contenido {
  id: string
  nombreCategoria: string
  nombreCategoriaPadre: string
  nombresSubcategorias: string[]
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

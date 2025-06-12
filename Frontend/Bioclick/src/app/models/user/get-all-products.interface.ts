export interface AllProductsResponse {
  numPagina: number
  tamanioPagina: number
  elementosEncontrados: number
  paginasTotales: number
  contenido: Producto[]
}

export interface Producto {
  id: string
  nombreProducto: string
  descripcion: string
  imagenProducto: string
  precioProducto: number
  estado: string
}

export interface AllProductsResponse {
  numPagina: number
  tamanioPagina: number
  elementosEncontrados: number
  paginasTotales: number
  contenido: Contenido[]
}

export interface Contenido {
  id: string
  nombreProducto: string
  descripcion: string
  imagenProducto: string
  precioProducto: number
  estado: string
}

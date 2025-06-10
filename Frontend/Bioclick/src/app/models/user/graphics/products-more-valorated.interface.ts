export interface ProductsMoreValoratedResponse {
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
  precioProducto: number
  mediaPuntuacion: number
}

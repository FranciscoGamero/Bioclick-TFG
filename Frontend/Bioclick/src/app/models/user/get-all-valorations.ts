export interface AllValorationsResponse {
  numPagina: number
  tamanioPagina: number
  elementosEncontrados: number
  paginasTotales: number
  contenido: Valoration[]
}

export interface Valoration {
  valoracionId: string
  nombreUsuario: string
  nombreProducto: string
  productoId: string
  puntuacion: number
  fechaValorado: string
}

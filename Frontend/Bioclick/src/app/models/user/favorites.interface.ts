export interface AllFavoritesResponse {
  numPagina: number
  tamanioPagina: number
  elementosEncontrados: number
  paginasTotales: number
  contenido: Favorito[]
}

export interface Favorito {
  nombreUsuario: string
  idUsuario: string
  nombreProducto: string
  idProducto: string
  fechaFavorito: string
}

export interface UsersMoreValorationsResponse {
  numPagina: number
  tamanioPagina: number
  elementosEncontrados: number
  paginasTotales: number
  contenido: usuariosEncontrados[]
}

export interface usuariosEncontrados {
  id: string
  username: string
  role: string
  valoracionesTotales: number
}

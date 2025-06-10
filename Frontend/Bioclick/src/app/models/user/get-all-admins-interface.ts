export interface AllAdminsResponse {
  numPagina: number
  tamanioPagina: number
  elementosEncontrados: number
  paginasTotales: number
  contenido: Contenido[]
}

export interface Contenido {
  id: string
  username: string
  correo: string
  fotoPerfilUrl: string
  fechaRegistro: string
}

export interface AllAdminsResponse {
  numPagina: number
  tamanioPagina: number
  elementosEncontrados: number
  paginasTotales: number
  contenido: Admin[]
}

export interface Admin {
  id: string
  username: string
  correo: string
  fotoPerfilUrl: string
  fechaRegistro: string
}

export interface AllUsersResponse {
  numPagina: number
  tamanioPagina: number
  elementosEncontrados: number
  paginasTotales: number
  contenido: Usuario[]
  role: String
}

export interface Usuario {
  id: string
  username: string
  correo: string
  password: string
  fotoPerfilUrl: string
  fechaRegistro: string
  role: String
}

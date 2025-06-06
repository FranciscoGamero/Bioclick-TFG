
export interface AllFoundResponse {
  numPagina: number
  tamanioPagina: number
  elementosEncontrados: number
  paginasTotales: number
  contenido: Contenido[]
  role: String
}

export interface Contenido {
  id: string
  username: string
  correo: string
  fotoPerfilUrl: string
  fechaRegistro: string
  role: String

}

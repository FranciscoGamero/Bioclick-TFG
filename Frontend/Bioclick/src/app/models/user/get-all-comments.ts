export interface AllCommentsResponse {
  numPagina: number
  tamanioPagina: number
  elementosEncontrados: number
  paginasTotales: number
  contenido: Comentario[]
}

export interface Comentario {
  id: string
  comentario: string
  usuarioId: string
  username: string
  fotoPerfil: string
  productoId: string
  fechaComentario: string

}

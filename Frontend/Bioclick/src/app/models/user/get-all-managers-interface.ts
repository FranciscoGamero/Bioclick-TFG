export interface AllManagersResponse {
  numPagina: number
  tamanioPagina: number
  elementosEncontrados: number
  paginasTotales: number
  contenido: Contenido[]
}

export interface Contenido {
  id: string;
  username: string;
  correo: string;
  password: string;
  fotoPerfilUrl: string;
  ultimaAccion: string;
  fechaUltimaAccion: string;
}
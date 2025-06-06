export interface userLoginResponse {
  id: string
  usuario: User
  token: string
  refreshToken: string
  enabled: boolean
  role: string
}

export interface User {
  username: string
  correo: string
  fotoPerfilUrl: string
  fechaRegistro: string
}

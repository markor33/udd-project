export interface LoginResponse {
  accessToken: string,
  expiresIn: number
}

export interface TokenContent {
  aud: string,
  exp: number,
  iat: number,
  role: string,
  sub: string
}

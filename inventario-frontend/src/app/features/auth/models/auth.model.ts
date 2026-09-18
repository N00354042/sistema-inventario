export interface AuthRequest {
  readonly username: string;
  readonly password: string;
}

export interface AuthResponse {
  readonly token: string;
}
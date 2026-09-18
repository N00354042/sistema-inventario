export interface Producto {
  readonly id?: number;
  readonly nombre: string;
  readonly descripcion: string;
  readonly precio: number;
  readonly stock: number;
  readonly stockMinimo: number;
}
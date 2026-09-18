export type TipoMovimiento = 'ENTRADA' | 'SALIDA';

export interface Movimiento {
  readonly id?: number;
  readonly producto: string;
  readonly tipo: TipoMovimiento;
  readonly cantidad: number;
  readonly fecha: string;
}
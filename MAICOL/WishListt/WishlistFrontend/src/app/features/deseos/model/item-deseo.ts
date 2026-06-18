export interface ItemDeseo {
  idListaDeseos: number;
  idProducto: number;
  nombreProducto: string;
  descripcion: string;
  precio: number;
  stockActual: number;
  cantidadDeseada: number;
  fechaAgregado: string;
  sinStock: boolean;
  mensajeNotificacion: string;
}

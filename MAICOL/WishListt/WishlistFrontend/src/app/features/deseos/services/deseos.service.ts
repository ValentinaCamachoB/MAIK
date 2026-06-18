import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DeseosService {

  private url = 'http://localhost:8080/lista-deseos';

  constructor(private http: HttpClient) { }

  // Lista los items de la lista de deseos del usuario
  listar(idUsuario: number): Observable<any> {
    return this.http.get<any>(this.url + '/' + idUsuario);
  }

  // Agregar un producto a la lista de deseos
  agregar(idUsuario: number, idProducto: number, cantidad: number): Observable<any> {
    const body = {
      idUsuario: idUsuario,
      idProducto: idProducto,
      cantidad: cantidad
    };
    return this.http.post<any>(this.url + '/agregar', body);
  }

  // Actualizar la cantidad de un item
  actualizar(idListaDeseos: number, cantidad: number): Observable<any> {
    const body = { cantidad: cantidad };
    return this.http.put<any>(this.url + '/actualizar/' + idListaDeseos, body);
  }

  // Eliminar un item
  eliminar(idListaDeseos: number): Observable<any> {
    return this.http.delete<any>(this.url + '/eliminar/' + idListaDeseos);
  }

  // Trae el historial completo
  getHistorico(idUsuario: number): Observable<any> {
    return this.http.get<any>(this.url + '/historico/' + idUsuario);
  }
}

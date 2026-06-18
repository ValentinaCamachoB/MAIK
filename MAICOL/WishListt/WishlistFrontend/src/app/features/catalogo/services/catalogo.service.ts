import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Producto } from '../model/producto';

@Injectable({
  providedIn: 'root'
})
export class CatalogoService {

  private url = 'http://localhost:8080/productos';

  constructor(private http: HttpClient) { }

  // Trae todos los productos del catalogo
  getCatalogo(): Observable<Producto[]> {
    return this.http.get<Producto[]>(this.url + '/catalogo');
  }
}

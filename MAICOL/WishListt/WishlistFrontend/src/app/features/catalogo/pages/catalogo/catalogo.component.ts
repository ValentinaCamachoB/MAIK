import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { DeseosService } from '../../../deseos/services/deseos.service';
import { Producto } from '../../model/producto';
import { CatalogoService } from '../../services/catalogo.service';

@Component({
  standalone: false,
  selector: 'app-catalogo',
  templateUrl: './catalogo.component.html',
  styleUrls: ['./catalogo.component.css']
})
export class CatalogoComponent implements OnInit {

  productos: Producto[] = [];
  mensaje = '';
  cargando = true;
  idUsuario = 1;

  constructor(
    private catalogoService: CatalogoService,
    private deseosService: DeseosService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit() {
    this.cargarCatalogo();
  }

  cargarCatalogo() {
    this.cargando = true;
    this.catalogoService.getCatalogo().subscribe({
      next: (data) => {
        this.productos = data;
        this.cargando = false;
        // Forzamos la deteccion de cambios para que la vista se refresque
        // al primer click. Sin esto a veces se requiere un segundo click.
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error al cargar el catalogo', err);
        this.mensaje = 'Ocurrio un error al cargar el catalogo';
        this.cargando = false;
        this.cdr.detectChanges();
      }
    });
  }

  agregar(producto: Producto) {
    this.deseosService.agregar(this.idUsuario, producto.idProducto, 1).subscribe({
      next: (resp) => {
        this.mensaje = resp.message;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error al agregar a la lista', err);
        this.mensaje = 'Ocurrio un error al agregar el producto';
        this.cdr.detectChanges();
      }
    });
  }
}
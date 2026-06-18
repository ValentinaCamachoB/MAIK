import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ItemDeseo } from '../../model/item-deseo';
import { DeseosService } from '../../services/deseos.service';

@Component({
  standalone: false,
  selector: 'app-lista-deseos',
  templateUrl: './lista-deseos.component.html',
  styleUrls: ['./lista-deseos.component.css']
})
export class ListaDeseosComponent implements OnInit {

  items: ItemDeseo[] = [];
  mensaje = '';
  cargando = true;
  idUsuario = 1;

  constructor(
    private deseosService: DeseosService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit() {
    this.cargar();
  }

  cargar() {
    this.cargando = true;
    this.deseosService.listar(this.idUsuario).subscribe({
      next: (resp) => {
        this.items = resp.data;
        this.cargando = false;
        // Forzamos la deteccion de cambios para que la vista se refresque
        // al primer click. Sin esto a veces se requiere un segundo click.
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error al cargar la lista de deseos', err);
        this.mensaje = 'Ocurrio un error al cargar la lista';
        this.cargando = false;
        this.cdr.detectChanges();
      }
    });
  }

  actualizar(item: ItemDeseo) {
    this.deseosService.actualizar(item.idListaDeseos, item.cantidadDeseada).subscribe({
      next: (resp) => {
        this.mensaje = resp.message;
        this.cargar();
      },
      error: (err) => {
        console.error('Error al actualizar el item', err);
        this.mensaje = 'Ocurrio un error al actualizar el item';
        this.cdr.detectChanges();
      }
    });
  }

  eliminar(item: ItemDeseo) {
    if (confirm('Seguro que deseas eliminar este item?')) {
      this.deseosService.eliminar(item.idListaDeseos).subscribe({
        next: (resp) => {
          this.mensaje = resp.message;
          this.cargar();
        },
        error: (err) => {
          console.error('Error al eliminar el item', err);
          this.mensaje = 'Ocurrio un error al eliminar el item';
          this.cdr.detectChanges();
        }
      });
    }
  }
}
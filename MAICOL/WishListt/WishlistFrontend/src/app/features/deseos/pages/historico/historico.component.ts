import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Historico } from '../../model/historico';
import { DeseosService } from '../../services/deseos.service';

@Component({
  standalone: false,
  selector: 'app-historico',
  templateUrl: './historico.component.html',
  styleUrls: ['./historico.component.css']
})
export class HistoricoComponent implements OnInit {

  historico: Historico[] = [];
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
    this.deseosService.getHistorico(this.idUsuario).subscribe({
      next: (resp) => {
        this.historico = resp.data;
        this.cargando = false;
        // Forzamos la deteccion de cambios para que la vista se refresque
        // al primer click. Sin esto a veces se requiere un segundo click.
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error al cargar el historico', err);
        this.mensaje = 'Ocurrio un error al cargar el historico';
        this.cargando = false;
        this.cdr.detectChanges();
      }
    });
  }
}
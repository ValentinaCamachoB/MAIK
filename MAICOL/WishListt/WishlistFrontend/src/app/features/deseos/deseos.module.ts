import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { HistoricoComponent } from './pages/historico/historico.component';
import { ListaDeseosComponent } from './pages/lista-deseos/lista-deseos.component';

@NgModule({
  declarations: [
    ListaDeseosComponent,
    HistoricoComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    RouterModule
  ],
  exports: [
    ListaDeseosComponent,
    HistoricoComponent
  ]
})
export class DeseosModule { }

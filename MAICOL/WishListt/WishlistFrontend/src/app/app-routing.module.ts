import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { CatalogoComponent } from './features/catalogo/pages/catalogo/catalogo.component';
import { HistoricoComponent } from './features/deseos/pages/historico/historico.component';
import { ListaDeseosComponent } from './features/deseos/pages/lista-deseos/lista-deseos.component';

const routes: Routes = [
  { path: '', redirectTo: 'catalogo', pathMatch: 'full' },
  { path: 'catalogo', component: CatalogoComponent },
  { path: 'deseos', component: ListaDeseosComponent },
  { path: 'historico', component: HistoricoComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

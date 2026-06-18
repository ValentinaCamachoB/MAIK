import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { CoreModule } from './core/core.module';
import { CatalogoModule } from './features/catalogo/catalogo.module';
import { DeseosModule } from './features/deseos/deseos.module';

import { AppComponent } from './app.component';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    CoreModule,
    CatalogoModule,
    DeseosModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

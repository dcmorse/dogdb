import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { DogdbSharedModule } from 'app/shared/shared.module';
import { DogdbCoreModule } from 'app/core/core.module';
import { DogdbAppRoutingModule } from './app-routing.module';
import { DogdbHomeModule } from './home/home.module';
import { DogdbEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    DogdbSharedModule,
    DogdbCoreModule,
    DogdbHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    DogdbEntityModule,
    DogdbAppRoutingModule
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent]
})
export class DogdbAppModule {}

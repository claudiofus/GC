import {BrowserModule} from '@angular/platform-browser';
import {LOCALE_ID, NgModule} from '@angular/core';
import {HttpClientModule} from '@angular/common/http';

import {AppComponent} from './app.component';
import {AppHeaderComponent} from '../common/components/header/app-header';
import {AppFooterComponent} from '../common/components/footer/app-footer';
import {TableComponent} from '../common/components/table-products/table-products.component';
import {ProductListComponent} from './product-list/product-list.component';
import {ProductListService} from './product-list/product-list.service';
import {AppRoutingModule} from './app.routing.module';
import {PageNotFoundComponent} from './not-found.component';
import {FilterPipe} from '../common/components/table-products/table-products.filter.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {AddInvoiceComponent} from './add-invoice/add-invoice.component';
import {DeadlinesComponent} from './deadlines/deadlines.component';
import {WorkersComponent} from './workers/workers.component';
import {SquadComponent} from './squad/squad.component';
import {VehiclesComponent} from './vehicles/vehicles.component';
import {ProfitsComponent} from './profits/profits.component';
import {SelectProviderComponent} from '../common/components/select-provider/select-provider.component';
import {AddInvoiceService} from './add-invoice/add-invoice.service';
import {BuildingsComponent} from './buildings/buildings.component';
import {TableProductsService} from '../common/components/table-products/table-products.service';
import {registerLocaleData} from '@angular/common';
import localeIt from '@angular/common/locales/it';
import {BuildingsService} from './buildings/buildings.service';
import {BuildingFormComponent} from '../common/components/building-form/building-form.component';
import {SharedModule} from '../common/components/generic-table/shared.module';
import {SelectBuildingComponent} from '../common/components/select-building/select-building.component';
import {SelectUmComponent} from '../common/components/select-um/select-um.component';
import {NguiAutoCompleteModule} from '@ngui/auto-complete';

registerLocaleData(localeIt, 'it');

@NgModule({
  imports: [
    HttpClientModule,
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    AppRoutingModule,
    SharedModule,
    NguiAutoCompleteModule
  ],
  declarations: [
    AppComponent,
    AppHeaderComponent,
    AppFooterComponent,
    TableComponent,
    FilterPipe,
    ProductListComponent,
    PageNotFoundComponent,
    AddInvoiceComponent,
    DeadlinesComponent,
    WorkersComponent,
    SquadComponent,
    VehiclesComponent,
    ProfitsComponent,
    SelectProviderComponent,
    SelectBuildingComponent,
    SelectUmComponent,
    BuildingsComponent,
    BuildingFormComponent,
  ],
  providers: [{
    provide: LOCALE_ID,
    useValue: 'it'
  }, ProductListService, AddInvoiceService, TableProductsService, BuildingsService],
  bootstrap: [AppComponent],
  exports: [
    FilterPipe
  ]
})

export class AppModule {
}

import 'flatpickr/dist/flatpickr.css';
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
import {CommonModule, registerLocaleData} from '@angular/common';
import localeIt from '@angular/common/locales/it';
import {BuildingsService} from './buildings/buildings.service';
import {BuildingFormComponent} from '../common/components/building-form/building-form.component';
import {SharedModule} from '../common/components/generic-table/shared.module';
import {SelectBuildingComponent} from '../common/components/select-building/select-building.component';
import {SelectUmComponent} from '../common/components/select-um/select-um.component';
import {NguiAutoCompleteModule} from '@ngui/auto-complete';
import {CalendarModule, DateAdapter} from 'angular-calendar';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {NgbModalModule, NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {FlatpickrModule} from 'angularx-flatpickr';
import {DeadlinesService} from './deadlines/deadlines.service';
import {VehiclesService} from './vehicles/vehicles.service';
import {VehicleFormComponent} from '../common/components/vehicle-form/vehicle-form.component';
import {PenaltyFormComponent} from '../common/components/penalty-form/penalty-form.component';
import {DeadlineFormComponent} from '../common/components/deadline-form/deadline-form.component';
import {adapterFactory} from 'angular-calendar/date-adapters/date-fns';
import {PagerService} from '../common/components/table-products/pager.service';
import {ConfirmDialogComponent} from '../common/components/confirm-dialog/confirm-dialog.component';
import {ConfirmDialogService} from '../common/components/confirm-dialog/confirm-dialog.service';
import {WorkersService} from './workers/workers.service';
import {WorkerFormComponent} from '../common/components/worker-form/worker-form.component';
import {UppercaseInputDirective} from '../common/components/worker-form/worker-form.directive';
import {SearchBarComponent} from '../common/components/search-bar/search-bar.component';

registerLocaleData(localeIt, 'it');

@NgModule({
  imports: [
    HttpClientModule,
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    AppRoutingModule,
    SharedModule,
    NguiAutoCompleteModule,
    NgbModule,
    BrowserAnimationsModule,
    CommonModule,
    NgbModalModule,
    FlatpickrModule.forRoot(),
    CalendarModule.forRoot({
      provide: DateAdapter,
      useFactory: adapterFactory
    }),
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
    VehicleFormComponent,
    PenaltyFormComponent,
    DeadlineFormComponent,
    WorkerFormComponent,
    ConfirmDialogComponent,
    UppercaseInputDirective,
    SearchBarComponent
  ],
  providers: [{
    provide: LOCALE_ID,
    useValue: 'it'
  },
    ProductListService,
    AddInvoiceService,
    PagerService,
    TableProductsService,
    BuildingsService,
    DeadlinesService,
    VehiclesService,
    WorkersService,
    ConfirmDialogService,
    FilterPipe
  ],
  bootstrap: [AppComponent],
  exports: [
    FilterPipe
  ]
})

export class AppModule {
}

import {async, TestBed} from '@angular/core/testing';
import {AppComponent} from './app.component';
import {AppHeaderComponent} from '../common/components/header/app-header';
import {RouterModule, Routes} from '@angular/router';
import {AppFooterComponent} from '../common/components/footer/app-footer';
import {AddInvoiceComponent} from './add-invoice/add-invoice.component';
import {WorkersComponent} from './workers/workers.component';
import {GetInvoiceComponent} from './get-invoice/get-invoice.component';
import {VehiclesComponent} from './vehicles/vehicles.component';
import {ProfitsComponent} from './profits/profits.component';
import {BuildingsComponent} from './buildings/buildings.component';
import {ProductListComponent} from './product-list/product-list.component';
import {PageNotFoundComponent} from './not-found.component';
import {DeadlinesComponent} from './deadlines/deadlines.component';
import {ConfirmDialogComponent} from '../common/components/confirm-dialog/confirm-dialog.component';
import {BuildingFormComponent} from '../common/components/forms/building-form/building-form.component';
import {GenericTableComponent} from '../common/components/generic-table/generic-table.component';
import {SearchBarComponent} from '../common/components/search-bar/search-bar.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {FilterPipe} from '../common/components/table-products/table-products.filter.component';
import {CalendarModule, DateAdapter} from 'angular-calendar';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import {CommonModule} from '@angular/common';
import {NgbModalModule} from '@ng-bootstrap/ng-bootstrap';
import {FlatpickrModule} from 'angularx-flatpickr';
import {adapterFactory} from 'angular-calendar/date-adapters/date-fns';
import {NguiAutoCompleteModule} from '@ngui/auto-complete';
import {SelectUmComponent} from '../common/components/select-um/select-um.component';
import {SelectProviderComponent} from '../common/components/select-provider/select-provider.component';
import {SelectBuildingComponent} from '../common/components/select-building/select-building.component';
import {TableComponent} from '../common/components/table-products/table-products.component';
import {WorkerFormComponent} from '../common/components/forms/worker-form/worker-form.component';
import {DeadlineFormComponent} from '../common/components/forms/deadline-form/deadline-form.component';
import {PenaltyFormComponent} from '../common/components/forms/penalty-form/penalty-form.component';
import {WorkersService} from './workers/workers.service';
import {GetInvoiceService} from './get-invoice/get-invoice.service';
import {AddInvoiceService} from './add-invoice/add-invoice.service';
import {PagerService} from '../common/components/table-products/pager.service';
import {DeadlinesService} from './deadlines/deadlines.service';
import {VehiclesService} from './vehicles/vehicles.service';
import {ProductListService} from './product-list/product-list.service';
import {BuildingsService} from './buildings/buildings.service';
import {TableProductsService} from '../common/components/table-products/table-products.service';
import {ConfirmDialogService} from '../common/components/confirm-dialog/confirm-dialog.service';
import {VehicleFormComponent} from '../common/components/forms/vehicle-form/vehicle-form.component';
import {StyleCellDirective} from '../common/components/generic-table/style-cell.directive';
import {FormatCellPipe} from '../common/components/generic-table/format-cell.pipe';
import {DashboardComponent} from './dashboard/dashboard.component';
import {DashboardService} from './dashboard/dashboard.service';
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {ChartsModule} from 'ng2-charts';

const appRoutes: Routes = [
    {path: 'cantieri', component: BuildingsComponent},
    {path: 'aggiungi-fattura', component: AddInvoiceComponent},
    {path: 'recupera-fattura', component: GetInvoiceComponent},
    {path: 'lista-prodotti', component: ProductListComponent},
    {path: 'scadenze', component: DeadlinesComponent},
    {path: 'operai', component: WorkersComponent},
    {path: 'mezzi-attrezzature', component: VehiclesComponent},
    {path: 'utili', component: ProfitsComponent},
    {path: '', redirectTo: '/cantieri', pathMatch: 'full'},
    {path: '**', component: PageNotFoundComponent}
];

describe('AppComponent', () => {
    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [
                AppComponent,
                AppHeaderComponent,
                AppFooterComponent,
                BuildingsComponent,
                DashboardComponent,
                AddInvoiceComponent,
                GetInvoiceComponent,
                ProductListComponent,
                DeadlinesComponent,
                WorkersComponent,
                VehiclesComponent,
                ProfitsComponent,
                PageNotFoundComponent,
                ConfirmDialogComponent,
                GenericTableComponent,
                SearchBarComponent,
                TableComponent,
                BuildingFormComponent,
                DeadlineFormComponent,
                PenaltyFormComponent,
                VehicleFormComponent,
                WorkerFormComponent,
                SelectUmComponent,
                SelectProviderComponent,
                SelectBuildingComponent,
                StyleCellDirective,
                FormatCellPipe,
                FilterPipe
            ],
            imports: [
                RouterModule.forRoot(appRoutes),
                NguiAutoCompleteModule,
                HttpClientTestingModule,
                CommonModule,
                FormsModule,
                ReactiveFormsModule,
                NgbModalModule,
                FontAwesomeModule,
                ChartsModule,
                FlatpickrModule.forRoot(),
                CalendarModule.forRoot({
                    provide: DateAdapter,
                    useFactory: adapterFactory
                }),
            ],
            providers: [
                ProductListService,
                AddInvoiceService,
                GetInvoiceService,
                PagerService,
                TableProductsService,
                BuildingsService,
                DeadlinesService,
                DashboardService,
                VehiclesService,
                WorkersService,
                ConfirmDialogService,
                FilterPipe
            ]
        }).compileComponents();
    }));
    it('should create the app', async(() => {
        const fixture = TestBed.createComponent(AppComponent);
        const app = fixture.debugElement.componentInstance;
        expect(app).toBeTruthy();
    }));
});

import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {BuildingsComponent} from './buildings.component';
import {GenericTableComponent} from '../../common/components/generic-table/generic-table.component';
import {ConfirmDialogComponent} from '../../common/components/confirm-dialog/confirm-dialog.component';
import {BuildingFormComponent} from '../../common/components/forms/building-form/building-form.component';
import {SearchBarComponent} from '../../common/components/search-bar/search-bar.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {FilterPipe} from '../../common/components/table-products/table-products.filter.component';
import {CalendarModule, DateAdapter} from 'angular-calendar';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {CommonModule} from '@angular/common';
import {NgbModalModule} from '@ng-bootstrap/ng-bootstrap';
import {FlatpickrModule} from 'angularx-flatpickr';
import {adapterFactory} from 'angular-calendar/date-adapters/date-fns';
import {StyleCellDirective} from '../../common/components/generic-table/style-cell.directive';
import {FormatCellPipe} from '../../common/components/generic-table/format-cell.pipe';
import {WorkersService} from '../workers/workers.service';
import {BuildingsService} from './buildings.service';
import {ConfirmDialogService} from '../../common/components/confirm-dialog/confirm-dialog.service';
import {Address} from '../../classes/address';
import {AddInvoiceService} from '../add-invoice/add-invoice.service';

describe('BuildingsComponent', () => {
    let component: BuildingsComponent;
    let fixture: ComponentFixture<BuildingsComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [
                BuildingsComponent,
                BuildingFormComponent,
                GenericTableComponent,
                ConfirmDialogComponent,
                SearchBarComponent,
                FilterPipe,
                StyleCellDirective,
                FormatCellPipe
            ],
            imports: [
                HttpClientTestingModule,
                CommonModule,
                FormsModule,
                ReactiveFormsModule,
                NgbModalModule,
                FlatpickrModule.forRoot(),
                CalendarModule.forRoot({
                    provide: DateAdapter,
                    useFactory: adapterFactory
                })
            ],
            providers: [
                WorkersService,
                BuildingsService,
                FilterPipe,
                ConfirmDialogService
            ]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(BuildingsComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });

    it('null address should be rejected', function () {
        const address = new Address();
        expect(component.isValidAddress(address)).toBeFalsy();
    });

    it('should close the panel', function () {
        component.addPanel = true;
        component.closePanel();
        expect(component.addPanel).toBeFalsy();
    });
});

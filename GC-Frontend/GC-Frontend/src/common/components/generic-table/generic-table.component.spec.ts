import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {GenericTableComponent} from './generic-table.component';
import {StyleCellDirective} from './style-cell.directive';
import {FormatCellPipe} from './format-cell.pipe';
import {CurrencyPipe, registerLocaleData} from '@angular/common';
import {WorkersService} from '../../../app/workers/workers.service';
import {GetInvoiceService} from '../../../app/get-invoice/get-invoice.service';
import {AddInvoiceService} from '../../../app/add-invoice/add-invoice.service';
import {PagerService} from '../table-products/pager.service';
import {DeadlinesService} from '../../../app/deadlines/deadlines.service';
import {VehiclesService} from '../../../app/vehicles/vehicles.service';
import {ProductListService} from '../../../app/product-list/product-list.service';
import {FilterPipe} from '../table-products/table-products.filter.component';
import {BuildingsService} from '../../../app/buildings/buildings.service';
import {TableProductsService} from '../table-products/table-products.service';
import {ConfirmDialogService} from '../confirm-dialog/confirm-dialog.service';
import {LOCALE_ID} from '@angular/core';
import localeIt from '@angular/common/locales/it';

describe('GenericTableComponent', () => {
    let component: GenericTableComponent;
    let fixture: ComponentFixture<GenericTableComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [GenericTableComponent, StyleCellDirective, FormatCellPipe],
            providers   : [
                ProductListService,
                AddInvoiceService,
                GetInvoiceService,
                PagerService,
                TableProductsService,
                BuildingsService,
                DeadlinesService,
                VehiclesService,
                WorkersService,
                ConfirmDialogService,
                FilterPipe,
                CurrencyPipe,
                FormatCellPipe,
                {
                    provide : LOCALE_ID,
                    useValue: 'it'
                },
            ]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(GenericTableComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
        registerLocaleData(localeIt);
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });

    it('should define FormatCellPipe', () => {
        const pipe = TestBed.get(FormatCellPipe);
        expect(pipe.transform(undefined, '')).toBe('not available');
        expect(pipe.transform([1, 2, 3], 'default')).toBe('1, 2, 3');
        expect(pipe.transform(12.44, 'currency')).toBe('12,44 €');
        expect(pipe.transform(1566391393208, 'date')).toBe('21/08/2019');
    });
});

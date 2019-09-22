import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {GetInvoiceComponent} from './get-invoice.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {GetInvoiceService} from './get-invoice.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import {BuildingsService} from '../buildings/buildings.service';
import {WorkersService} from '../workers/workers.service';
import {AddInvoiceService} from '../add-invoice/add-invoice.service';
import {PagerService} from '../../common/components/table-products/pager.service';
import {DeadlinesService} from '../deadlines/deadlines.service';
import {VehiclesService} from '../vehicles/vehicles.service';
import {ProductListService} from '../product-list/product-list.service';
import {FilterPipe} from '../../common/components/table-products/table-products.filter.component';
import {TableProductsService} from '../../common/components/table-products/table-products.service';
import {ConfirmDialogService} from '../../common/components/confirm-dialog/confirm-dialog.service';

describe('GetInvoiceComponent', () => {
    let component: GetInvoiceComponent;
    let fixture: ComponentFixture<GetInvoiceComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [GetInvoiceComponent],
            imports: [HttpClientTestingModule, FormsModule, ReactiveFormsModule],
            providers: [
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
                FilterPipe
            ]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(GetInvoiceComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});

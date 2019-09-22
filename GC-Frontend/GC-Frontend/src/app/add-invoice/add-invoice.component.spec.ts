import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {AddInvoiceComponent} from './add-invoice.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {AddInvoiceService} from './add-invoice.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import {BuildingsService} from '../buildings/buildings.service';

describe('AddInvoiceComponent', () => {
    let component: AddInvoiceComponent;
    let fixture: ComponentFixture<AddInvoiceComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [AddInvoiceComponent],
            imports: [HttpClientTestingModule, ReactiveFormsModule, FormsModule],
            providers: [AddInvoiceService, BuildingsService]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(AddInvoiceComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });

    it('orderColumns should be an Array', function () {
        expect(Array.isArray(AddInvoiceService.getOrderColumns())).toBeTruthy();
    });
});

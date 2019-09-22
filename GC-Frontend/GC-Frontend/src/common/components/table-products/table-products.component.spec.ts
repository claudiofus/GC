import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {TableComponent} from './table-products.component';
import {SearchBarComponent} from '../search-bar/search-bar.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {FilterPipe} from './table-products.filter.component';
import {PagerService} from './pager.service';
import {TableProductsService} from './table-products.service';
import {HttpClientTestingModule} from '@angular/common/http/testing';

describe('TableComponent', () => {
    let component: TableComponent;
    let fixture: ComponentFixture<TableComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [TableComponent, SearchBarComponent, FilterPipe],
            imports: [HttpClientTestingModule, FormsModule, ReactiveFormsModule],
            providers: [
                TableProductsService,
                PagerService,
                FilterPipe
            ]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(TableComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});

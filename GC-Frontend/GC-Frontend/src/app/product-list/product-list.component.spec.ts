import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ProductListComponent} from './product-list.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {SelectUmComponent} from '../../common/components/select-um/select-um.component';
import {SelectProviderComponent} from '../../common/components/select-provider/select-provider.component';
import {TableComponent} from '../../common/components/table-products/table-products.component';
import {SelectBuildingComponent} from '../../common/components/select-building/select-building.component';
import {NguiAutoCompleteModule} from '@ngui/auto-complete';
import {CalendarModule, DateAdapter} from 'angular-calendar';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import {CommonModule} from '@angular/common';
import {NgbModalModule} from '@ng-bootstrap/ng-bootstrap';
import {FlatpickrModule} from 'angularx-flatpickr';
import {adapterFactory} from 'angular-calendar/date-adapters/date-fns';
import {SearchBarComponent} from '../../common/components/search-bar/search-bar.component';
import {FilterPipe} from '../../common/components/table-products/table-products.filter.component';
import {ProductListService} from './product-list.service';

describe('ProductListComponent', () => {
    let component: ProductListComponent;
    let fixture: ComponentFixture<ProductListComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [
                ProductListComponent,
                SelectUmComponent,
                SelectProviderComponent,
                SelectBuildingComponent,
                TableComponent,
                SearchBarComponent,
                FilterPipe
            ],
            imports: [
                NguiAutoCompleteModule,
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
                ProductListService,
                FilterPipe
            ]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(ProductListComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});

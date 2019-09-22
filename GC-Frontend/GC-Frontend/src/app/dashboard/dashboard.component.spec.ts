import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {DashboardComponent} from './dashboard.component';
import {NguiAutoCompleteModule} from '@ngui/auto-complete';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {DashboardService} from './dashboard.service';
import {CommonModule, CurrencyPipe} from '@angular/common';
import {ChartsModule} from 'ng2-charts';
import {BuildingsService} from '../buildings/buildings.service';
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {NgbModalModule} from '@ng-bootstrap/ng-bootstrap';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {LOCALE_ID} from '@angular/core';
import {FilterPipe} from '../../common/components/table-products/table-products.filter.component';

describe('DashboardComponent', () => {
    let component: DashboardComponent;
    let fixture: ComponentFixture<DashboardComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [
                DashboardComponent
            ],
            imports     : [
                NguiAutoCompleteModule,
                HttpClientTestingModule,
                CommonModule,
                FormsModule,
                ReactiveFormsModule,
                NgbModalModule,
                ChartsModule,
                FontAwesomeModule
            ],
            providers   : [
                DashboardService,
                BuildingsService,
                FilterPipe,
                CurrencyPipe,
                {
                    provide : LOCALE_ID,
                    useValue: 'it'
                },
            ]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(DashboardComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});

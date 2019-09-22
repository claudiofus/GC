import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ProfitsComponent} from './profits.component';
import {NguiAutoCompleteModule} from '@ngui/auto-complete';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {CommonModule, CurrencyPipe} from '@angular/common';
import {NgbModalModule} from '@ng-bootstrap/ng-bootstrap';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {DashboardComponent} from '../dashboard/dashboard.component';
import {DashboardService} from '../dashboard/dashboard.service';
import {ChartsModule} from 'ng2-charts';
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {BuildingsService} from '../buildings/buildings.service';
import {LOCALE_ID} from '@angular/core';
import {FilterPipe} from '../../common/components/table-products/table-products.filter.component';

describe('ProfitsComponent', () => {
    let component: ProfitsComponent;
    let fixture: ComponentFixture<ProfitsComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [
                ProfitsComponent, DashboardComponent
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
        fixture = TestBed.createComponent(ProfitsComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});

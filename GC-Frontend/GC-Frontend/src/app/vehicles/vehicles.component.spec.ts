import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {VehiclesComponent} from './vehicles.component';
import {GenericTableComponent} from '../../common/components/generic-table/generic-table.component';
import {VehicleFormComponent} from '../../common/components/forms/vehicle-form/vehicle-form.component';
import {PenaltyFormComponent} from '../../common/components/forms/penalty-form/penalty-form.component';
import {DeadlineFormComponent} from '../../common/components/forms/deadline-form/deadline-form.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {CalendarModule, DateAdapter} from 'angular-calendar';
import {CommonModule} from '@angular/common';
import {NgbModalModule} from '@ng-bootstrap/ng-bootstrap';
import {FlatpickrModule} from 'angularx-flatpickr';
import {adapterFactory} from 'angular-calendar/date-adapters/date-fns';
import {FormatCellPipe} from '../../common/components/generic-table/format-cell.pipe';
import {StyleCellDirective} from '../../common/components/generic-table/style-cell.directive';
import {VehiclesService} from './vehicles.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('VehiclesComponent', () => {
    let component: VehiclesComponent;
    let fixture: ComponentFixture<VehiclesComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [
                VehiclesComponent,
                VehicleFormComponent,
                PenaltyFormComponent,
                DeadlineFormComponent,
                GenericTableComponent,
                FormatCellPipe,
                StyleCellDirective
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
            providers: [VehiclesService]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(VehiclesComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});

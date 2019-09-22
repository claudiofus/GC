import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {WorkersComponent} from './workers.component';
import {WorkerFormComponent} from '../../common/components/forms/worker-form/worker-form.component';
import {GenericTableComponent} from '../../common/components/generic-table/generic-table.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {CalendarModule, DateAdapter} from 'angular-calendar';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import {CommonModule} from '@angular/common';
import {NgbModalModule} from '@ng-bootstrap/ng-bootstrap';
import {FlatpickrModule} from 'angularx-flatpickr';
import {adapterFactory} from 'angular-calendar/date-adapters/date-fns';
import {FormatCellPipe} from '../../common/components/generic-table/format-cell.pipe';
import {StyleCellDirective} from '../../common/components/generic-table/style-cell.directive';
import {WorkersService} from './workers.service';

describe('WorkersComponent', () => {
    let component: WorkersComponent;
    let fixture: ComponentFixture<WorkersComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [
                WorkersComponent,
                WorkerFormComponent,
                GenericTableComponent,
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
            providers: [WorkersService]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(WorkersComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});

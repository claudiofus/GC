import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {DeadlinesComponent} from './deadlines.component';
import {CalendarModule, DateAdapter} from 'angular-calendar';
import {FlatpickrModule} from 'angularx-flatpickr';
import {NgbModalModule} from '@ng-bootstrap/ng-bootstrap';
import {FormsModule} from '@angular/forms';
import {CommonModule} from '@angular/common';
import {adapterFactory} from 'angular-calendar/date-adapters/date-fns';
import {DeadlinesService} from './deadlines.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('DeadlinesComponent', () => {
    let component: DeadlinesComponent;
    let fixture: ComponentFixture<DeadlinesComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [DeadlinesComponent],
            imports: [
                HttpClientTestingModule,
                CommonModule,
                FormsModule,
                NgbModalModule,
                FlatpickrModule.forRoot(),
                CalendarModule.forRoot({
                    provide: DateAdapter,
                    useFactory: adapterFactory
                })
            ],
            providers: [DeadlinesService]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(DeadlinesComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});

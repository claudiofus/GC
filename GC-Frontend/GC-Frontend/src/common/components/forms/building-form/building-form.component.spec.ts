import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {BuildingFormComponent} from './building-form.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {CalendarModule, DateAdapter} from 'angular-calendar';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import {CommonModule} from '@angular/common';
import {NgbModalModule} from '@ng-bootstrap/ng-bootstrap';
import {FlatpickrModule} from 'angularx-flatpickr';
import {adapterFactory} from 'angular-calendar/date-adapters/date-fns';

describe('BuildingFormComponent', () => {
    let component: BuildingFormComponent;
    let fixture: ComponentFixture<BuildingFormComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [BuildingFormComponent],
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
                })]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(BuildingFormComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });

    it('should emit on click', () => {
        spyOn(component.submit, 'emit');
        component.sendBuilding();
        expect(component.submit.emit).toHaveBeenCalled();
    });
});

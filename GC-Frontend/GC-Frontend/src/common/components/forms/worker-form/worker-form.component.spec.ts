import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {WorkerFormComponent} from './worker-form.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {CalendarModule, DateAdapter} from 'angular-calendar';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {CommonModule} from '@angular/common';
import {NgbModalModule} from '@ng-bootstrap/ng-bootstrap';
import {FlatpickrModule} from 'angularx-flatpickr';
import {adapterFactory} from 'angular-calendar/date-adapters/date-fns';

describe('WorkerFormComponent', () => {
    let component: WorkerFormComponent;
    let fixture: ComponentFixture<WorkerFormComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [WorkerFormComponent],
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
            ]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(WorkerFormComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });

    it('should validate gender', () => {
        expect(WorkerFormComponent.findGender('RSSMRA80A01A662A')).toBe('M');
        expect(WorkerFormComponent.findGender('RSSMRA80A41A662E')).toBe('F');
    });

    it('should extract consonants', () => {
        expect(WorkerFormComponent.estraiConsonanti(undefined)).toBe('');
        expect(WorkerFormComponent.estraiConsonanti('RSSMRA80A01A662A')).toBe('RSSMR');
    });

    it('should extract vowels', () => {
        expect(WorkerFormComponent.estraiVocali(undefined)).toBe('');
        expect(WorkerFormComponent.estraiVocali('RSSMRA80A01A662A')).toBe('AAAA');
        expect(WorkerFormComponent.estraiVocali('RSSMRA80A41A662E')).toBe('AAAE');
    });

    it('should extract surname code', () => {
        expect(component.surnameCode('ROSSI')).toBe('RSS');
    });

    it('should extract name code', () => {
        expect(component.nameCode('MARIO')).toBe('MRA');
        expect(component.nameCode('MARIA')).toBe('MRA');
    });

    it('should find birth bate', () => {
        const birthDate = component.findBirthDate('RSSMRA80A01A662A');
        expect(typeof new Date === typeof birthDate).toBeTruthy();
        birthDate.setHours(0, 0, 0);
        expect(birthDate.getTime()).toEqual(new Date(1980, 0, 1).getTime());
    });

    it('should emit on click', () => {
        spyOn(component.submit, 'emit');
        component.sendWorker();
        expect(component.submit.emit).toHaveBeenCalled();
    });
});

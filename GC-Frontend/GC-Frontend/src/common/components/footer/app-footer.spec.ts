import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {CommonModule} from '@angular/common';
import {AppFooterComponent} from './app-footer';

describe('AppFooterComponent', () => {
    let component: AppFooterComponent;
    let fixture: ComponentFixture<AppFooterComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [AppFooterComponent],
            imports: [
                HttpClientTestingModule,
                CommonModule,
                FormsModule,
                ReactiveFormsModule
                ]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(AppFooterComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});

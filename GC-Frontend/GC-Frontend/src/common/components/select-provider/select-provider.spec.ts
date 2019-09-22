import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {CommonModule} from '@angular/common';
import {SelectProviderComponent} from './select-provider.component';

describe('SelectProviderComponent', () => {
    let component: SelectProviderComponent;
    let fixture: ComponentFixture<SelectProviderComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [SelectProviderComponent],
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
        fixture = TestBed.createComponent(SelectProviderComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});

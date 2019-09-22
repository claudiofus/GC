import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {CommonModule} from '@angular/common';
import {SelectUmComponent} from './select-um.component';

describe('SelectUmComponent', () => {
    let component: SelectUmComponent;
    let fixture: ComponentFixture<SelectUmComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [SelectUmComponent],
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
        fixture = TestBed.createComponent(SelectUmComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});

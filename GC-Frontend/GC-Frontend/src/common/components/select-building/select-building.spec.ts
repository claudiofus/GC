import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {CommonModule} from '@angular/common';
import {SelectBuildingComponent} from './select-building.component';
import {BuildingListService} from './select-building.service';

describe('SelectBuildingComponent', () => {
    let component: SelectBuildingComponent;
    let fixture: ComponentFixture<SelectBuildingComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [SelectBuildingComponent],
            imports: [
                HttpClientTestingModule,
                CommonModule,
                FormsModule,
                ReactiveFormsModule
            ],
            providers: [BuildingListService]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(SelectBuildingComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});

import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {SearchBarComponent} from './search-bar.component';
import {FormsModule} from '@angular/forms';

describe('SearchBarComponent', () => {
    let component: SearchBarComponent;
    let fixture: ComponentFixture<SearchBarComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [SearchBarComponent],
            imports: [FormsModule]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(SearchBarComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});

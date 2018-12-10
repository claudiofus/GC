import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PenaltyFormComponent } from './penalty-form.component';

describe('PenaltyFormComponent', () => {
  let component: PenaltyFormComponent;
  let fixture: ComponentFixture<PenaltyFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PenaltyFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PenaltyFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

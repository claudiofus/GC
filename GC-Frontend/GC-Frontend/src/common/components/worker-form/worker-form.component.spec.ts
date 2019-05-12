import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {WorkerFormComponent} from './worker-form.component';

describe('WorkerFormComponent', () => {
  let component: WorkerFormComponent;
  let fixture: ComponentFixture<WorkerFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [WorkerFormComponent]
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
});

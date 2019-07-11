import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GetInvoiceComponent } from './get-invoice.component';

describe('GetInvoiceComponent', () => {
  let component: GetInvoiceComponent;
  let fixture: ComponentFixture<GetInvoiceComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GetInvoiceComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GetInvoiceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

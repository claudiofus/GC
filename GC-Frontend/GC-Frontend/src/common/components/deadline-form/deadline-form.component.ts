import {Component, EventEmitter, Input, OnChanges, Output} from '@angular/core';
import {Italian} from 'flatpickr/dist/l10n/it';
import {Utils} from '../../../classes/utils';

@Component({
  selector: 'app-deadline-form',
  templateUrl: './deadline-form.component.html'
})
export class DeadlineFormComponent implements OnChanges {
  @Input() vehicles: any[];
  @Input() selVehicle: any;
  @Input() type: string;
  @Output() submit = new EventEmitter<any>();
  deadlineDate: any;
  amount: string;
  paid: boolean;
  locale = Italian;
  utils = Utils;

  ngOnChanges() {
    if (this.selVehicle && this.type) {
      if (this.type === 'insurance' && this.selVehicle.insurance) {
        this.deadlineDate = this.selVehicle.insurance.deadlineDate;
        this.amount = this.selVehicle.insurance.amount;
        this.paid = this.selVehicle.insurance.paid;
      } else if (this.type === 'revision' && this.selVehicle.revision) {
        this.deadlineDate = this.selVehicle.revision.deadlineDate;
        this.amount = this.selVehicle.revision.amount;
        this.paid = this.selVehicle.revision.paid;
      } else if (this.type === 'car_tax' && this.selVehicle.car_tax) {
        this.deadlineDate = this.selVehicle.car_tax.deadlineDate;
        this.amount = this.selVehicle.car_tax.amount;
        this.paid = this.selVehicle.car_tax.paid;
      }
    }
  }

  sendSelVehicle() {
    switch (this.type) {
      case 'insurance':
        this.selVehicle.insurance.deadlineDate = this.deadlineDate;
        this.selVehicle.insurance.amount = this.amount;
        this.selVehicle.insurance.paid = this.paid;
        break;
      case 'revision':
        this.selVehicle.revision.deadlineDate = this.deadlineDate;
        this.selVehicle.revision.amount = this.amount;
        this.selVehicle.revision.paid = this.paid;
        break;
      case 'car_tax':
        this.selVehicle.car_tax.deadlineDate = this.deadlineDate;
        this.selVehicle.car_tax.amount = this.amount;
        this.selVehicle.car_tax.paid = this.paid;
        break;
    }
    this.submit.emit(this.selVehicle);
  }
}

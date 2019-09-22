import {Component, EventEmitter, Output} from '@angular/core';
import {Italian} from 'flatpickr/dist/l10n/it';
import {Utils} from '../../../../classes/utils';
import {Building} from '../../../../classes/building';

@Component({
  selector: 'app-vehicle-form',
  templateUrl: './vehicle-form.component.html',
  styleUrls: ['./vehicle-form.component.css']
})

export class VehicleFormComponent {
  @Output() submit = new EventEmitter<Building>();
  newVehicle: any;
  locale = Italian;
  utils = Utils;

  constructor() {
    this.newVehicle = {};
    this.newVehicle.insurance = {};
    this.newVehicle.car_tax = {};
    this.newVehicle.revision = {};
  }

  sendVehicle() {
    this.submit.emit(this.newVehicle);
  }
}

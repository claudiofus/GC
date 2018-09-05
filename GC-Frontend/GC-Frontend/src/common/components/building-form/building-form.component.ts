import {Component, EventEmitter, Output} from '@angular/core';
import {Building} from '../../../classes/building';
import {Address} from '../../../classes/address';
import {Utils} from '../../../classes/utils';
import {Italian} from 'flatpickr/dist/l10n/it';

@Component({
  selector: 'app-building-form',
  templateUrl: './building-form.component.html'
})
export class BuildingFormComponent {
  @Output() submit = new EventEmitter<Building>();
  public model: Building;
  public utils = Utils;
  locale = Italian;

  constructor() {
    this.model = new Building();
    this.model.address = new Address();
  }

  sendBuilding() {
    this.submit.emit(this.model);
  }
}

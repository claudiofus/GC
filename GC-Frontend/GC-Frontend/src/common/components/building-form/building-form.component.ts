import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Building} from '../../../classes/building';
import {Utils} from '../../../classes/utils';
import {Italian} from 'flatpickr/dist/l10n/it';

@Component({
  selector: 'app-building-form',
  templateUrl: './building-form.component.html'
})
export class BuildingFormComponent {
  @Output() submit = new EventEmitter<Building>();
  @Input() model: Building;
  public utils = Utils;
  locale = Italian;

  sendBuilding() {
    this.submit.emit(this.model);
  }
}

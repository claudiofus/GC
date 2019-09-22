import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Italian} from 'flatpickr/dist/l10n/it';
import {Utils} from '../../../../classes/utils';
import {Building} from '../../../../classes/building';

@Component({
  selector: 'app-penalty-form',
  templateUrl: './penalty-form.component.html'
})
export class PenaltyFormComponent implements OnInit {
  @Input() vehicles: any[];
  @Output() submit = new EventEmitter<Building>();
  vehicle: any;
  locale = Italian;
  utils = Utils;

  constructor() {
    this.vehicle = {};
    this.vehicle.penalty = [{}];
  }

  ngOnInit() {
  }

  sendPenalty() {
    this.submit.emit(this.vehicle);
  }
}

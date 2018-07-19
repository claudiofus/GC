import {Component, Input, OnInit} from '@angular/core';
import {BuildingListService} from './select-building.service';
import {FormGroup} from '@angular/forms';

@Component({
  selector: 'app-select-building',
  templateUrl: './select-building.component.html',
  providers: [BuildingListService]
})

export class SelectBuildingComponent implements OnInit {
  buildings: string[];
  @Input() group: FormGroup;

  constructor(private buildingService: BuildingListService) {
  }

  ngOnInit() {
    this.buildings = [];
    this.buildingService.getAll().subscribe(
      restItems => {
        restItems.map(building => this.buildings.push(building));
      }
    );
  }
}

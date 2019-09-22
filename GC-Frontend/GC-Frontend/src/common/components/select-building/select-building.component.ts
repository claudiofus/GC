import {Component, Input, OnInit} from '@angular/core';
import {BuildingListService} from './select-building.service';
import {FormControl, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-select-building',
  templateUrl: './select-building.component.html',
  providers: [BuildingListService]
})

export class SelectBuildingComponent implements OnInit {
  buildings: string[];
  @Input() group = new FormGroup({
      buildingId: new FormControl(null, [Validators.required])
  });

  constructor(private buildingService: BuildingListService) {
  }

  ngOnInit() {
    this.buildings = [];
    this.buildingService.getAll().subscribe(
      restItems => {
        restItems.forEach(building => this.buildings.push(building));
      }
    );
  }
}

import {Component, OnInit} from '@angular/core';
import {BuildingsService} from './buildings.service';
import {ColumnSetting} from '../../common/components/generic-table/layout.model';
import {Address} from '../../classes/address';

@Component({
  selector: 'app-buildings',
  templateUrl: './buildings.component.html',
  styleUrls: ['./buildings.component.css']
})
export class BuildingsComponent implements OnInit {
  buildings: any[];
  orderSum: number;
  addPanel: boolean;
  projects: any[];
  projectSettings: ColumnSetting[];

  constructor(public buildingsService: BuildingsService) {
  }

  ngOnInit() {
    this.projectSettings = [{primaryKey: 'name'}, {primaryKey: 'value', format: 'currency'}];
    this.getRestItems();
    this.addPanel = false;
  }

  // Read all REST Items
  getRestItems(): void {
    this.buildings = [];
    this.buildingsService.getAll().subscribe(
      restItems => {
        restItems.map(building => {
          this.buildings.push(building);
          const completeAddr = building.address.addressType + building.address.addressName +
            building.address.addressNumber + building.address.city + building.address.province;
          this.buildingsService.getDirections(completeAddr).then(items => {
            if (items.routes.length > 0 && items.routes['0'].legs['0'].distance.text && items.routes['0'].legs['0'].duration.text) {
              building.address.distance = items.routes['0'].legs['0'].distance.text;
              building.address.timeToReach = items.routes['0'].legs['0'].duration.text;
            }
          });
        });
      }
    );
  }

  public getImage(id): string {
    return id % 10 + '.jpg';
  }

  addBuilding(building) {
    console.log(building);
    this.buildingsService.addBuilding(building)
      .then(result => {
        console.log(result);
        this.addPanel = false;
        this.getRestItems();
      })
      .catch(err => {
        console.error(err);
      });
  }

  getBuildingDet(building) {
    for (const el of this.buildings) {
      el.details = false;
      if (building.name === el.name) {
        this.orderSum = 0;
        this.buildingsService.getBuildingDet(building.name).subscribe(
          items => {
            items.map(order => this.orderSum += order.adj_price);
            this.projects = [{name: 'Importo complessivo lavori', value: building.req_amount},
              {name: 'Importo materiali', value: -this.orderSum},
              {name: 'Utili', value: building.req_amount - this.orderSum}];
          }
        );
      }
    }
    building.details = !building.details;
  }

  isValidAddress(addr: Address): boolean {
    return (addr.addressType != null && addr.addressName != null && addr.city != null);
  }
}

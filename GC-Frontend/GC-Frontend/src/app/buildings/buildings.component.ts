import {Component, OnInit} from '@angular/core';
import {BuildingsService} from './buildings.service';
import {ColumnSetting} from '../../common/components/generic-table/layout.model';
import {Address} from '../../classes/address';
import {AddInvoiceService} from '../add-invoice/add-invoice.service';
import {ConfirmDialogService} from '../../common/components/confirm-dialog/confirm-dialog.service';

@Component({
  selector: 'app-buildings',
  templateUrl: './buildings.component.html',
  styleUrls: ['./buildings.component.css']
})
export class BuildingsComponent implements OnInit {
  buildings: any[];
  buildingOrders: any[];
  buildingSel: string;
  orderColumns: string[];
  orderSum: number;
  orderIvaSum: number;
  addPanel: boolean;
  projects: any[];
  projectSettings: ColumnSetting[];
  showOpened = false;
  showClosed = false;
  showBuildingOrd = false;
  header = ['Descrizione', 'Importo'];

  constructor(public buildingsService: BuildingsService, private confirmDialogService: ConfirmDialogService) {
  }

  ngOnInit() {
    this.projectSettings = [{primaryKey: 'name'}, {primaryKey: 'value', format: 'currency'}];
    this.getRestItems();
    this.addPanel = false;
    this.showBuildingOrd = false;
    this.buildingSel = undefined;
  }

  // Read all REST Items
  getRestItems(): void {
    this.buildings = [];
    this.buildingsService.getAll().subscribe(
      restItems => {
        restItems.map(building => {
          this.buildings.push(building);
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
    this.buildingOrders = [];
    this.buildingSel = building.name;
    for (const el of this.buildings) {
      el.details = false;
      if (building.name === el.name) {
        this.orderSum = 0;
        this.orderIvaSum = 0;
        this.buildingsService.getBuildingDet(building.name).subscribe(
          items => {
            items.map(order => {
              this.orderSum += order.noIvaPrice;
              this.orderIvaSum += order.ivaPrice;
            });
            this.buildingOrders = items;
            this.projects = [{name: 'Importo complessivo lavori', value: building.req_amount},
              {name: 'Importo materiali', value: -this.orderIvaSum},
              {name: 'Utili', value: building.req_amount - this.orderIvaSum}];
          }
        );
      }
    }
    building.details = !building.details;
  }

  isValidAddress(addr: Address): boolean {
    return (addr.addressType != null && addr.addressName != null && addr.city != null);
  }

  getDetailsOfSel() {
    this.orderColumns = AddInvoiceService.getOrderColumns();
    this.buildings.splice(0, this.buildings.length);
    this.showBuildingOrd = true;
  }

  resetView() {
    this.buildingOrders.splice(0, this.buildingOrders.length);
    this.ngOnInit();
  }

  updateOrder(order) {
    const self = this;
    this.confirmDialogService.confirmThis('Vuoi proseguire con la cancellazione?',
      function () {
        order.building_id = undefined;
        self.buildingsService.updateOrder(order)
          .then(_ => {
            self.buildingOrders = self.buildingOrders.filter(function (obj) {
              return obj.id !== order.id;
            });
            self.orderSum = 0;
            self.orderIvaSum = 0;
            self.buildingsService.getBuildingDet(self.buildingSel).subscribe(
              items => {
                items.map(el => {
                  self.orderSum += el.noIvaPrice;
                  self.orderIvaSum += el.ivaPrice;
                });
                self.buildingOrders = items;
              }
            );
            alert(`L'ordine con descrizione ${order.name} e' stato rimosso dal cantiere ${self.buildingSel}.`);
          })
          .catch(err => {
            console.error(err);
            alert(`Si e' verificato un errore imprevisto: ${err}.`);
          });
      },
      function () {
      });
  }
}

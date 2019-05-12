import {Component, OnInit} from '@angular/core';
import {BuildingsService} from './buildings.service';
import {ColumnSetting} from '../../common/components/generic-table/layout.model';
import {Address} from '../../classes/address';
import {AddInvoiceService} from '../add-invoice/add-invoice.service';
import {ConfirmDialogService} from '../../common/components/confirm-dialog/confirm-dialog.service';
import {WorkersService} from '../workers/workers.service';
import {Italian} from 'flatpickr/dist/l10n/it';
import {Utils} from '../../classes/utils';

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
  workers: any[];
  jobs: any[];
  addPanel: boolean;
  projects: any[];
  projectSettings: ColumnSetting[];
  showBuildingOrd = false;
  showBuildingWorkers = false;
  workedHours: number;
  workedHoursCost: number;
  header = ['Descrizione', 'Importo'];
  locale = Italian;

  constructor(public workersService: WorkersService,
              public buildingsService: BuildingsService,
              private confirmDialogService: ConfirmDialogService) {
  }

  ngOnInit() {
    this.projectSettings = [{primaryKey: 'name'}, {primaryKey: 'value', format: 'currency'}];
    this.getRestItems();
    this.addPanel = false;
    this.showBuildingOrd = false;
    this.showBuildingWorkers = false;
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
    const self = this;
    self.buildingOrders = [];
    self.buildingSel = building.name;
    self.buildingsService.getJobs(this.buildingSel).subscribe(
      jobs => {
        self.workedHours = 0;
        self.workedHoursCost = 0;
        if (jobs.length === 0) {
          self.goOn(self, building);
        } else {
          const rowLen = jobs.length;
          jobs.map((job, i) => {
            self.workedHours += job.hoursOfWork;
            self.buildingsService.calcCost(job)
              .then(result => {
                self.workedHoursCost += result;
                if (rowLen === i + 1) {
                  self.goOn(self, building);
                }
              })
              .catch(error => {
                console.error(error);
              });
          });
        }
      }
    );
  }

  assignWorker(building) {
    this.jobs = [];
    this.workers = [];
    this.buildingSel = building.name;
    this.buildings.splice(0, this.buildings.length);
    this.showBuildingWorkers = true;
    this.buildingsService.getJobs(this.buildingSel).subscribe(
      restItems => {
        restItems.map(job => {
          this.jobs.push(job);
        });
        this.jobs.push({});
      }
    );
    this.workersService.getAll().subscribe(
      restItems => {
        restItems.map(worker => {
          this.workers.push(worker);
        });
      }
    );
  }

  updateWorker(buildingName, job) {
    if (this.isEmpty(job)) {
      return;
    }
    const self = this;
    this.confirmDialogService.confirmThis('Vuoi confermare l\'operazione?',
      function () {
        self.buildingsService.assignWorker(buildingName, job)
          .then(result => {
            console.log(result);
            self.buildingsService.getJobs(self.buildingSel).subscribe(
              restItems => {
                self.jobs = [];
                restItems.map(el => {
                  self.jobs.push(el);
                });
                self.jobs.push({hoursOfWork: 8});
              }
            );
          })
          .catch(error => {
            console.error(error);
          });
      },
      function () {
      });
  }

  deleteRow(job) {
    if (this.isEmpty(job)) {
      return;
    }
    const self = this;
    this.confirmDialogService.confirmThis('Vuoi proseguire con la cancellazione?',
      function () {
        self.buildingsService.deleteJob(job.id)
          .then(result => {
            console.log(result);
            self.buildingsService.getJobs(self.buildingSel).subscribe(
              restItems => {
                self.jobs = [];
                restItems.map(el => {
                  self.jobs.push(el);
                });
                self.jobs.push({hoursOfWork: 8});
              }
            );
          })
          .catch(error => {
            console.error(error);
          });
      },
      function () {
      }
    );
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
    if (this.buildingOrders) {
      this.buildingOrders.splice(0, this.buildingOrders.length);
    }
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

  isEmpty(obj) {
    return Object.keys(obj).length === 0 && obj.constructor === Object;
  }

  goOn(self, building) {
    for (const el of self.buildings) {
      el.details = false;
      if (building.name === el.name) {
        self.orderSum = 0;
        self.orderIvaSum = 0;
        self.buildingsService.getBuildingDet(self.buildingSel).subscribe(
          items => {
            items.map(order => {
              self.orderSum += order.noIvaPrice;
              self.orderIvaSum += order.ivaPrice;
            });
            const req_amount_round = Utils.round10(building.req_amount, -2);
            const orderIvaSum_rounded = Utils.round10(self.orderIvaSum, -2);
            const workedHoursCost_rounded = Utils.round10(self.workedHoursCost, -2);
            const diff = req_amount_round - orderIvaSum_rounded - workedHoursCost_rounded;
            self.buildingOrders = items;
            self.projects = [{name: 'Importo complessivo lavori', value: building.req_amount},
              {name: 'Importo materiali', value: -self.orderIvaSum},
              {name: 'Costo operai', value: -self.workedHoursCost},
              {name: 'Utili', value: diff}];
          }
        );
        building.details = !building.details;
      }
    }
  }
}

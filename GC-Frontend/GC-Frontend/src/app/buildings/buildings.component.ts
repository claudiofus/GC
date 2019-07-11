import {Component, OnInit} from '@angular/core';
import {BuildingsService} from './buildings.service';
import {ColumnSetting} from '../../common/components/generic-table/layout.model';
import {Address} from '../../classes/address';
import {AddInvoiceService} from '../add-invoice/add-invoice.service';
import {ConfirmDialogService} from '../../common/components/confirm-dialog/confirm-dialog.service';
import {WorkersService} from '../workers/workers.service';
import {Italian} from 'flatpickr/dist/l10n/it';
import {Utils} from '../../classes/utils';
import {Building} from '../../classes/building';
import {FilterPipe} from '../../common/components/table-products/table-products.filter.component';

@Component({
  selector: 'app-buildings',
  templateUrl: './buildings.component.html',
  styleUrls: ['./buildings.component.css']
})
export class BuildingsComponent implements OnInit {
  buildings: any[];
  ddts: Map<string, any[]>;
  ddtSel: any = undefined;
  buildingSel: string;
  buildingToUpd: any;
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
  section: any = [];
  searchString: string;

  constructor(public workersService: WorkersService,
              public buildingsService: BuildingsService,
              public filterPipe: FilterPipe,
              private confirmDialogService: ConfirmDialogService) {
  }

  ngOnInit() {
    this.projectSettings = [{primaryKey: 'name'}, {primaryKey: 'value', format: 'currency'}];
    this.getRestItems();
    this.addPanel = false;
    this.showBuildingOrd = false;
    this.showBuildingWorkers = false;
    this.buildingSel = undefined;
    this.buildingToUpd = new Building();
    this.buildingToUpd.address = new Address();
    this.ddts = new Map<string, any[]>();
    this.ddtSel = undefined;
  }

  // Read all REST Items
  getRestItems(): void {
    this.buildings = [];
    this.buildingsService.getAll().subscribe(
      restItems => {
          restItems.forEach(building => {
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

    if (this.buildings && this.buildings.length > 0) {
      const updateCheck = this.buildings.filter(function (b) {
        if (b.name.toLowerCase() === building.name.toLowerCase()) {
          return b;
        }
      });

      if (updateCheck.length > 0) {
        const self = this;
        this.confirmDialogService.confirmThis('Vuoi aggiornare il cantiere già esistente?',
          function () {
            self.updateBuilding(building);
          },
          function () {
          });
      } else {
          this.updateBuilding(building);
      }
    } else {
      this.updateBuilding(building);
    }
  }

  updateBuilding(building) {
    if (building.details) {
      delete building.details;
    }

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

  getBuildings(section) {
    if (!section.open && !section.close || section.open && section.close) {
      return this.buildings;
    } else if (section.open === true) {
      return this.buildings.filter(el => el.open === true);
    } else if (section.close === true) {
      return this.buildings.filter(el => el.open === false);
    }
  }

  edit(building) {
    this.addPanel = true;
    this.buildings = [];
    this.buildingToUpd = building;
  }

  closePanel() {
    if (this.addPanel) {
      this.getRestItems();
    } else {
      this.buildingToUpd = new Building();
      this.buildingToUpd.address = new Address();
    }
    this.addPanel = !this.addPanel;
  }

  getBuildingDet(building) {
    const self = this;
    self.ddts = new Map<string, any[]>();
    self.buildingSel = building.name;
    self.buildingsService.getJobs(this.buildingSel).subscribe(
      jobs => {
        self.workedHours = 0;
        self.workedHoursCost = 0;
        if (jobs.length === 0) {
          self.goOn(self, building);
        } else {
          const rowLen = jobs.length;
          jobs.forEach((job, i) => {
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
        restItems.forEach(job => {
          this.jobs.push(job);
        });
        this.jobs.push({});
      }
    );
    this.workersService.getAll().subscribe(
      restItems => {
        restItems.forEach(worker => {
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
                restItems.forEach(el => {
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
                restItems.forEach(el => {
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
    this.ddts = new Map<string, any[]>();
    this.ngOnInit();
  }

  updateOrder(order, ddt) {
    const self = this;
    this.confirmDialogService.confirmThis('Vuoi proseguire con la cancellazione?',
      function () {
        order.buildingId = undefined;
        self.buildingsService.updateOrder(order)
          .then(_ => {
            const filtered = ddt.value.filter(function (obj) {
              return obj.id !== order.id;
            });
            self.ddts.set(ddt.key, filtered);
            self.orderSum = 0;
            self.orderIvaSum = 0;
            self.buildingsService.getBuildingDet(self.buildingSel).subscribe(
              items => {
                self.ddts = new Map<string, any[]>();
                for (const item in items) {
                  if (item) {
                    items[item].forEach(el => {
                      self.orderSum += el.noIvaPrice;
                      self.orderIvaSum += el.ivaPrice;
                    });
                    self.ddts.set(item, items[item]);
                    if (!self.ddtSel) {
                      self.ddtSel = undefined;
                    }
                  }
                }
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
        this.getOrders(building);
        building.details = !building.details;
      }
    }
  }

  getOrders(building) {
    this.searchString = undefined;
    this.orderSum = 0;
    this.orderIvaSum = 0;
    this.buildingSel = building.name;
    this.buildingsService.getBuildingDet(this.buildingSel).subscribe(
      items => {
        for (const item in items) {
          if (item) {
            items[item].forEach(order => {
              this.orderSum += order.noIvaPrice;
              this.orderIvaSum += order.ivaPrice;
            });
            this.ddts.set(item, items[item]);
          }
        }
        const reqAmount_round = Utils.round10(building.reqAmount, -2);
        const orderIvaSum_rounded = Utils.round10(this.orderIvaSum, -2);
        const workedHoursCost_rounded = Utils.round10(this.workedHoursCost, -2);
        const diff = reqAmount_round - orderIvaSum_rounded - workedHoursCost_rounded;
        this.projects = [{name: 'Importo complessivo lavori', value: building.reqAmount},
          {name: 'Importo materiali', value: -this.orderIvaSum},
          {name: 'Costo operai', value: -this.workedHoursCost},
          {name: 'Utili', value: diff}];
      }
    );
  }

  fetchResults(string) {
    this.searchString = string;
  }

  checkSearch(ddt) {
    const filtered = this.filterPipe.transform(ddt, this.searchString, false);
    return filtered.length > 0;
  }
}

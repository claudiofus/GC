import {Component, OnInit} from '@angular/core';
import {BuildingsService} from './buildings.service';
import {ColumnSetting} from '../../common/components/generic-table/layout.model';
import {Address} from '../../classes/address';
import {AddInvoiceService} from '../add-invoice/add-invoice.service';
import {ConfirmDialogService} from '../../common/components/confirm-dialog/confirm-dialog.service';
import {WorkersService} from '../workers/workers.service';
import {Italian} from 'flatpickr/dist/l10n/it';
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
  buildingSel: Building;
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
    self.buildingSel = building;
    self.buildingsService.getStats().subscribe(
        buildings => {
            buildings.forEach(dto => {
                if (self.buildingSel.id === dto.building.id) {
                    const budgetBuilding = dto.building.reqAmount - dto.ordersTotal - dto.workersTotal;
                    self.projects = [{name: 'Importo complessivo lavori', value: dto.building.reqAmount},
                        {name: 'Importo materiali', value: -dto.ordersTotal},
                        {name: 'Costo operai', value: -dto.workersTotal},
                        {name: 'Utili', value: budgetBuilding}];
                    self.showBuildingDetails(self, dto.building);
                }
            });
        }
    );
    self.buildingsService.getWorkingHoursSum(building.id).subscribe(
        res => {
            self.workedHours = res;
        }
    );
  }

  assignWorker(building) {
    this.jobs = [];
    this.workers = [];
    this.buildingSel = building;
    this.buildings.splice(0, this.buildings.length);
    this.showBuildingWorkers = true;
    this.buildingsService.getJobs(this.buildingSel.id).subscribe(
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

  updateWorker(building, job) {
    if (this.isEmpty(job)) {
      return;
    }
    const self = this;
    this.confirmDialogService.confirmThis('Vuoi confermare l\'operazione?',
      function () {
        self.buildingsService.assignWorker(building.id, job)
          .then(result => {
              console.log(result);
              self.getJobs();
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
            self.getJobs();
          })
          .catch(error => {
            console.error(error);
          });
      },
      function () {
      }
    );
  }

  getJobs() {
      this.buildingsService.getJobs(this.buildingSel.id).subscribe(
          restItems => {
              this.jobs = [];
              restItems.forEach(el => {
                  this.jobs.push(el);
              });
              this.jobs.push({hoursOfWork: 8});
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
            alert(`L'ordine con descrizione ${order.name} e' stato rimosso dal cantiere ${self.buildingSel.name}.`);
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

  showBuildingDetails(self, building) {
    for (const el of self.buildings) {
      el.details = false;
      if (building.name === el.name) {
        el.details = !building.details;
      }
    }
  }

  getOrders(building) {
    this.searchString = undefined;
    this.buildingSel = building;
    this.buildingsService.getBuildingDet(this.buildingSel.id).subscribe(
      items => {
        for (const item in items) {
          if (item) {
            this.ddts.set(item, items[item]);
          }
        }
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

import {Component, OnInit} from '@angular/core';
import {WorkersService} from './workers.service';
import {Worker} from '../../classes/worker';
import {Contacts} from '../../classes/contacts';
import {ContractsTerms} from '../../classes/contractsTerms';
import {Salary} from '../../classes/salary';
import {ColumnSetting} from '../../common/components/generic-table/layout.model';

@Component({
  selector: 'app-workers',
  templateUrl: './workers.component.html',
  styleUrls: ['./workers.component.css']
})
export class WorkersComponent implements OnInit {
  workers: any[];
  selWorker: Worker;
  header = ['Cantiere', 'Ore totali'];
  projects: any[];
  projectSettings: ColumnSetting[];
  addEditPanel = false;

  constructor(public workersService: WorkersService) {
  }

  ngOnInit() {
    this.projectSettings = [{primaryKey: 'name'}, {primaryKey: 'value'}];
    this.initSelWorker();
    this.getRestItems();
  }

  initSelWorker(): void {
    this.selWorker = new Worker();
    this.selWorker.contacts = new Contacts();
    this.selWorker.contractTerms = new ContractsTerms();
    this.selWorker.salary = new Salary();
  }

  getRestItems(): void {
    this.workers = [];
    this.workersService.getAll().subscribe(
      restItems => {
        restItems.forEach(building => {
          this.workers.push(building);
        });
      }
    );
  }

  addWorker(worker) {
    this.workersService.addWorker(worker)
      .then(result => {
        console.log(result);
        this.addEditPanel = false;
        this.getRestItems();
      })
      .catch(err => {
        console.error(err);
      });
  }

  edit(worker) {
    this.addEditPanel = true;
    this.workers = [];
    this.workersService.editWorker(worker.id).subscribe(
      el => this.selWorker = el
    );
  }

  hoursForBuilding(worker) {
    const self = this;
    if (!worker.id) {
      alert('Si è verificato un errore');
    } else {
      self.projects = [];
      for (const el of this.workers) {
        el.details = false;
        if (worker.name === el.name) {
          this.workersService.getHours(worker.id).subscribe(
            map => {
              for (const [building, HoursSum] of Object.entries(map)) {
                self.projects.push({name: building, value: HoursSum});
              }
            }
          );
        }
      }
    }
    worker.details = true;
  }

  closePanel() {
    if (this.addEditPanel) {
      this.initSelWorker();
      this.getRestItems();
    }
    this.addEditPanel = !this.addEditPanel;
  }
}

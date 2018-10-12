import {Component, OnInit} from '@angular/core';
import {VehiclesService} from './vehicles.service';
import {ColumnSetting} from '../../common/components/generic-table/layout.model';

@Component({
  selector: 'app-vehicles',
  templateUrl: './vehicles.component.html',
  styleUrls: ['./vehicles.component.css']
})
export class VehiclesComponent implements OnInit {
  vehicles: any[];
  insurances: any[];
  cartaxes: any[];
  revisions: any[];
  vehicle: any;
  addVehiclePanel = false;
  addPenaltyPanel = false;
  addInsurancePanel = false;
  addCarTaxPanel = false;
  addRevisionPanel = false;
  header = ['Targa', 'Data multa', 'Importo', 'Descrizione', 'Punti', 'Pagato'];
  projects: any[];
  projectSettings: ColumnSetting[];

  constructor(public vehiclesService: VehiclesService) {
  }

  ngOnInit() {
    // const options = {
    //   icon: '/assets/images/calendar.png'
    // };
    // if (!('Notification' in window)) {
    //   alert('This browser does not support desktop notification');
    // } else if ((Notification as any).permission === 'granted') {
    //   // If it's okay let's create a notification
    //   const notification = new Notification('Notifica scadenza', options);
    // } else if ((Notification as any).permission !== 'denied') {
    //   Notification.requestPermission(function (permission) {
    //     // If the user accepts, let's create a notification
    //     if (permission === 'granted') {
    //       const notification = new Notification('Notifica scadenza', options);
    //     }
    //   });
    // }

    this.projects = [];
    this.projectSettings = [{primaryKey: 'plate'}, {primaryKey: 'date', format: 'date'},
      {primaryKey: 'amount', format: 'currency'}, {primaryKey: 'description'},
      {primaryKey: 'points'}, {primaryKey: 'paid'}];
    this.getRestItems();
    this.getInsurances();
    this.getCarTaxes();
    this.getRevisions();
    this.getPenalties();
  }

  addVehicle(vehicle) {
    const _this = this;
    this.vehiclesService.addVehicle(vehicle)
      .then(function () {
        _this.addVehiclePanel = false;
        _this.getRestItems();
        _this.getInsurances();
        _this.getCarTaxes();
        _this.getRevisions();
        _this.getPenalties();
      })
      .catch(err => {
        console.error(err);
      });
  }

  addPenalty(vehicle) {
    const _this = this;
    this.vehiclesService.addPenalty(vehicle)
      .then(function () {
        _this.addPenaltyPanel = false;
        _this.getPenalties();
      })
      .catch(err => {
        console.error(err);
      });
  }

  getRestItems() {
    this.vehiclesService.getAll().subscribe(
      restItems => {
        this.vehicles = restItems;
      });
  }

  getInsurances() {
    this.vehiclesService.getAllInsurances().subscribe(
      restItems => {
        this.insurances = restItems;
      }
    );
  }

  getCarTaxes() {
    this.vehiclesService.getAllCarTaxes().subscribe(
      restItems => {
        this.cartaxes = restItems;
      }
    );
  }

  getRevisions() {
    this.vehiclesService.getAllRevisions().subscribe(
      restItems => {
        this.revisions = restItems;
      }
    );
  }

  getPenalties() {
    this.vehiclesService.getAllPenalties().subscribe(
      restItems => {
        this.projects = [];
        const __this = this;
        for (const v of restItems) {
          v.penalty.map(function (p) {
            __this.projects.push({
              plate: v.plate,
              date: p.deadlineDate,
              amount: -p.amount,
              description: p.description,
              points: -p.points,
              paid: p.paid ? 'SI' : 'NO'
            });
          });
        }
      }
    );
  }

  edit(vehicle, type) {
    this.vehicle = Object.assign({}, vehicle);
    const foundVeh = this.vehicles.find(v => {
      return v.plate === vehicle.plate;
    });
    this.vehicle.brand = foundVeh.brand;
    this.vehicle.model = foundVeh.model;
    this.hidePanels();

    if (type === 'insurance') {
      this.addInsurancePanel = true;
    } else if (type === 'car_tax') {
      this.addCarTaxPanel = true;
    } else if (type === 'revision') {
      this.addRevisionPanel = true;
    }
  }

  save(vehicle, type) {
    this.vehicle = Object.assign({}, vehicle);

    const _this = this;
    this.vehiclesService.updateVehicle(vehicle)
      .then(function () {
        _this.hidePanels();
        if (type === 'insurance') {
          _this.getInsurances();
        } else if (type === 'car_tax') {
          _this.getCarTaxes();
        } else if (type === 'revision') {
          _this.getRevisions();
        }
      })
      .catch(err => {
        console.error(err);
      });
  }

  hidePanels() {
    this.addInsurancePanel = false;
    this.addCarTaxPanel = false;
    this.addRevisionPanel = false;
  }
}

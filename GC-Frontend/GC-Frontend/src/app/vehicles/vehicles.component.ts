import {Component, OnInit} from '@angular/core';
import {VehiclesService} from './vehicles.service';
import {ColumnSetting} from '../../common/components/generic-table/layout.model';

@Component({
  selector: 'app-vehicles',
  templateUrl: './vehicles.component.html'
})
export class VehiclesComponent implements OnInit {
  vehicles: any[];
  addVehiclePanel = false;
  addPenaltyPanel = false;
  header = ['Targa', 'Data', 'Importo', 'Descrizione', 'Punti'];
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
      {primaryKey: 'points'}];
    this.getRestItems();
  }

  getRestItems() {
    this.vehiclesService.getAll().subscribe(
      restItems => {
        this.vehicles = restItems;
        const __this = this;
        for (const v of this.vehicles) {
          if (v.penalty) {
            v.penalty.map(function (p) {
              __this.projects.push({
                plate: v.plate,
                date: p.deadlineDate,
                amount: -p.amount,
                description: p.description,
                points: -p.points
              });
            });
          }
        }
      });
  }

  addVehicle(vehicle) {
    console.log(vehicle);
    this.vehiclesService.addVehicle(vehicle)
      .then(result => {
        console.log(result);
        this.addVehiclePanel = false;
        this.getRestItems();
      })
      .catch(err => {
        console.error(err);
      });
  }

  addPenalty(vehicle) {
    console.log(vehicle);
    this.vehiclesService.addPenalty(vehicle)
      .then(result => {
        console.log(result);
        this.addPenaltyPanel = false;
        this.getRestItems();
      })
      .catch(err => {
        console.error(err);
      });
  }
}

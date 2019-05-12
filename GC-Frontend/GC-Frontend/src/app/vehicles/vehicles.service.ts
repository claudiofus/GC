import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {map} from 'rxjs/operators';
import {environment} from "../../environments/environment.prod";

@Injectable()
export class VehiclesService {
  protected url = environment.contextBase + '/GCRest/rest/vehicle/all';
  protected url2 = environment.contextBase + '/GCRest/rest/vehicle/addVehicle';
  protected url3 = environment.contextBase + '/GCRest/rest/vehicle/addPenalty';
  protected url4 = environment.contextBase + '/GCRest/rest/vehicle/updateVehicle';
  protected url5 = environment.contextBase + '/GCRest/rest/vehicle/insurance/all';
  protected url6 = environment.contextBase + '/GCRest/rest/vehicle/cartax/all';
  protected url7 = environment.contextBase + '/GCRest/rest/vehicle/revision/all';
  protected url8 = environment.contextBase + '/GCRest/rest/vehicle/penalty/all';

  constructor(private http: HttpClient) {
  }

  // Rest Items Service: Read all REST Items
  getAll() {
    return this.http
      .get<any[]>(this.url)
      .pipe(map(data => data));
  }

  getAllInsurances() {
    return this.http
      .get<any[]>(this.url5)
      .pipe(map(data => data));
  }

  getAllCarTaxes() {
    return this.http
      .get<any[]>(this.url6)
      .pipe(map(data => data));
  }

  getAllRevisions() {
    return this.http
      .get<any[]>(this.url7)
      .pipe(map(data => data));
  }

  getAllPenalties() {
    return this.http
      .get<any[]>(this.url8)
      .pipe(map(data => data));
  }

  addVehicle(json): Promise<any> {
    console.log('Invoking service addVehicle');

    return this.http
      .post<any[]>(this.url2, json)
      .toPromise()
      .then(data => data);
  }

  addPenalty(json): Promise<any> {
    console.log('Invoking service addPenalty');

    return this.http
      .post<any[]>(this.url3, json)
      .toPromise()
      .then(data => data);
  }

  updateVehicle(json): Promise<any> {
    console.log('Invoking service updateVehicle');

    return this.http
      .post<any[]>(this.url4, json)
      .toPromise()
      .then(data => data);
  }
}

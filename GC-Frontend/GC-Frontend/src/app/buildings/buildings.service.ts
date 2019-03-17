import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {map} from 'rxjs/operators';
import {environment} from "../../environments/environment";

@Injectable()
export class BuildingsService {
  protected url = environment.contextBase + '/GCRest/rest/building/all';
  protected url2 = environment.contextBase + '/GCRest/rest/building/addBuilding';
  protected url3 = environment.contextBase + '/GCRest/rest/building/details/';
  protected url4 = environment.contextBase + '/GCRest/rest/order/updateOrder';

  constructor(private http: HttpClient) {
  }

  // Rest Items Service: Read all REST Items
  getAll() {
    return this.http
      .get<any[]>(this.url)
      .pipe(map(data => data));
  }

  // Rest Items Service: Read all REST Items
  addBuilding(json): Promise<any> {
    console.log('Invoking service addBuilding');

    return this.http
      .post<any[]>(this.url2, json)
      .toPromise()
      .then(data => data);
  }

  getBuildingDet(buildingName) {
    console.log('Invoking service getBuildingDet');

    return this.http
      .get<any[]>(this.url3 + buildingName)
      .pipe(map(data => data));
  }

  updateOrder(json) {
    console.log('Invoking service updateOrder');

    return this.http
      .post<any[]>(this.url4, json)
      .toPromise()
      .then(data => data);
  }
}

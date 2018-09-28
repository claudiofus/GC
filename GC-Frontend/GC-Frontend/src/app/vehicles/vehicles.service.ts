import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {map} from 'rxjs/operators';

@Injectable()
export class VehiclesService {
  protected url = 'http://localhost:8000/GCRest/rest/vehicle/all';
  protected url2 = 'http://localhost:8000/GCRest/rest/vehicle/addVehicle';
  protected url3 = 'http://localhost:8000/GCRest/rest/vehicle/updateVehicle';

  constructor(private http: HttpClient) {
  }

  // Rest Items Service: Read all REST Items
  getAll() {
    return this.http
      .get<any[]>(this.url)
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
}

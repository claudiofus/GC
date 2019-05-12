import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {map} from 'rxjs/operators';
import {environment} from "../../environments/environment.prod";

@Injectable()
export class WorkersService {
  protected url = environment.contextBase + '/GCRest/rest/worker/all';
  protected url2 = environment.contextBase + '/GCRest/rest/worker/addWorker';
  protected url3 = environment.contextBase + '/GCRest/rest/worker/updateWorker';
  protected url4 = environment.contextBase + '/GCRest/rest/worker/details/';
  protected url5 = environment.contextBase + '/GCRest/rest/worker/hours/';

  constructor(private http: HttpClient) {
  }

  getAll() {
    return this.http
      .get<any[]>(this.url)
      .pipe(map(data => data));
  }

  addWorker(json): Promise<any> {
    console.log('Invoking service addWorker');

    return this.http
      .post<any[]>(this.url2, json)
      .toPromise()
      .then(data => data);
  }

  updateWorker(json) {
    console.log('Invoking service updateWorker');

    return this.http
      .post<any[]>(this.url3, json)
      .toPromise()
      .then(data => data);
  }

  editWorker(id) {
    console.log('Invoking service getWorkerDet');

    return this.http
      .get<any>(this.url4 + id);
  }

  getHours(id) {
    console.log('Invoking service getHours');

    return this.http
      .get<any>(this.url5 + id);
  }
}

import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {map} from 'rxjs/operators';
import {environment} from "../../environments/environment.prod";

@Injectable()
export class DeadlinesService {
  protected url = environment.contextBase + '/GCRest/rest/event/all';
  protected url2 = environment.contextBase + '/GCRest/rest/event/addEvent';
  protected url3 = environment.contextBase + '/GCRest/rest/event/deleteEvent';

  constructor(private http: HttpClient) {
  }

  getAll() {
    return this.http
      .get<any[]>(this.url)
      .pipe(map(data => data));
  }

  addEvent(event): Promise<any> {
    console.log('Invoking service addEvent');

    return this.http
      .post<any[]>(this.url2, event)
      .toPromise()
      .then(data => data);
  }

  deleteEvent(event): Promise<any> {
    console.log('Invoking service deleteEvent');

    return this.http
      .post<any[]>(this.url3, event)
      .toPromise()
      .then(data => data);
  }
}

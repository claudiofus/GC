import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {map} from 'rxjs/operators';
import {environment} from '../../environments/environment';

@Injectable()
export class DashboardService {
    protected url = environment.contextBase + '/GCRest/rest/stats/all';
    protected url2 = environment.contextBase + '/GCRest/rest/stats/workload';

    constructor(private http: HttpClient) {
    }

    getAll() {
        return this.http
            .get<any[]>(this.url)
            .pipe(map(data => data));
    }

    getWorkload() {
        return this.http
            .get<any[]>(this.url2)
            .pipe(map(data => data));
    }
}

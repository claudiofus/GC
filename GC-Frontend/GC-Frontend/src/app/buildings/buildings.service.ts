import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {map} from 'rxjs/operators';
import {environment} from '../../environments/environment';

@Injectable()
export class BuildingsService {
    protected url = environment.contextBase + '/GCRest/rest/building/all';
    protected url2 = environment.contextBase + '/GCRest/rest/building/addBuilding';
    protected url3 = environment.contextBase + '/GCRest/rest/building/details/';
    protected url4 = environment.contextBase + '/GCRest/rest/order/updateOrder';
    protected url5 = environment.contextBase + '/GCRest/rest/worker/assignBuilding/';
    protected url6 = environment.contextBase + '/GCRest/rest/building/jobs/';
    protected url7 = environment.contextBase + '/GCRest/rest/building/jobsDel';
    protected url8 = environment.contextBase + '/GCRest/rest/worker/cost';
    protected url9 = environment.contextBase + '/GCRest/rest/stats/all';
    protected url10 = environment.contextBase + '/GCRest/rest/stats/workload/';

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

    assignWorker(id: number, worker: JSON) {
        console.log('Invoking service assignWorker');

        return this.http
            .post<any[]>(this.url5 + id, worker)
            .toPromise()
            .then(data => data);
    }

    getJobs(id: number) {
        return this.http
            .get<any[]>(this.url6 + id)
            .pipe(map(data => data));
    }

    deleteJob(job_id): Promise<any> {
        console.log('Invoking service deleteJob');

        return this.http
            .post<any[]>(this.url7, job_id)
            .toPromise()
            .then(data => data);
    }

    calcCost(job: JSON) {
        console.log('Invoking service assignWorker');

        return this.http
            .post<number>(this.url8, job)
            .toPromise()
            .then(data => data);
    }

    getStats() {
        return this.http
            .get<any[]>(this.url9)
            .pipe(map(data => data));
    }

    getWorkingHoursSum(id: number) {
        return this.http
            .get<any>(this.url10 + id)
            .pipe(map(data => data));
    }
}

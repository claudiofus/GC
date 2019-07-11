import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {map} from 'rxjs/internal/operators';

@Injectable()
export class GetInvoiceService {
  protected url = environment.contextBase + '/GCRest/rest/provider/providers/';
  protected url2 = environment.contextBase + '/GCRest/rest/einvoice/providers/';
  protected url3 = environment.contextBase + '/GCRest/rest/einvoice/rebuild/';

  constructor(private http: HttpClient) {
  }

  getAllProviders() {
    return this.http
      .get<any[]>(this.url)
      .pipe(map(data => data));
  }

  getInvoices(prov_id) {
    return this.http
      .get<any[]>(this.url2 + prov_id)
      .pipe(map(data => data));
  }

  getInvoice(einv_id) {
    return this.http
      .get<any[]>(this.url3 + einv_id)
      .pipe(map(data => data));
  }
}

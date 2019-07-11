import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';

@Injectable()
export class AddInvoiceService {
  protected url = environment.contextBase + '/GCRest/rest/einvoice/addEinvoice';
  protected url2 = environment.contextBase + '/GCRest/rest/provider/providers/';
  protected url3 = environment.contextBase + '/GCRest/rest/building/assignBuilding/';

  static getOrderColumns(): string[] {
    return ['Descrizione', 'UM', 'Quantità', 'Prezzo', 'Sconto', 'Totale (NO IVA)', 'Iva', 'Totale (CON IVA)'];
  }

  constructor(private http: HttpClient) {
  }

  // Rest Items Service: Read all REST Items
  addOrder(file: File, provider: string): Promise<any> {
    console.log('Invoking service insertOrder');

    const formData = new FormData();
    formData.set('file', file, file.name);
    formData.set('provider', provider);

    return this.http
      .post<any[]>(this.url, formData)
      .toPromise()
      .then(data => data);
  }

  getProvider(code: string) {
    return this.http
      .get<any[]>(this.url2 + code)
      .toPromise()
      .then(data => data);
  }

  assignBuilding(name: String, order: JSON, ddt_id: number) {
    return this.http
      .post<any[]>(this.url3 + name, {order, ddt_id})
      .toPromise()
      .then(data => data);
  }
}

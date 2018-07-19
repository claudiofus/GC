import {Injectable, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable()
export class AddInvoiceService implements OnInit {
  protected url = 'http://localhost:8000/GCRest/rest/order/insertOrder';
  protected url2 = 'http://localhost:8000/GCRest/rest/provider/providers/';
  protected url3 = 'http://localhost:8000/GCRest/rest/building/assignBuilding/';

  static getOrderColumns(): string[] {
    return ['Codice', 'Descrizione', 'UM', 'Quantità', 'Prezzo', 'Sconto', 'Prezzo tot.', 'Iva'];
  }

  ngOnInit(): void {
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

  assignBuilding(name: String, order: JSON) {
    return this.http
      .post<any[]>(this.url3 + name, order)
      .toPromise()
      .then(data => data);
  }
}

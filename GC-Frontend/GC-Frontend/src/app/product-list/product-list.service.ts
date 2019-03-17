import {Injectable, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {map} from 'rxjs/operators';
import {environment} from "../../environments/environment";

@Injectable()
export class ProductListService {
  protected url = environment.contextBase + '/GCRest/rest/product/products';
  protected url2 = environment.contextBase + '/GCRest/rest/order/addOrder/';

  static getProductColumns(): string[] {
    return ['Fornitore', 'Descrizione'];
  }

  constructor(private http: HttpClient) {
  }

  // Rest Items Service: Read all REST Items
  getAll() {
    return this.http
      .get<any[]>(this.url)
      .pipe(map(data => data));
  }

  addOrder(order, provider): Promise<any> {
    if (!provider) {
      console.error('URL:' + this.url2 + provider + ' is not valid!');
      return;
    }
    console.log('Invoking service addOrder');

    return this.http
      .post<any[]>(this.url2 + provider, order)
      .toPromise()
      .then(data => data);
  }
}

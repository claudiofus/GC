import {Injectable, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {map} from 'rxjs/operators';

@Injectable()
export class ProductListService implements OnInit {
  protected url = 'http://localhost:8000/GCRest/rest/product/products';
  protected url2 = 'http://localhost:8000/GCRest/rest/order/addOrder/';

  static getProductColumns(): string[] {
    return ['Fornitore', 'Codice', 'Descrizione'];
  }

  ngOnInit(): void {
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

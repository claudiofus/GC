import {Injectable, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {map} from 'rxjs/operators';
import {environment} from "../../../environments/environment";

@Injectable()
export class TableProductsService {
  protected url = environment.contextBase + '/GCRest/rest/product/products/prices';
  protected url2 = environment.contextBase + '/GCRest/rest/product/price/';

  static getProductColumns(): string[] {
    return ['Fornitore', 'Descrizione', 'Prezzo medio'];
  }

  constructor(private http: HttpClient) {
  }

  // Rest Items Service: Read all REST Items
  getAll() {
    return this.http
      .get<any[]>(this.url)
      .pipe(map(data => data));
  }

  getPricesHistory(name: string) {
    return this.http
      .get<any[]>(this.url2 + name)
      .pipe(map(data => data));
  }
}

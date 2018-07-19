import {Injectable, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {map} from 'rxjs/operators';

@Injectable()
export class TableProductsService implements OnInit {
  protected url = 'http://localhost:8000/GCRest/rest/product/products/prices';
  protected url2 = 'http://localhost:8000/GCRest/rest/product/price/';

  static getProductColumns(): string[] {
    return ['Fornitore', 'Codice', 'Descrizione', 'Prezzo medio'];
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

  getPricesHistory(name: string) {
    return this.http
      .get<any[]>(this.url2 + name)
      .pipe(map(data => data));
  }
}

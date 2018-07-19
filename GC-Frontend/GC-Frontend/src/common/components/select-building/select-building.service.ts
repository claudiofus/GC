import {Injectable, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {map} from 'rxjs/operators';

@Injectable()
export class BuildingListService implements OnInit {
  protected url = 'http://localhost:8000/GCRest/rest/building/all';

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
}

import {Component, Input, OnChanges} from '@angular/core';
import {ColumnMap, ColumnSetting} from './layout.model';

@Component({
  selector: 'app-generic-table',
  templateUrl: './generic-table.component.html',
  styleUrls: ['./generic-table.component.css']
})
export class GenericTableComponent implements OnChanges {
  @Input() records: any[];
  @Input() settings: ColumnSetting[];
  @Input() header: string[];
  columnMaps: ColumnMap[];

  ngOnChanges() {
    if (this.settings) {
      this.columnMaps = this.settings
        .map(col => new ColumnMap(col));
    } else {
      this.columnMaps = Object.keys(this.records[0])
        .map(key => {
          return new ColumnMap({primaryKey: key});
        });
    }
  }
}

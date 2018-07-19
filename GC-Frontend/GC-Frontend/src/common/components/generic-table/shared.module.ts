import {NgModule} from '@angular/core';
import {CommonModule, CurrencyPipe} from '@angular/common';
import {GenericTableComponent} from './generic-table.component';
import {FormatCellPipe} from './format-cell.pipe';
import {StyleCellDirective} from './style-cell.directive';

@NgModule({
  imports: [CommonModule],
  declarations: [GenericTableComponent, FormatCellPipe, StyleCellDirective],
  providers: [CurrencyPipe],
  exports: [
    CommonModule,
    GenericTableComponent
  ]
})

export class SharedModule {
}

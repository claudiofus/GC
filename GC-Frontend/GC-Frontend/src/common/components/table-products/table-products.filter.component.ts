import {Pipe, PipeTransform} from '@angular/core';

@Pipe({name: 'filtername'})
export class FilterPipe implements PipeTransform {
  transform(items: any[], stringToSearch: string, checkProvider: boolean): any {
    if (checkProvider === true) {
      return !stringToSearch ? items : items.filter(item => item.name.toLowerCase().indexOf(stringToSearch.toLowerCase()) !== -1 ||
        item.providerName.toLowerCase().indexOf(stringToSearch.toLowerCase()) !== -1);
    } else {
      return !stringToSearch ? items : items.filter(item => item.name.toLowerCase().indexOf(stringToSearch.toLowerCase()) !== -1);
    }
  }
}

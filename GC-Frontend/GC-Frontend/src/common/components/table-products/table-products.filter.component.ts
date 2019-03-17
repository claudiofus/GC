import {Pipe, PipeTransform} from '@angular/core';

@Pipe({name: 'filtername'})
export class FilterPipe implements PipeTransform {
  transform(items: any[], args: string): any {
    return !args ? items : items.filter(item => item.name.toLowerCase().indexOf(args.toLowerCase()) !== -1 ||
      item.providerName.toLowerCase().indexOf(args.toLowerCase()) !== -1);
  }
}

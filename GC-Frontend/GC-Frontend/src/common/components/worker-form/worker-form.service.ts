import {Injectable} from '@angular/core';
// https://github.com/matteocontrini/comuni-json
import comuni from '../../../assets/comuni.json';

@Injectable({
  providedIn: 'root'
})
export class WorkerFormService {
  towns = comuni.map(item => ({
    nome: item.nome,
    codCatastale: item.codiceCatastale,
    prov: item.sigla
  }));

  getTown(town: string) {
    return this.towns.find(function (el) {
      return el.codCatastale === town;
    });
  }
}

import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Worker} from '../../../classes/worker';
import {Italian} from 'flatpickr/dist/l10n/it';
import {WorkerFormService} from './worker-form.service';
import {Contacts} from '../../../classes/contacts';
import {ContractsTerms} from '../../../classes/contractsTerms';
import {Salary} from '../../../classes/salary';
import {Utils} from '../../../classes/utils';

@Component({
  selector: 'app-worker-form',
  templateUrl: './worker-form.component.html'
})
export class WorkerFormComponent {
  @Input() model: any;
  @Output() submit = new EventEmitter<Worker>();
  utils = Utils;
  locale = Italian;
  months = {A: '01', B: '02', C: '03', D: '04', E: '05', H: '06', L: '07', M: '08', P: '09', R: '10', S: '11', T: '12'};
  fiscalCodeVisited = false;

  constructor(public workerService: WorkerFormService) {
    if (!this.model) {
      this.model = new Worker();
      this.model.contacts = new Contacts();
      this.model.contractTerms = new ContractsTerms();
      this.model.salary = new Salary();
    }
  }

  static findGender(fiscalCode) {
    return fiscalCode.substring(9, 11) > 40 ? 'F' : 'M';
  }

  static estraiConsonanti(str) {
    return str ? str.replace(/[^BCDFGHJKLMNPQRSTVWXYZ]/gi, '') : '';
  }

  static estraiVocali(str) {
    return str ? str.replace(/[^AEIOU]/gi, '') : '';
  }

  surnameCode(surname) {
    const codeSurname = WorkerFormComponent.estraiConsonanti(surname) + WorkerFormComponent.estraiVocali(surname) + 'XXX';
    return codeSurname.substr(0, 3).toUpperCase();
  }

  nameCode(name) {
    let codNome = WorkerFormComponent.estraiConsonanti(name);
    if (codNome.length >= 4) {
      codNome = codNome.charAt(0) + codNome.charAt(2) + codNome.charAt(3);
    } else {
      codNome += WorkerFormComponent.estraiVocali(name) + 'XXX';
      codNome = codNome.substr(0, 3);
    }
    return codNome.toUpperCase();
  }

  completeFields(fiscalCode) {
    if (fiscalCode) {
      this.model.gender = WorkerFormComponent.findGender(fiscalCode);
      this.model.birthPlace = this.findBirthPlace(fiscalCode);
      this.model.birthProvPlace = this.findBirthProvPlace(fiscalCode);
      this.model.birthDate = this.findBirthDate(fiscalCode);
    }
    this.fiscalCodeVisited = true;
  }

  findBirthPlace(fiscalCode) {
    if (fiscalCode.charAt(12).toUpperCase() === 'Z') {
      return 'Estero';
    } else {
      const townStr = fiscalCode.substring(11, 15).toUpperCase();
      const town = this.workerService.getTown(townStr);
      return town ? town.nome.toUpperCase() : '';
    }
  }

  findBirthProvPlace(fiscalCode) {
    if (fiscalCode.charAt(12).toUpperCase() === 'Z') {
      return 'EE';
    } else {
      const townStr = fiscalCode.substring(11, 15).toUpperCase();
      const town = this.workerService.getTown(townStr);
      return town ? town.prov.toUpperCase() : '';
    }
  }

  findBirthDate(fiscalCode) {
    const year = fiscalCode.substring(6, 8);
    let day = fiscalCode.substring(9, 11);
    if (day > 40) {
      day -= 40;
    }
    const month = this.months[fiscalCode.charAt(8).toUpperCase()];
    const date = (year < 20 ? '20' : '19') + year + '-' + month + '-' + day;
    return new Date(date);
  }

  calcSeniority() {
    if (this.model.contractTerms.recruitmentDate) {
      const date1 = new Date(this.model.contractTerms.recruitmentDate);
      const date2 = this.model.contractTerms.dismissalDate ? new Date(this.model.contractTerms.dismissalDate) : new Date();
      const diff = new Date(date2.getTime() - date1.getTime());
      const years = diff.getUTCFullYear() - 1970;
      const months = diff.getUTCMonth();
      const days = diff.getUTCDate() - 1;

      if (years < 0 || months < 0 || days < 0) {
        return '';
      }

      let daysStr = '';
      let monthsStr = '';
      let yearsStr = '';

      if (days > 0) {
        daysStr = days > 1 ? ` ${days} GIORNI ` : ` ${days} GIORNO `;
      }
      if (months > 0) {
        monthsStr = months > 1 ? ` ${months} MESI ` : ` ${months} MESE `;
      }
      if (years > 0) {
        yearsStr = years > 1 ? ` ${years} ANNI ` : ` ${years} ANNO `;
      }

      return yearsStr + monthsStr + daysStr;
    }
    return '';
  }

  sendWorker() {
    this.submit.emit(this.model);
  }
}

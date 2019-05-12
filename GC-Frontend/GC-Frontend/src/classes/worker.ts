import {Contacts} from './contacts';
import {ContractsTerms} from './contractsTerms';
import {Salary} from './salary';

export class Worker {
  name: string;
  surname: string;
  gender: string;
  fiscalCode: string;
  birthDate: Date;
  birthPlace: string;
  birthProvPlace: string;
  married: boolean;
  contacts: Contacts;
  contractTerms: ContractsTerms;
  salary: Salary;
}

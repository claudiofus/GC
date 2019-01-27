import {Component, OnInit, ViewChild} from '@angular/core';
import {SelectProviderComponent} from '../../common/components/select-provider/select-provider.component';
import {AddInvoiceService} from './add-invoice.service';
import {Building} from '../../classes/building';
import {BuildingsService} from '../buildings/buildings.service';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {formatDate} from '@angular/common';

@Component({
  selector: 'app-add-invoice',
  templateUrl: './add-invoice.component.html',
  styleUrls: ['./add-invoice.component.css']
})
export class AddInvoiceComponent implements OnInit {
  @ViewChild(SelectProviderComponent)
  private selectProvider: SelectProviderComponent;
  files: FileList;
  filestring: string;
  providerObj: any;
  waitDiv: boolean;
  invoices: any[];
  itemsOrder: any;
  deliveryNotes: any;
  ddts: object;
  orderColumns: string[];
  building: Building;
  buildings: Building[];
  assignResult: string;
  addInvoiceFG = new FormGroup({
    provider: new FormControl(null, [Validators.required])
  });

  get provider() {
    return this.addInvoiceFG.get('provider');
  }

  constructor(public addInvoiceService: AddInvoiceService, public buildingsService: BuildingsService) {
  }

  ngOnInit() {
    this.providerObj = undefined;
    this.building = new Building();
    this.buildings = [];
    this.buildingsService.getAll().subscribe(
      restItems => {
        restItems.map(building => this.buildings.push(building));
      }
    );
  }

  showFileUploader() {
    return !(this.addInvoiceFG.value.provider && this.addInvoiceFG.value.provider !== '');
  }

  getFiles(event) {
    this.files = event.target.files;
    const reader = new FileReader();
    reader.onload = this._handleReaderLoaded.bind(this);
    reader.readAsBinaryString(this.files[0]);
  }

  _handleReaderLoaded(readerEvt) {
    const binaryString = readerEvt.target.result;
    this.filestring = btoa(binaryString);  // Converting binary string data.
  }

  resetForm(uploadForm) {
    uploadForm.reset();
    this.providerObj = undefined;
  }

  submitForm() {
    let row = '';
    this.waitDiv = true;
    this.orderColumns = AddInvoiceService.getOrderColumns();
    this.addInvoiceService.getProvider(this.addInvoiceFG.value.provider)
      .then(result => {
        this.providerObj = result;
      });
    this.addInvoiceService.addOrder(this.files[0], this.addInvoiceFG.value.provider)
      .then(result => {
        this.waitDiv = false;
        this.deliveryNotes = new Map<string, any>();
        this.itemsOrder = new Map<string, any>();
        this.ddts = {};
        this.invoices = result;

        for (let i = 0; i < this.invoices.length; i++) {
          const ddtMap = this.obj_to_map(this.invoices[i].ddtorders);
          this.deliveryNotes[i] = Array.from(ddtMap.keys());
          for (const el of this.deliveryNotes[i]) {
            if (ddtMap.get(el)) {
              this.itemsOrder[el] = Array.from(ddtMap.get(el).values());
            }
          }
          if (this.invoices[i].scadenze) {
            this.invoices[i].scadenze.forEach(sc => {
              row += formatDate(sc.deadlineDate, 'dd/MM/yyyy', 'it') + ' - ' + sc.amount + '€\n';
            });
          }
        }

        alert('Scadenze inserite:\n' + row);
      })
      .catch(error => {
        this.waitDiv = false;
        console.error(error);
      });
  }

  obj_to_map(obj) {
    const mp = new Map;
    Object.keys(obj).forEach(k => {
      mp.set(k, obj[k]);
    });
    return mp;
  }

  checkAll(ev, list) {
    list.forEach(x => x.state = ev.target.checked);
  }

  isAllChecked(list): boolean {
    let breakCond = true;
    if (list) {
      list.forEach((order: any) => {
        if (!order.state) {
          breakCond = false;
        }
      });
    }
    return breakCond;
  }

  assignBuilding(deliveryNote, dnIndex) {
    const building = this.itemsOrder[dnIndex].building;
    this.addInvoiceService.assignBuilding(building.name, deliveryNote)
      .then(result => {
        console.log(result);
        deliveryNote.assignResult = 'OK';
      })
      .catch(error => {
        console.error(error);
        deliveryNote.assignResult = 'KO';
      });
  }
}

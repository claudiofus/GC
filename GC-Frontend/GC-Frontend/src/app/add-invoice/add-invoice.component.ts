import {Component, OnInit, ViewChild} from '@angular/core';
import {SelectProviderComponent} from '../../common/components/select-provider/select-provider.component';
import {AddInvoiceService} from './add-invoice.service';
import {Building} from '../../classes/building';
import {BuildingsService} from '../buildings/buildings.service';
import {PaymentMod} from '../../classes/paymentMod';

@Component({
  selector: 'app-add-invoice',
  templateUrl: './add-invoice.component.html',
  styleUrls: ['./add-invoice.component.css']
})
export class AddInvoiceComponent implements OnInit {
  @ViewChild(SelectProviderComponent, { static: false })
  private selectProvider: SelectProviderComponent;
  files: FileList;
  filestring: string;
  providerObj: any;
  waitDiv: boolean;
  invoices: any[];
  itemsOrder: any;
  deliveryNotes: any;
  ddts: any;
  orderColumns: string[];
  building: Building;
  buildings: Building[];
  assignResult: string;
  mode = PaymentMod;
  hideConfirm = true;
  assignedOrders = [];

  constructor(public addInvoiceService: AddInvoiceService, public buildingsService: BuildingsService) {
  }

  ngOnInit() {
    this.providerObj = undefined;
    this.building = new Building();
    this.buildings = [];
    this.buildingsService.getAll().subscribe(
      restItems => {
        restItems.forEach(building => this.buildings.push(building));
      }
    );
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
    this.assignedOrders = [];
  }

  submitForm() {
    this.waitDiv = true;
    this.ddts = undefined;
    this.orderColumns = AddInvoiceService.getOrderColumns();
    this.addInvoiceService.addOrder(this.files[0])
      .then(result => {
        this.waitDiv = false;
        this.deliveryNotes = new Map<string, any>();
        this.itemsOrder = new Map<string, any>();
        this.invoices = result;

        for (let i = 0; i < this.invoices.length; i++) {
          this.providerObj = this.invoices[i].provider;
          this.ddts = this.invoices[i].ddts;
          const ddtMap = this.obj_to_map(this.invoices[i].ddtorders);
          this.deliveryNotes[i] = Array.from(ddtMap.keys());
          for (const el of this.deliveryNotes[i]) {
            if (ddtMap.get(el)) {
              this.itemsOrder[el] = ddtMap.get(el);
              if (this.ddts && this.ddts.length === 1) {
                this.itemsOrder[el].selDDT = this.ddts['0'].id;
              }
            }
          }
        }
      })
      .catch(error => {
        this.waitDiv = false;
        alert(error.error);
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
    this.waitDiv = true;
    const building = this.itemsOrder[dnIndex].building;
    this.addInvoiceService.assignBuilding(building.name, deliveryNote, this.itemsOrder[dnIndex].selDDT)
      .then(result => {
        console.log(result);
        deliveryNote.assignResult = 'OK';
        for (const elem of result) {
          if (!this.assignedOrders.includes(elem)) {
            this.assignedOrders.push(elem);
          }
        }
        this.waitDiv = false;
      })
      .catch(error => {
        console.error(error);
        deliveryNote.assignResult = 'KO';
        this.waitDiv = false;
      });
  }

  openBase64(fileName, data) {
    if (window.navigator && window.navigator.msSaveOrOpenBlob) { // IE workaround
      const byteCharacters = atob(data);
      const byteNumbers = new Array(byteCharacters.length);
      for (let i = 0; i < byteCharacters.length; i++) {
        byteNumbers[i] = byteCharacters.charCodeAt(i);
      }
      const byteArray = new Uint8Array(byteNumbers);
      const blob = new Blob([byteArray], {type: 'application/pdf'});
      window.navigator.msSaveOrOpenBlob(blob, fileName);
    } else if (/Chrome/.test(navigator.userAgent) && /Google Inc/.test(navigator.vendor)) {
      const a = document.createElement('a');
      a.href = 'data:application/pdf;charset=utf-8;base64,' + data;
      a.target = '_blank';
      a.download = fileName;
      document.body.appendChild(a);
      a.click();
    } else {
      window.open('data:application/pdf;base64,' + data);
    }
    return false;
  }
}

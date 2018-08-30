import {Component, OnInit, ViewChild} from '@angular/core';
import {SelectProviderComponent} from '../../common/components/select-provider/select-provider.component';
import {AddInvoiceService} from './add-invoice.service';
import {Building} from '../../classes/building';
import {BuildingsService} from '../buildings/buildings.service';
import {FormControl, FormGroup, Validators} from '@angular/forms';

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
  itemsOrder: any[];
  deliveryNotes: string[];
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
    this.waitDiv = true;
    this.orderColumns = AddInvoiceService.getOrderColumns();
    this.addInvoiceService.getProvider(this.addInvoiceFG.value.provider)
      .then(result => {
        this.providerObj = result;
      });
    this.addInvoiceService.addOrder(this.files[0], this.addInvoiceFG.value.provider)
      .then(result => {
        this.waitDiv = false;
        this.itemsOrder = result;
        this.deliveryNotes = Object.keys(result);
        for (const el of this.deliveryNotes) {
          if (this.itemsOrder[el]) {
            this.itemsOrder[el].building = new Building();
          }
        }
      })
      .catch(error => {
        this.waitDiv = false;
        console.error(error);
      });
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
    const building = this.itemsOrder[this.deliveryNotes[dnIndex]].building;
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

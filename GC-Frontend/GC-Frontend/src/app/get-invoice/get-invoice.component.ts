import {Component, OnInit} from '@angular/core';
import {GetInvoiceService} from './get-invoice.service';
import {AddInvoiceService} from '../add-invoice/add-invoice.service';
import {Building} from '../../classes/building';
import {BuildingsService} from '../buildings/buildings.service';

@Component({
    selector: 'app-get-invoice',
    templateUrl: './get-invoice.component.html',
    styleUrls: ['./get-invoice.component.css']
})
export class GetInvoiceComponent implements OnInit {
    waitDiv: boolean;
    showInvoices: boolean;
    showOrders: boolean;
    providers: string[];
    provider: number;
    invoices: any[];
    invoice: any;
    itemsOrder: any;
    deliveryNotes: any;
    providerObj: any;
    ddts: any;
    orderColumns: string[];
    assignedOrders = [];
    buildings: Building[];

    constructor(public getInvoiceService: GetInvoiceService, public buildingsService: BuildingsService,
                public addInvoiceService: AddInvoiceService) {
    }

    ngOnInit() {
        this.waitDiv = false;
        this.showInvoices = false;
        this.showOrders = false;
        this.providers = [];
        this.getInvoiceService.getAllProviders().subscribe(
            restItems => {
                restItems.forEach(provider => this.providers.push(provider));
            }
        );

        this.buildings = [];
        this.buildingsService.getAll().subscribe(
            restItems => {
                restItems.forEach(building => this.buildings.push(building));
            }
        );
    }

    findInvoices() {
        this.waitDiv = true;
        this.getInvoiceService.getInvoices(this.provider).subscribe(
            result => {
                this.invoices = result;
                if (this.invoices.length === 1) {
                    this.invoice = this.invoices[0].id;
                }
                this.showInvoices = true;
                this.waitDiv = false;
            }
        );
    }

    clearInvoices() {
        this.invoices = [];
        this.invoice = undefined;
        this.showOrders = false;
        this.findInvoices();
    }

    getInvoice() {
        this.orderColumns = AddInvoiceService.getOrderColumns();
        this.getInvoiceService.getInvoice(this.invoice).subscribe(result => {
            this.showOrders = true;
            this.deliveryNotes = new Map<string, any>();
            this.itemsOrder = new Map<string, any>();

            this.ddts = result['ddts'];
            const ddtMap = this.obj_to_map(result['ddtorders']);
            this.deliveryNotes = Array.from(ddtMap.keys());
            for (const el of this.deliveryNotes) {
                if (ddtMap.get(el)) {
                    this.itemsOrder[el] = ddtMap.get(el);
                    if (this.ddts && this.ddts.length === 1) {
                        this.itemsOrder[el].selDDT = this.ddts['0'].id;
                    }
                }
            }
        });
    }

    assignBuilding(deliveryNote, dnIndex) {
        this.waitDiv = true;
        const building = this.itemsOrder[dnIndex].building;
        this.addInvoiceService.assignBuilding(building.id, deliveryNote, this.itemsOrder[dnIndex].selDDT)
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

    findBuildingById(id: number) {
        const buildingSel = this.buildings.find(function (building) {
            return building.id === id;
        });
        return buildingSel.name;
    }
}

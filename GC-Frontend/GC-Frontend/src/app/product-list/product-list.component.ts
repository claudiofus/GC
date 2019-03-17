import {Component, OnInit} from '@angular/core';
import {ProductListService} from './product-list.service';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Order} from '../../classes/order';
import {Utils} from '../../classes/utils';
import {Italian} from 'flatpickr/dist/l10n/it';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html'
})
export class ProductListComponent implements OnInit {
  products: any[];
  addPanel: boolean;
  locale = Italian;
  public utils = Utils;
  addOrderFG = new FormGroup({
    code: new FormControl(null, [Validators.required, Validators.minLength(3)]),
    name: new FormControl(null, [Validators.required, Validators.minLength(3)]),
    um: new FormControl(null, [Validators.required]),
    quantity: new FormControl(null, [Validators.required]),
    price: new FormControl(null, [Validators.required]),
    noIvaPrice: new FormControl(null, [Validators.required]),
    iva: new FormControl(null, [Validators.required]),
    discount: new FormControl({value: null, disabled: true}),
    date_order: new FormControl(null, [Validators.required, Validators.maxLength(10)]),
    provider: new FormControl(null, [Validators.required]),
    building_id: new FormControl(null, [Validators.required])
  });

  get code() {
    return this.addOrderFG.get('code');
  }

  get name() {
    return this.addOrderFG.get('name');
  }

  get um() {
    return this.addOrderFG.get('um');
  }

  get quantity() {
    return this.addOrderFG.get('quantity');
  }

  get price() {
    return this.addOrderFG.get('price');
  }

  get noIvaPrice() {
    return this.addOrderFG.get('noIvaPrice');
  }

  get iva() {
    return this.addOrderFG.get('iva');
  }

  get date_order() {
    return this.addOrderFG.get('date_order');
  }

  get provider() {
    return this.addOrderFG.get('provider');
  }

  get building_id() {
    return this.addOrderFG.get('building_id');
  }

  constructor(public productListService: ProductListService) {
  }

  ngOnInit() {
    this.getRestItems();
    this.addPanel = false;
  }

  // Read all REST Items
  getRestItems(): void {
    this.products = [];
    this.productListService.getAll().subscribe(
      restItems => {
        restItems.map(product => this.products.push(product));
      }
    );
  }

  calcDiscount(price, noIvaPrice, quantity) {
    let discount = 0;
    if (!price || !noIvaPrice || !quantity || price === 0 || noIvaPrice === 0) {
      this.addOrderFG.controls['discount'].setValue(discount.toFixed(2));
      return;
    }

    if (quantity === 0) {
      discount = 100;
      this.addOrderFG.controls['discount'].setValue(discount.toFixed(2));
      return;
    }

    noIvaPrice = noIvaPrice / quantity;
    discount = (1 - (noIvaPrice / price)) * 100;
    this.addOrderFG.controls['discount'].setValue(discount.toFixed(2));
  }

  updateProduct(event) {
    if (event.name && event.code) {
      this.addOrderFG.controls['name'].setValue(event.name);
      this.addOrderFG.controls['code'].setValue(event.code);
      this.addOrderFG.controls['provider'].setValue(event.providerName);
    }
  }

  addOrder() {
    const ord = new Order();
    Object.assign(ord, this.addOrderFG.value);
    delete ord.provider;
    this.productListService.addOrder(ord, this.provider.value)
      .then(_ => {
        this.getRestItems();
        this.addOrderFG.reset();
      })
      .catch(err => {
        console.error(err);
      });
  }
}

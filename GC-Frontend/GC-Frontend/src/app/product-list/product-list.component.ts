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
    name: new FormControl(null, [Validators.required, Validators.minLength(3)]),
    um: new FormControl(null, [Validators.required]),
    quantity: new FormControl(null, [Validators.required]),
    price: new FormControl(null, [Validators.required]),
    noIvaPrice: new FormControl(null, [Validators.required]),
    iva: new FormControl(null, [Validators.required]),
    discount: new FormControl({value: null, disabled: true}),
    ivaPrice: new FormControl(null, [Validators.required]),
    dateOrder: new FormControl(null, [Validators.required, Validators.maxLength(10)]),
    provider: new FormControl(null, [Validators.required]),
    buildingId: new FormControl(null, [Validators.required])
  });

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

  get provider() {
    return this.addOrderFG.get('provider');
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
        restItems.forEach(product => this.products.push(product));
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
    if (event.name) {
      this.addOrderFG.controls['name'].setValue(event.name);
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

  formatValue(param) {
    this.addOrderFG.controls[param].setValue(this.utils.fixAmount(this.addOrderFG.value[param]));
  }

  calcIVA() {
    if (this.noIvaPrice.invalid || this.iva.invalid) {
      return;
    }

    const ivaTot = this.noIvaPrice.value * this.iva.value / 100;
    const noIvaPriceNum = typeof this.noIvaPrice.value === 'string' ? parseFloat(this.noIvaPrice.value) : this.noIvaPrice.value;
    const ivaTotAmount = this.utils.fixAmount(noIvaPriceNum + ivaTot);
    this.addOrderFG.controls['ivaPrice'].setValue(ivaTotAmount);
  }
}

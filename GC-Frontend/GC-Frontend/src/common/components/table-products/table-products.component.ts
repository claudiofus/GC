import {Component, OnInit} from '@angular/core';
import {TableProductsService} from './table-products.service';

@Component({
  selector: 'app-table',
  templateUrl: './table-products.component.html',
  styleUrls: ['./table-products.component.css'],
})
export class TableComponent implements OnInit {
  products: string[];
  prices: string[] = [];
  columns: string[];
  searchString: string;
  selected: string;
  oldItem: any;

  constructor(private tableProductsService: TableProductsService) {
  }

  ngOnInit() {
    this.products = [];
    this.columns = TableProductsService.getProductColumns();
    this.tableProductsService.getAll().subscribe(
      restItems => {
        restItems.map(product => this.products.push(product));
      }
    );
  }

  addRow(item) {
    if (!item) {
      return;
    }

    if (item.showDetails) {
      item.showDetails = false;
      return;
    }

    if (this.selected !== '' && this.oldItem && this.oldItem.hasOwnProperty('showDetails')) {
      this.oldItem.showDetails = false;
    }

    this.selected = item.name;
    this.oldItem = item;
    item.showDetails = true;
    this.prices = [];
    this.tableProductsService.getPricesHistory(item.name).subscribe(
      restItems => {
        restItems.map(prHist => this.prices.push(prHist));
      }
    );
  }
}

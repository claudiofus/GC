import {Component, EventEmitter, Output} from '@angular/core';

@Component({
  selector: 'app-search-bar',
  templateUrl: './search-bar.component.html'
})
export class SearchBarComponent {
  searchString: string;
  @Output() searchEvent = new EventEmitter();

  constructor() {
  }

  sendSearch(searchValue: string) {
    this.searchEvent.emit(searchValue);
  }

  clearSearch() {
    this.searchString = undefined;
    this.searchEvent.emit();
  }
}

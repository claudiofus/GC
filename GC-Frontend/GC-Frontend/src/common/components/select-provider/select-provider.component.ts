import {Component, Input, OnInit} from '@angular/core';
import {ProviderListService} from './select-provider.service';
import {FormGroup} from '@angular/forms';

@Component({
  selector: 'app-select-provider',
  templateUrl: './select-provider.component.html',
  providers: [ProviderListService]
})

export class SelectProviderComponent implements OnInit {
  providers: string[];
  @Input() group: FormGroup;

  constructor(private providerService: ProviderListService) {
  }

  ngOnInit() {
    this.providers = [];
    this.providerService.getAll().subscribe(
      restItems => {
        restItems.forEach(provider => this.providers.push(provider));
      }
    );
  }
}

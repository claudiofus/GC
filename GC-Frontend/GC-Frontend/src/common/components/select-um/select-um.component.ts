import {Component, Input, OnInit} from '@angular/core';
import {UMListService} from './select-um.service';
import {FormGroup} from '@angular/forms';

@Component({
  selector: 'app-select-um',
  templateUrl: './select-um.component.html',
  providers: [UMListService]
})

export class SelectUmComponent implements OnInit {
  UMs: string[];
  @Input() group: FormGroup;

  constructor(private umService: UMListService) {
  }

  ngOnInit() {
    this.UMs = [];
    this.umService.getAll().subscribe(
      restItems => {
        restItems.map(UM => this.UMs.push(UM));
      }
    );
  }
}

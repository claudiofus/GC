import {Component, Input, OnInit} from '@angular/core';
import {UMListService} from './select-um.service';
import {FormControl, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-select-um',
  templateUrl: './select-um.component.html',
  providers: [UMListService]
})

export class SelectUmComponent implements OnInit {
  UMs: string[];
  @Input() group = new FormGroup({
      um: new FormControl(null, [Validators.required])
  });

  constructor(private umService: UMListService) {
  }

  ngOnInit() {
    this.UMs = [];
    this.umService.getAll().subscribe(
      restItems => {
        restItems.forEach(UM => this.UMs.push(UM));
      }
    );
  }
}

import {Injectable, OnInit} from '@angular/core';
import {DomSanitizer} from '@angular/platform-browser';

@Injectable()
export class AppService implements OnInit {
  ngOnInit(): void {
  }

  constructor(public sanitizer: DomSanitizer) {
  }
}

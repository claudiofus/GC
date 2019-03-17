import {Injectable} from '@angular/core';
import {DomSanitizer} from '@angular/platform-browser';

@Injectable()
export class AppService {
  constructor(public sanitizer: DomSanitizer) {
  }
}

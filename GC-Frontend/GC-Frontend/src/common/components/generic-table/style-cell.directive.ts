import {Directive, ElementRef, Input, OnInit, Renderer2} from '@angular/core';

@Directive({selector: '[appStyleCell]'})
export class StyleCellDirective implements OnInit {
  @Input() appStyleCell: string;

  constructor(private el: ElementRef,
              private renderer: Renderer2) {
  }

  ngOnInit() {
    if (typeof this.appStyleCell === 'number') {
      if (this.appStyleCell < 0) {
        this.renderer.setStyle(this.el.nativeElement, 'color', '#f00');
      } else if (this.appStyleCell > 0) {
        this.renderer.setStyle(this.el.nativeElement, 'color', 'darkgreen');
      }
      this.renderer.setStyle(this.el.nativeElement, 'font-weight', 'bold');
      this.renderer.setStyle(this.el.nativeElement, 'text-align', 'right');
    } else {
      this.renderer.setStyle(this.el.nativeElement, 'text-align', 'left');
    }
  }
}

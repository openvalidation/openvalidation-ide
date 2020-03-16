import { AfterViewInit, Directive, ElementRef, HostListener, Inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';

@Directive({
  selector: '[ovideEllipsis]'
})
export class EllipsisDirective implements AfterViewInit {

  private get el(): HTMLElement {
    return this.elementRef.nativeElement;
  }

  private innerText: string;

  constructor(
    private readonly elementRef: ElementRef,
    @Inject(PLATFORM_ID) private readonly platformId
  ) { }

  public ngAfterViewInit(): void {
    this.truncate();
  }

  @HostListener('window:resize')
  private onWindowResize() {
    this.truncate();
  }

  private truncate(): void {
    if (!isPlatformBrowser(this.platformId)) {
      return;
    }

    if (this.innerText === undefined) {
      this.innerText = this.el.innerText.trim();
    }

    this.el.innerText = this.innerText;

    const text = this.innerText.split(' ');
    while (text.length > 0 && this.el.scrollHeight > this.el.clientHeight) {
      text.pop();
      this.el.innerText = `${text.join(' ')}…`;
    }
  }
}

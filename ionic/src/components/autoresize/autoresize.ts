// An autoresize directive that works with ion-textarea in Ionic 2
// Usage example: <ion-textarea autoresize [(ngModel)]="body"></ion-textarea>
// Based on https://www.npmjs.com/package/angular2-autosize

import { Directive, HostListener, ElementRef } from "@angular/core";

@Directive({
  selector: "ion-textarea[autoresize]" // Attribute selector
})
export class Autoresize {

  @HostListener("input", ["$event.target"])
  onInput(textArea: HTMLTextAreaElement): void {
    this.adjust();
  }

  constructor(public element: ElementRef) { }

  /**
   * Lifecycle event that will be fired after the elements are available in the DOM
   */
  ngOnInit(): void {
    this.adjust();
  }

  /**
   * Set the new height of the textarea
   */
  adjust(): void {
    setTimeout(() => {
      let ta = this.element.nativeElement.querySelector("textarea");
      if (ta) {
        ta.style.overflow = "hidden";
        ta.style.height = "auto";
        ta.style.height = ta.scrollHeight + "px";
      }
    }, 100);
  }

}

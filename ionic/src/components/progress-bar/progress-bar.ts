import { Component, Input } from '@angular/core';

@Component({
  selector: 'progress-bar',
  templateUrl: 'progress-bar.html'
})
/**
 * A component that shows a progressbar when passed a value from 0 to 100
 */
export class ProgressBarComponent {

  /**
   * The current progress (0 - 100)
   */
  @Input('progress') progress;

  /**
   * The constructor, it shall be empty
   */
  constructor() { }

}

import {Component, Input} from '@angular/core';

@Component({
  selector: 'recipient',
  templateUrl: 'recipient.html'
})
export class RecipientComponent {

  /**
   * The title of the component
   */
  @Input() label: string;

  /**
   * An array of email addresses
   */
  @Input() recipients: string[];

  /**
   * The string that the user entered
   * @type {string}
   */
  private input = '';

  constructor() {
    if (!this.recipients) {
      this.recipients = [];
    }
  }

  /**
   * Remove a recipient from the list
   * @param recipient
   */
  removeRecipient(recipient: string) {
    let index = this.recipients.indexOf(recipient.trim().toLocaleLowerCase());
    if (index > -1) {
      this.recipients.splice(index, 1);
    }
  }

  /**
   * Add a recipient to the list
   */
  addRecipient() {
    if (this.recipients.indexOf(this.input.trim().toLocaleLowerCase()) == -1 && this.input.trim() != '') {
        this.recipients.push(this.input.trim().toLocaleLowerCase());
        this.input = "";
     }
  }

}

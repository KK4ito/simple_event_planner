import {Component, Input, Output, EventEmitter} from '@angular/core';

/**
 * Generated class for the RecipientComponent component.
 *
 * See https://angular.io/docs/ts/latest/api/core/index/ComponentMetadata-class.html
 * for more info on Angular Components.
 */
@Component({
  selector: 'recipient',
  templateUrl: 'recipient.html'
})
export class RecipientComponent {

  @Input() label: string;
  @Input() recipients: string[];

  private input = "";

  constructor() {
    if(!this.recipients){
      this.recipients = [];
    }
  }

  removeRecipient(recipient:string){
    var index = this.recipients.indexOf(recipient.trim().toLocaleLowerCase());
    if (index > -1) {
      this.recipients.splice(index, 1);
    }
  }

  addRecipient(){
    if (this.recipients.indexOf(this.input.trim().toLocaleLowerCase()) == -1 && this.input.trim() != '') {
        this.recipients.push(this.input.trim().toLocaleLowerCase());
        this.input = "";
     }
  }

}

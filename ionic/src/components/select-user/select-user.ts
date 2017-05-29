import {Component, Output, EventEmitter, Input} from '@angular/core';
import {Subject} from "rxjs";
import { CompleterService, CompleterData } from 'ng2-completer';
import {ApiService} from "../../providers/api.service";
import {User} from "../../models/User";
import {environment} from "../../../environments/environment";

@Component({
  selector: 'select-user',
  templateUrl: 'select-user.html'
})
export class SelectUserComponent {

  /**
   * The placeholder shown in the autocomplete box
   */
  @Input() placeholder: string;

  /**
   * The callback that is called when an element is selected
   * @type {EventEmitter}
   */
  @Output() onSelected: EventEmitter<[User]> = new EventEmitter();

  /**
   * A subject that stores the results of the search
   * @type {Subject}
   */
  private results$ = new Subject();

  /**
   * A flag to indicate if the results have been fetched
   * @type {boolean}
   */
  resultsFetched = false;

  /**
   * The data source for the autocomplete
   * @type {LocalData}
   */
  datasource = this.completerService.local(this.results$ , null,  'name');

  /**
   * The current search string
   * @type {string}
   */
  searchString = '';

  /**
   * Constructor, inject providers and set initial values
   * @param completerService
   * @param apiService
   */
  constructor(private completerService: CompleterService, private apiService: ApiService){
    this.resultsFetched = false;

    this.datasource.descriptionField('email');
    this.datasource.imageField('avatar');
  }

  /**
   * Search for a user. Take the searchString and get the results from the API
   * @param event
   */
  searchUser(event: any){
    if (this.searchString && this.searchString.length > 2 && !this.resultsFetched) {
      this.apiService.getUsersByName(this.searchString)
        .then(users => {
          let newUsers = [];
          for(let i = 0; i < users.length; i++){
            let user: any;
            user = users[i];
            user.name = (user.firstName + ' ' + user.lastName).trim();
            user.avatar = environment.baseUrl + user.imageUri;
            newUsers.push(user);
          }
          this.results$.next(newUsers);
        });
      this.resultsFetched = true;
    } else {
      this.resultsFetched = false;
    }
  }

  /**
   * Callback when selecting an item from the dropdown list
   * @param selected
   */
  selectedItem(selected: any) {
    if (selected) {
      this.onSelected.emit([selected.originalObject]);
      this.searchString = '';
    }
  }

}

import {Component, Output, EventEmitter, Input} from '@angular/core';
import {Subject} from "rxjs";
import { CompleterService, CompleterData } from 'ng2-completer';
import {ApiService} from "../../providers/api.service";
import {User} from "../../models/User";
import {environment} from "../../../environments/environment";

/*
  Generated class for the SelectUser component.

  See https://angular.io/docs/ts/latest/api/core/index/ComponentMetadata-class.html
  for more info on Angular 2 Components.
*/
@Component({
  selector: 'select-user',
  templateUrl: 'select-user.html'
})
export class SelectUserComponent {

  @Input() placeholder: string;
  @Output() onSelected: EventEmitter<[User]> = new EventEmitter();

  private results$ = new Subject();

  resultsFetched = false;
  datasource = this.completerService.local(this.results$ , null,  'name');
  searchString = "";

  constructor(private completerService: CompleterService, private apiService:ApiService){
    this.resultsFetched = false;

    this.datasource.descriptionField('email');
    this.datasource.imageField('avatar');
  }

  searchUser(event:any){
    if(this.searchString && this.searchString.length > 2 && !this.resultsFetched){
      this.apiService.getUsersByName(this.searchString)
        .then(users => {
          var newUsers = [];
          for(var i = 0; i < users.length; i++){
            var user:any;
            user = users[i];
            user.name = (user.firstName + ' ' + user.lastName).trim();
            user.avatar = environment.baseUrl + user.imageUri;
            newUsers.push(user);
          }
          this.results$.next(newUsers);
        });
      this.resultsFetched = true;
    }else{
      this.resultsFetched = false;
    }
  }

  selectedItem(selected : any) {
    if(selected) {
      this.onSelected.emit([selected.originalObject]);
      this.searchString = "";
    }
  }

}

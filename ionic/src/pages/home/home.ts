import { Component } from '@angular/core';

import { NavController } from 'ionic-angular';
import { ApiService } from "../../providers/api.service";
import { Event } from '../../models/Event';
import { RoleType } from '../../models/RoleType';
import { DetailPage } from "../detail/detail";
import {AuthService} from "../../providers/auth.service";

@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})
export class HomePage {
  events: Event[];

  // Static binding workaround (http://stackoverflow.com/questions/39193538/how-to-bind-static-variable-of-component-in-html-in-angular-2)
  public RoleType = RoleType;

  constructor(private _apiService: ApiService, private navCtrl: NavController, public authService: AuthService) {
    this._apiService.getEvents().then(data => this.events = data);
  }

  openEvent(id: number) {
    this.navCtrl.push(DetailPage, {
      id: id
    })
  }

  openDialog(ev) {
    ev.stopPropagation();
  }

}

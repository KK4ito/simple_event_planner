import { Component } from '@angular/core';

import { NavController } from 'ionic-angular';
import { ApiService } from "../../providers/api.service";
import { Event } from '../../models/Event';
import { DetailPage } from "../detail/detail";

@Component({
  selector: 'page-page1',
  templateUrl: 'page1.html'
})
export class Page1 {
  events: Event[];

  constructor(private _apiService: ApiService, private navCtrl: NavController) {
    this._apiService.getEvents().then(data => this.events = data);
  }

  openEvent(id: number) {
    this.navCtrl.push(DetailPage, {
      id: id
    });
  }

  openDialog(ev) {
    ev.stopPropagation();
  }

}

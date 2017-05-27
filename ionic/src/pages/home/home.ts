import { Component } from '@angular/core';

import { NavController } from 'ionic-angular';
import { ApiService } from "../../providers/api.service";
import { Event } from '../../models/Event';
import { RoleType } from '../../models/RoleType';
import { DetailPage } from "../detail/detail";
import { AuthService } from "../../providers/auth.service";

@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})
export class HomePage {
  upcomingEvents: Event[] = [];
  pastEvents: Event[] = [];

  // Static binding workaround (http://stackoverflow.com/questions/39193538/how-to-bind-static-variable-of-component-in-html-in-angular-2)
  public RoleType = RoleType;

  constructor(private _apiService: ApiService, private navCtrl: NavController, public authService: AuthService) {
    this._apiService.getEvents()
      .then(data => {
        let sorted = data.sort((a, b) => {
          if (a.startTime < b.startTime) return -1;
          if (a.startTime > b.startTime) return 1;
          return 0;
        })
        this.upcomingEvents = sorted.filter(e => new Date(e.startTime) > new Date());
        this.pastEvents = sorted.filter(e => new Date(e.startTime) < new Date());
      })
      .catch(console.log);
  }

  /**
   * Open page on click
   * @param id
   */
  openEvent(id: number) {
    this.navCtrl.push(DetailPage, {
      id: id
    })
  }
}

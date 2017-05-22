import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';
import { ApiService } from "../../providers/api.service";
import { EventAttendeeFlat } from "../../models/EventAttendeeFlat";
import { FoodType } from "../../models/FoodType";
import { Event } from '../../models/Event';

@Component({
  selector: 'page-detail-print',
  templateUrl: 'detail-print.html',
})
export class DetailPrintPage {
  externalAttendees: EventAttendeeFlat[] = [];
  internalAttendees: EventAttendeeFlat[] = [];

  nofDrinks: number = 0;
  nofVegi: number = 0;
  nofMeat: number = 0;

  event: Event;

  constructor(public navCtrl: NavController, public navParams: NavParams, public _apiService: ApiService) {
    this._apiService.getEvent(this.navParams.get('id')).then(event => {
      this.event = event;
      console.log(event);
    });
    this._apiService.getPrint(this.navParams.get('id')).then(res => {
      this.internalAttendees = res.filter(e => e.internal);
      this.externalAttendees = res.filter(e => !e.internal);
      res.forEach(e => {
        if (e.drink) this.nofDrinks++;
        if (e.foodType === FoodType.VEGI) this.nofVegi++;
        if (e.foodType === FoodType.NORMAL) this.nofMeat++;
      });
    });
  }

  /**
   * Print the current page
   */
  print() {
    window.print();
  }

}

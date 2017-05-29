import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';
import { ApiService } from "../../providers/api.service";
import { EventAttendeeFlat } from "../../models/EventAttendeeFlat";
import { FoodType } from "../../models/FoodType";
import { Event } from '../../models/Event';
import {User} from "../../models/User";

@Component({
  selector: 'page-detail-print',
  templateUrl: 'detail-print.html',
})
export class DetailPrintPage {
  /**
   * External users array
   * @type {Array}
   */
  externalAttendees: EventAttendeeFlat[] = [];

  /**
   * Internal (switch-aai) users array
   * @type {Array}
   */
  internalAttendees: EventAttendeeFlat[] = [];

  /**
   * Total number of drinks
   * @type {number}
   */
  nofDrinks: number = 0;

  /**
   * Total number of vegi sandwiches
   * @type {number}
   */
  nofVegi: number = 0;

  /**
   * Total number of meat sandwiches
   * @type {number}
   */
  nofMeat: number = 0;

  /**
   * Current event
   */
  event: Event;

  /**
   * Speakers array
   */
  speakers: User[];

  constructor(public navCtrl: NavController, public navParams: NavParams, public _apiService: ApiService) {
    this._apiService.getEvent(this.navParams.get('id')).then(event => {
      this.event = event;
    });

    this._apiService.getSpeakers(this.navParams.get('id')).then(speakers => {
      this.speakers = speakers;
    });

    this._apiService.getPrint(this.navParams.get('id')).then(res => {
      this.internalAttendees = res.filter(e => e.internal);
      this.externalAttendees = res.filter(e => !e.internal);
      res.forEach(e => {
        if (e.drink) this.nofDrinks++;
        if (e.foodType === FoodType.VEGI) this.nofVegi++;
        if (e.foodType === FoodType.MEAT) this.nofMeat++;
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

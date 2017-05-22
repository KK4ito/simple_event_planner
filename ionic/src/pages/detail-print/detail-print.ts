import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import {ApiService} from "../../providers/api.service";

@Component({
  selector: 'page-detail-print',
  templateUrl: 'detail-print.html',
})
export class DetailPrintPage {

  constructor(public navCtrl: NavController, public navParams: NavParams, public _apiService: ApiService) {
    this._apiService.getAttendees(this.navParams.get('id')).then(res => {console.log(res)})
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad DetailPrintPage');
  }

  print() {
    window.print();
  }

}

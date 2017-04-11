import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';
import {User} from "../../models/User";
import {ApiService} from "../../providers/api.service";

/*
  Generated class for the Users page.

  See http://ionicframework.com/docs/v2/components/#navigation for more info on
  Ionic pages and navigation.
*/
@Component({
  selector: 'page-users',
  templateUrl: 'users.html'
})
export class UsersPage {

  users: User[];

  constructor(public navCtrl: NavController, public navParams: NavParams, private apiService:ApiService) {
    apiService.getUsers().then((data) => this.users = data);
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad UsersPage');
  }

  createUser(){

  }

}

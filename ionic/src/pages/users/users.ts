import { Component } from '@angular/core';
import {NavController, NavParams, ModalController, Events} from 'ionic-angular';
import {User} from "../../models/User";
import {ApiService} from "../../providers/api.service";
import {UpdateUserPage} from "../update-user/update-user";

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

  constructor(public navCtrl: NavController, public navParams: NavParams, private apiService:ApiService, private modalCtrl: ModalController, private events:Events) {
    this.events.subscribe('users:changed', () => {
      this.refreshUsers();
    });
    this.refreshUsers();
  }

  refreshUsers(){
    this.apiService.getUsers().then((data) => this.users = data);
  }

  updateUser(user:User){
      this.modalCtrl.create(UpdateUserPage, {user: user}, {enableBackdropDismiss: false}).present();
  }

}

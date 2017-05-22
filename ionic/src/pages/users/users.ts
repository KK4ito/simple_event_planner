import { Component } from '@angular/core';
import {NavController, NavParams, ModalController, Events} from 'ionic-angular';
import {User} from "../../models/User";
import {ApiService} from "../../providers/api.service";
import {UpdateUserPage} from "../update-user/update-user";

@Component({
  selector: 'page-users',
  templateUrl: 'users.html'
})
export class UsersPage {

  /**
   * Store the list of users
   */
  users: User[];

  constructor(public navCtrl: NavController, public navParams: NavParams, private apiService:ApiService, private modalCtrl: ModalController, private events:Events) {
    this.events.subscribe('users:changed', () => {
      this.refreshUsers();
    });
    this.refreshUsers();
  }

  /**
   * Load list of users from the server
   */
  refreshUsers(){
    this.apiService.getUsers().then((data) => this.users = data);
  }

  /**
   * Update a user
   *
   * @param user
   */
  updateUser(user: User){
      this.modalCtrl.create(UpdateUserPage, {user: user}, {enableBackdropDismiss: false}).present();
  }

}

import { Component } from '@angular/core';
import {NavController, NavParams, ToastController} from 'ionic-angular';
import {ApiService} from "../../providers/api.service";
import {User} from "../../models/User";
import {Role} from "../../models/Role";

@Component({
  selector: 'page-permissions',
  templateUrl: 'permissions.html'
})
export class PermissionsPage {

  users: User[];
  oldUser: User;

  constructor(public navCtrl: NavController, public navParams: NavParams, private apiService:ApiService, private toastCtrl:ToastController) {
    this.refreshUsers();
  }

  refreshUsers(){
    this.apiService.getUsersByRole(Role.ADMINISTRATOR).then((data) => this.users = data);
  }

  onSelected(user:User){
    this.oldUser = Object.assign({}, user);
    user.role = Role.ADMINISTRATOR;
    this.updateUser(user);
  }

  removeRole($event, user:User){
    this.oldUser = Object.assign({}, user);
    user.role = Role.REGISTERED;
    this.updateUser(user);
  }

  updateUser(user:User, showUndo = true){
    this.apiService.updateUser(user.id, user).then(() =>{
    this.refreshUsers();
      let toast = this.toastCtrl.create({
        message: 'User role successfully updated.',
        duration: 3000,
        showCloseButton: true,
        closeButtonText: "undo",
        position: 'bottom right'
      });
      toast.onDidDismiss((data, role) => {
        if (role == "close") {
          this.updateUser(this.oldUser, false);
        }
      });
      toast.present();
  });
  }
}
import { Component } from '@angular/core';
import {NavController, NavParams, ToastController} from 'ionic-angular';
import {ApiService} from "../../providers/api.service";
import {User} from "../../models/User";
import {RoleType} from "../../models/RoleType";
import {TranslateService} from '@ngx-translate/core';

@Component({
  selector: 'page-permissions',
  templateUrl: 'permissions.html'
})
export class PermissionsPage {

  users: User[];
  oldUser: User;

  constructor(public navCtrl: NavController, public navParams: NavParams, private apiService: ApiService, private toastCtrl: ToastController, private translate: TranslateService) {
    this.refreshUsers();
  }

  refreshUsers(){
    this.apiService.getUsersByRole(RoleType.ADMINISTRATOR).then((data) => this.users = data);
  }

  onSelected(user:User){
    this.oldUser = Object.assign({}, user);
    user.role = RoleType.ADMINISTRATOR;
    this.updateUser(user);
  }

  removeRole($event, user:User){
    this.oldUser = Object.assign({}, user);
    user.role = RoleType.REGISTERED;
    this.updateUser(user);
  }

  updateUser(user:User, showUndo = true){
    this.apiService.updateUser(user).then(() =>{
    this.refreshUsers();
    this.translate.get(['PERMISSIONS.USER_UPDATED', 'PERMISSIONS.USER_UPDATED_UNDO']).subscribe(str => {
      let toast = this.toastCtrl.create({
        message: str[0],
        duration: 3000,
        showCloseButton: true,
        closeButtonText: str[1],
        position: 'bottom right'
      });
      toast.onDidDismiss((data, role) => {
        if (role == "close") {
          this.updateUser(this.oldUser, false);
        }
      });
      toast.present();
    });
  });
  }
}

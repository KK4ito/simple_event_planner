import { Component } from '@angular/core';
import {NavParams, ViewController, ToastController, Events, AlertController} from 'ionic-angular';
import {User} from "../../models/User";
import {ApiService} from "../../providers/api.service";
import {TranslateService} from "@ngx-translate/core";

/*
  Generated class for the NewUser page.

  See http://ionicframework.com/docs/v2/components/#navigation for more info on
  Ionic pages and navigation.
*/
@Component({
  selector: 'page-update-user',
  templateUrl: 'update-user.html'
})
export class UpdateUserPage {

  public user = new User();
  oldUser:User;

  constructor(private params: NavParams, private viewCtrl: ViewController,private apiService:ApiService, private toastCtrl:ToastController, private translateService: TranslateService, public events: Events, private alertCtrl: AlertController) {
    var user = params.get('user');
    if(user){
      this.user = user;
      this.oldUser = Object.assign({}, this.user);
    }
  }

  dismiss() {
    this.viewCtrl.dismiss();
  }

  save(user: User, showToast = true){
    if(this.oldUser){
      this.apiService.updateUser(user).then(user =>{
        if(showToast) {
          this.translateService.get(['UPDATE_USER.UPDATED', 'UPDATE_USER.UNDO']).subscribe(translated => {
            let toast = this.toastCtrl.create({
              message: user.firstName + ' ' + user.lastName + ' ' + translated['UPDATE_USER.UPDATED'],
              duration: 5000,
              showCloseButton: true,
              closeButtonText: translated['UPDATE_USER.UNDO'],
              position: 'bottom right'
            });
            toast.onDidDismiss((data, role) => {
              if (role == "close") {
                this.save(this.oldUser, false);
              }
            });
            toast.present();
          });
        }
        this.dismiss();
        this.events.publish('users:changed');
      });
    }else {
      this.apiService.createUser(this.user).then(user => {
        this.translateService.get(['UPDATE_USER.CREATED']).subscribe(translated => {
          this.toastCtrl.create({
            message: user.firstName + ' ' + user.lastName + ' ' + translated['UPDATE_USER.CREATED'],
            duration: 5000,
            position: 'bottom right'
          }).present();
        });
        this.dismiss();
        this.events.publish('users:changed');
      });
    }
  }

  delete(){
    let alert = this.alertCtrl.create({
      message: 'Are you sure you want to delete this user?',
      buttons: [
        {
          text: 'Cancel',
          role: 'cancel'
        },
        {
          text: 'Delete',
          handler: () => {
            this.apiService.deleteUser(this.user.id).then(() =>{
              this.translateService.get(['UPDATE_USER.DELETED']).subscribe(translated => {
                this.toastCtrl.create({
                  message: this.user.firstName + ' ' + this.user.lastName + ' ' + translated['UPDATE_USER.DELETED'],
                  duration: 5000,
                  position: 'bottom right'
                }).present();
              });
              this.dismiss();
              this.events.publish('users:changed');
            });
          }
        }
      ]
    });
    alert.present();
  }
}

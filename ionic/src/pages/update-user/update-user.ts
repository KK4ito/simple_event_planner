import { Component } from '@angular/core';
import {NavParams, ViewController, ToastController, Events, AlertController} from 'ionic-angular';
import {User} from "../../models/User";
import {ApiService} from "../../providers/api.service";
import {TranslateService} from "@ngx-translate/core";
import {File} from "../../models/File";
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'page-update-user',
  templateUrl: 'update-user.html'
})
export class UpdateUserPage {

  /**
   * Store the current user
   */
  user = new User();

  /**
   * The old user info
   */
  oldUser: User;

  /**
   * Store the form control
   */
  userForm: FormGroup;

  constructor(private params: NavParams, private viewCtrl: ViewController,private apiService:ApiService, private toastCtrl:ToastController, private translateService: TranslateService, public events: Events, private alertCtrl: AlertController, public formBuilder: FormBuilder) {
    let user = params.get('user');
    if(user) {
      this.user = user;
      this.oldUser = Object.assign({}, this.user);
    }

    // define the form and set up validators
    this.userForm = formBuilder.group({
      firstName: ['', Validators.compose([Validators.required, Validators.minLength(1), Validators.maxLength(1024)])],
      lastName: ['', Validators.compose([Validators.required, Validators.minLength(1), Validators.maxLength(1024)])],
      email: ['', Validators.compose([Validators.required, Validators.minLength(5), Validators.maxLength(1024), Validators.email])],
      password: ['', Validators.compose([Validators.required])],
    });
  }

  /**
   * Dismiss/Close the current page
   */
  dismiss() {
    this.viewCtrl.dismiss();
  }

  /**
   * Save the current user by sending it to the API
   * @param user
   * @param showToast
   */
  save(user: User, showToast = true) {
    Object.assign(user, this.userForm.value);
    console.log(user);
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
    } else {
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

  /**
   * Delete the current user from the database
   */
  delete() {
    this.translateService.get(['UPDATE_USER.CONFIRM_DELETE', 'UPDATE_USER.CANCEL', 'UPDATE_USER.DELETE']).subscribe(translated => {

      let alert = this.alertCtrl.create({
        message: translated[0],
        buttons: [
          {
            text: translated[1],
            role: 'cancel'
          },
          {
            text: translated[2],
            handler: () => {
              this.apiService.deleteUser(this.user.id).then(() => {
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
    });
  }

  /**
   * Update avatar
   *
   * @param file
   */
  avatarUpdated(file: File) {
    this.user.image = file.uri;
  }
}

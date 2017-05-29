import {Component} from '@angular/core';
import {NavParams, ViewController, Events, AlertController} from 'ionic-angular';
import {User} from "../../models/User";
import {ApiService} from "../../providers/api.service";
import {TranslateService} from "@ngx-translate/core";
import {File} from "../../models/File";
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {TranslatedSnackbarService} from "../../providers/translated-snackbar.service";
import {AuthService} from "../../providers/auth.service";

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

  constructor(private params: NavParams, private viewCtrl: ViewController, private apiService: ApiService, private authService: AuthService, private translatedSnackbarService: TranslatedSnackbarService, private translateService: TranslateService, public events: Events, private alertCtrl: AlertController, public formBuilder: FormBuilder) {
    let user = params.get('user');

    if (user) {
      this.user = user;
      this.oldUser = Object.assign({}, this.user);
    }

    // define the form and set up validators
    this.userForm = formBuilder.group({
      firstName: [this.user.firstName, Validators.compose([Validators.required, Validators.minLength(1), Validators.maxLength(1024)])],
      lastName: [this.user.lastName, Validators.compose([Validators.required, Validators.minLength(1), Validators.maxLength(1024)])],
      email: [this.user.email, Validators.compose([Validators.required, Validators.minLength(5), Validators.maxLength(1024), Validators.email])]
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
    if (this.oldUser) {
      this.apiService.updateUserPartial(this.user.id, this.userForm.value).then(user => {
        if (showToast) {
          this.translatedSnackbarService.showSnackbar('USER_UPDATED', 'UNDO', {
            firstName: user.firstName,
            lastName: user.lastName
          }).then(() => {
            this.save(this.oldUser, false);
          });
        }
        this.dismiss();
        this.events.publish('users:changed');
      });
    } else {
      this.apiService.createUser(this.userForm.value).then(user => {
        this.authService.requestPasswordReset(user.email, false).then(() => {
          this.translatedSnackbarService.showSnackbar('USER_CREATED', null, {
            firstName: user.firstName,
            lastName: user.lastName
          });
          this.dismiss();
          this.events.publish('users:changed');
        });
      });
    }
  }

  /**
   * Delete the current user from the database
   */
  delete() {
    this.translateService.get(['CONFIRM_USER_DELETE', 'CANCEL', 'DELETE']).subscribe(translated => {
      let alert = this.alertCtrl.create({
        message: translated['CONFIRM_USER_DELETE'],
        buttons: [
          {
            text: translated['CANCEL'],
            role: 'cancel'
          },
          {
            text: translated['DELETE'],
            handler: () => {
              this.apiService.deleteUser(this.user.id).then(() => {
                this.translatedSnackbarService.showSnackbar('USER_DELETED', 'UNDO', {
                  firstName: this.user.firstName,
                  lastName: this.user.lastName
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
   * Callback after avatar has been updated
   *
   * @param file
   */
  avatarUpdated(file: File) {
    this.user.image = file.uri;
  }
}

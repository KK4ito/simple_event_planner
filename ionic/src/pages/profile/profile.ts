import { Component } from '@angular/core';
import {NavController, NavParams, ToastController} from 'ionic-angular';
import {User} from "../../models/User";
import {RoleType} from "../../models/RoleType";
import {AuthService} from "../../providers/auth.service";
import {ApiService} from "../../providers/api.service";
import {HomePage} from "../home/home";
import {TranslateService} from "@ngx-translate/core";
declare var window:any;

@Component({
  selector: 'page-profile',
  templateUrl: 'profile.html'
})
export class ProfilePage {

  // Static binding workaround (http://stackoverflow.com/questions/39193538/how-to-bind-static-variable-of-component-in-html-in-angular-2)
  public RoleType = RoleType;

  private user = new User();
  private receiveEmails: boolean;

  private passwordResetToken: string;
  private password: string;
  private passwordConfirm: string;

  private email: string;

  constructor(public navCtrl: NavController, public navParams: NavParams, public authService: AuthService, public apiService: ApiService, private toastCtrl: ToastController, private translateService: TranslateService) {
    this.user = authService.getUser();

    this.passwordResetToken = this.navParams.get('resetToken');

    if(this.user == null) {
      this.user = new User();
    } else {
      this.receiveEmails = !this.user.optOut;
      console.log(this.user);
    }
  }

  /**
   * Try to log the user in and redirect to the HomePage if successful, show toast if unsuccessful.
   */
  login() {
    this.authService.login(this.user).then((user) => {
      this.user = user;
      this.receiveEmails = !this.user.optOut;
      this.navCtrl.setRoot(HomePage);
    }).catch(() =>{
      this.user.password = "";
      this.translateService.get('PROFILE.LOGIN_FAILED').subscribe(translated => {
        this.toastCtrl.create({
          message: translated,
          duration: 3000,
          position: 'bottom'
        }).present();
      });
    });
  }

  /**
   * Log the user out
   */
  logout() {
    this.authService.logout();
    this.user = new User();
  }

  /**
   * Fires every time the user toggles "Receive Notifications"
   */
  toggleEmails() {
    this.apiService.updateUserPartial(this.user.id, { optOut: !this.receiveEmails }).then(user => {
      this.user = user;
      console.log(user);
    }).catch(err => {
      console.log(err);
    });
  }

  resetPassword() {
    if (this.passwordConfirm === this.password) {
      this.authService.resetPassword(this.passwordResetToken, this.password).then(res => {
        console.log(res);
      }).catch(err => console.log(err));
    } else {
      this.toastCtrl.create({
        message: 'Passwords do not match!',
        duration: 3000,
        position: 'bottom'
      }).present();

    }
  }

  requestPasswordReset() {
    this.authService.requestPasswordReset(this.email).then(res => {
      console.log(res);
    }).catch(err => console.log(err));
  }

  loginWithSibboleth() {
    window.location.href = 'https://www.cs.technik.fhnw.ch/wodss17-5/#/profile';
  }
}

import { Component } from '@angular/core';
import {NavController, NavParams, Events} from 'ionic-angular';
import {User} from "../../models/User";
import {RoleType} from "../../models/RoleType";
import {AuthService} from "../../providers/auth.service";
import {ApiService} from "../../providers/api.service";
import {HomePage} from "../home/home";
import {TranslatedSnackbarService} from "../../providers/translated-snackbar.service";
import {File} from "../../models/File";
import {PasswordResetPage} from "../password-reset/password-reset";

declare var window:any;

@Component({
  selector: 'page-profile',
  templateUrl: 'profile.html'
})
export class ProfilePage {

  // Static binding workaround (http://stackoverflow.com/questions/39193538/how-to-bind-static-variable-of-component-in-html-in-angular-2)
  public RoleType = RoleType;

  private user;
  private receiveEmails: boolean;

  constructor(public navCtrl: NavController, public authService: AuthService, public apiService: ApiService, private translatedSnackbarService: TranslatedSnackbarService, private events:Events) {

    this.events.subscribe('user:changed', (user:User) =>{
      if(user){
        this.user = user;
      }
    });

    this.user = authService.getUser();


    if(this.user == null) {
      this.user = new User();
    } else {
      this.receiveEmails = !this.user.optOut;
    }
  }

  /**
   * Try to log the user in and redirect to the HomePage if successful, show toast if unsuccessful.
   */
  login() {
    let user = this.user;
    this.authService.login(this.user).then((user) => {
      user = user;
      this.receiveEmails = !this.user.optOut;
      this.navCtrl.setRoot(HomePage);
    }).catch(() =>{
      user.password = "";
      this.translatedSnackbarService.showSnackbar('LOGIN_FAILED');
    });
  }

  /**
   * Log the user out
   */
  logout() {
    this.authService.logout();
  }

  /**
   * Fires every time the user toggles "Receive Notifications"
   */
  toggleEmails() {
    this.apiService.updateUserPartial(this.user.id, { optOut: !this.receiveEmails }).then(user => {
      this.user = user;
    }).catch(err => {
      console.log(err);
    });
  }

  loginWithSibboleth() {
    window.location.href = 'https://www.cs.technik.fhnw.ch/wodss17-5-aai/#/profile';
  }

  requestPasswordReset() {
    this.navCtrl.push(PasswordResetPage);
  }

  avatarUpdated(file: File) {
    let user = this.user;
    user.image = file.uri
    this.apiService.updateUserPartial(user.id, {image: user.image}).then(() =>{
      this.authService.login();
      this.translatedSnackbarService.showSnackbar('USER_UPDATED', null, {firstName: user.firstName, lastName: user.lastName})
    });
  }
}

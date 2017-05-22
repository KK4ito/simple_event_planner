import { Component } from '@angular/core';
import {NavController, ToastController} from 'ionic-angular';
import {User} from "../../models/User";
import {RoleType} from "../../models/RoleType";
import {AuthService} from "../../providers/auth.service";
import {ApiService} from "../../providers/api.service";
import {HomePage} from "../home/home";
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'page-profile',
  templateUrl: 'profile.html'
})
export class ProfilePage {

  // Static binding workaround (http://stackoverflow.com/questions/39193538/how-to-bind-static-variable-of-component-in-html-in-angular-2)
  public RoleType = RoleType;

  private user = new User();

  constructor(public navCtrl: NavController, public authService: AuthService, public apiService: ApiService, private toastCtrl: ToastController, private translateService: TranslateService) {
    this.user = authService.getUser();

    console.log(this.user);
    if(this.user == null) this.user = new User();
  }

  /**
   * Try to log the user in and redirect to the HomePage if successful, show toast if unsuccessful.
   */
  login() {
    this.authService.login(this.user).then((user) => {
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

}

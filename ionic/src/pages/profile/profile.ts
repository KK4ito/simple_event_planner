import { Component } from '@angular/core';
import {NavController, ToastController} from 'ionic-angular';
import {User} from "../../models/User";
import {RoleType} from "../../models/RoleType";
import {AuthService} from "../../providers/auth.service";
import {ApiService} from "../../providers/api.service";
import {HomePage} from "../home/home";

/*
  Generated class for the Login page.

  See http://ionicframework.com/docs/v2/components/#navigation for more info on
  Ionic pages and navigation.
*/
@Component({
  selector: 'page-profile',
  templateUrl: 'profile.html'
})
export class ProfilePage {

  // Static binding workaround (http://stackoverflow.com/questions/39193538/how-to-bind-static-variable-of-component-in-html-in-angular-2)
  public RoleType = RoleType;

  private user = new User();

  constructor(public navCtrl: NavController,public authService:AuthService, public apiService:ApiService, private toastCtrl:ToastController) {
    this.user = authService.getUser();

    if(this.user == null) this.user = new User();
  }

  login(){
    this.authService.login(this.user).then((user) => {
      this.navCtrl.setRoot(HomePage);
    }).catch(() =>{
      this.authService.logout();
      this.user.password = "";
      this.toastCtrl.create({
        message: 'Login failed',
        duration: 3000,
        position: 'bottom'
      }).present();
    });
  }

  logout(){
    this.authService.logout();
    this.user = new User();
  }

}

import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';
import {User} from "../../models/User";
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

  private user = new User();

  constructor(public navCtrl: NavController,public authService:AuthService, private apiService:ApiService) {

  }

  login(){
    this.authService.login(this.user).then((user) => {
      this.navCtrl.push(HomePage);
    }).catch(() =>{
      this.authService.logout();
      this.user.password = "";
    });
  }

  logout(){
    this.authService.logout();
    this.user = new User();
  }

}

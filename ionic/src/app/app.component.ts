import { Component, ViewChild } from '@angular/core';
import {Nav, Platform, Events} from 'ionic-angular';
import { StatusBar, Splashscreen } from 'ionic-native';
import { TranslateService } from "@ngx-translate/core";

import { HomePage } from "../pages/home/home";
import { AboutPage } from "../pages/about/about";
import {UsersPage} from "../pages/users/users";
import {PermissionsPage} from "../pages/permissions/permissions";
import {ProfilePage} from "../pages/login/profile";
import {User} from "../models/User";
import {AuthService} from "../providers/auth.service";


@Component({
  templateUrl: 'app.html'
})
export class MyApp {
  @ViewChild(Nav) nav: Nav;

  rootPage: any = HomePage;

  pages: Array<{title: string, component: any}>;
  public user: User;

  constructor(public platform: Platform, private translate: TranslateService, private events: Events, private authService:AuthService) {
    this.initializeApp();

    // this language will be used as a fallback when a translation isn't found in the current language
    translate.setDefaultLang('de');

    // the lang to use, if the lang isn't available, it will use the current loader to get them
    translate.use('de');


    let self = this;
    events.subscribe('user:changed', (user:User) => {
      self.user = user;
    });
    this.user = authService.user;
    console.log(this.user);

    // used for an example of ngFor and navigation
    this.pages = [
      { title: 'NAV.EVENTS', component: HomePage },
      { title: 'NAV.PERMISSIONS', component: PermissionsPage },
      { title: 'NAV.USERS', component: UsersPage },
      { title: 'NAV.LOGIN', component: ProfilePage },
      { title: 'NAV.ABOUT', component: AboutPage }
    ];

  }

  initializeApp() {
    this.platform.ready().then(() => {
      // Okay, so the platform is ready and our plugins are available.
      // Here you can do any higher level native things you might need.
      StatusBar.styleDefault();
      Splashscreen.hide();
    });
  }

  openPage(page) {
    // Reset the content nav to have just this page
    // we wouldn't want the back button to show in this scenario
    this.nav.setRoot(page.component);
  }

  changeLanguage(language) {
    this.translate.use(language);
  }
}

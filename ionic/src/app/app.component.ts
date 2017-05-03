import {Component, ViewChild} from '@angular/core';
import {Nav, Platform, Events, MenuController} from 'ionic-angular';
import {StatusBar, Splashscreen} from 'ionic-native';
import {TranslateService} from "@ngx-translate/core";

import {HomePage} from "../pages/home/home";
import {AboutPage} from "../pages/about/about";
import {UsersPage} from "../pages/users/users";
import {PermissionsPage} from "../pages/permissions/permissions";
import {ProfilePage} from "../pages/profile/profile";
import {User} from "../models/User";
import {AuthService} from "../providers/auth.service";
import {RoleType} from "../models/RoleType";


@Component({
  templateUrl: 'app.html'
})
export class MyApp {
  @ViewChild(Nav) nav: Nav;

  public rootPage:any = HomePage;
  pages: Array<{title: string, component: any}>;
  public user: User;

  constructor(private menuCtrl: MenuController, public platform: Platform, private translate: TranslateService, private events: Events, private authService: AuthService) {
    this.initializeApp();

    // this language will be used as a fallback when a translation isn't found in the current language
    translate.setDefaultLang('de');

    // the lang to use, if the lang isn't available, it will use the current loader to get them
    translate.use('de');


    let self = this;
    events.subscribe('user:changed', (user: User) => {
      self.user = user;
      self.propagateNavigation();
    });
    this.user = authService.getUser();

    this.propagateNavigation();
  }

  private propagateNavigation() {
    if (this.authService.getRole() == RoleType.ADMINISTRATOR) {
      this.pages = [
        {title: 'NAV.EVENTS', component: HomePage},
        {title: 'NAV.PERMISSIONS', component: PermissionsPage},
        {title: 'NAV.USERS', component: UsersPage},
        {title: 'NAV.LOGOUT', component: ProfilePage}
      ];
    } else if (this.authService.getRole() == RoleType.REGISTERED) {
      this.pages = [
        {title: 'NAV.EVENTS', component: HomePage},
        {title: 'NAV.LOGOUT', component: ProfilePage}
      ];
    } else {
      this.pages = [
        {title: 'NAV.LOGIN', component: ProfilePage},
        {title: 'NAV.EVENTS', component: HomePage}
      ];
    }
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
    this.nav.setRoot(page.component);
    this.rootPage = page.component;
    this.menuCtrl.close();
  }

  openProfilePage(){
    this.nav.setRoot(ProfilePage);
    this.rootPage = ProfilePage;
    this.menuCtrl.close();
  }

  isLanguage(lang) {
    return lang == this.translate.currentLang;
  }

  changeLanguage(language) {
    this.translate.use(language);
  }
}

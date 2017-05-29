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
  /**
   * Reference to the nav element
   */
  @ViewChild(Nav) nav: Nav;

  /**
   * The root page of the app
   * @type {HomePage}
   */
  public rootPage: any = HomePage;

  /**
   * Pages that will be displayed in the sidebar
   */
  pages: Array<{title: string, component: any}>;

  /**
   * The current logged in user
   */
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

  /**
   * Set the navigation according to the user role
   */
  private propagateNavigation() {
    if (this.authService.getRole() == RoleType.ADMINISTRATOR) {
      this.pages = [
        {title: 'EVENTS', component: HomePage},
        {title: 'PERMISSIONS', component: PermissionsPage},
        {title: 'USERS', component: UsersPage},
        {title: 'LOGOUT', component: ProfilePage}
      ];
    } else if (this.authService.getRole() == RoleType.REGISTERED) {
      this.pages = [
        {title: 'EVENTS', component: HomePage},
        {title: 'LOGOUT', component: ProfilePage}
      ];
    } else {
      this.pages = [
        {title: 'LOGIN', component: ProfilePage},
        {title: 'EVENTS', component: HomePage}
      ];
    }
  }

  /**
   * Initialize the app and wait until everything is loaded.
   * This function is mainly used when deploying on a mobile phone
   * because native plugins are only available after the platform is
   * ready.
   */
  initializeApp() {
    this.platform.ready().then(() => {
      // Okay, so the platform is ready and our plugins are available.
      // Here you can do any higher level native things you might need.
      StatusBar.styleDefault();
      Splashscreen.hide();
    });
  }

  /**
   * Sets the page that is passed as the new root.
   * @param page
   */
  openPage(page) {
    this.nav.setRoot(page.component);
    this.rootPage = page.component;
    this.menuCtrl.close();
  }

  /**
   * Set ProfilePage as root
   */
  openProfilePage(){
    this.nav.setRoot(ProfilePage);
    this.rootPage = ProfilePage;
    this.menuCtrl.close();
  }

  /**
   * Check if a string is the current language
   * @param lang
   * @returns {boolean}
   */
  isLanguage(lang) {
    return lang == this.translate.currentLang;
  }

  /**
   * Set the language of the app
   * @param language
   */
  changeLanguage(language) {
    this.translate.use(language);
  }
}

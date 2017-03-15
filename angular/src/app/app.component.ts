import { Component } from '@angular/core';
import { TranslateService } from "@ngx-translate/core";
import { PushNotificationsService } from 'angular2-notifications';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  constructor(private _pushNotifications: PushNotificationsService, translate: TranslateService) {
    // this language will be used as a fallback when a translation isn't found in the current language
    translate.setDefaultLang('de');

    // the lang to use, if the lang isn't available, it will use the current loader to get them
    translate.use('de');
  }

  requestNotificationPermissions() {
    this._pushNotifications.requestPermission();
    setTimeout(() => this.sendNotification(), 5000);
  }

  sendNotification() {
    this._pushNotifications.create('FHNW', {body: 'Your event starts in 5 minutes!'}).subscribe(
      res => console.log(res),
      err => console.log(err)
    )
  }
}

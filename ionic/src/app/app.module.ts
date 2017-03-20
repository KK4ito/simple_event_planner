import { NgModule, ErrorHandler } from '@angular/core';
import { IonicApp, IonicModule, IonicErrorHandler, DeepLinkConfig } from 'ionic-angular';
import { MyApp } from './app.component';
import { Page1 } from '../pages/page1/page1';
import { Page2 } from '../pages/page2/page2';
import { DetailPage } from '../pages/detail/detail';

import { ApiService } from '../providers/api.service';
import { AuthService } from '../providers/auth.service';

import { ImageUri } from "../pipes/ImageUri";

export const deepLinkConfig: DeepLinkConfig = {
  links: [
    { component: Page1, name: "contact", segment: ""},
    { component: Page2, name: "hello", segment: "hello" },
    { component: DetailPage, name: "hello", segment: "event/:id" }
  ]
};

@NgModule({
  declarations: [
    MyApp,
    Page1,
    Page2,
    DetailPage,
    ImageUri
  ],
  imports: [
    IonicModule.forRoot(MyApp, {}, deepLinkConfig)
  ],
  bootstrap: [IonicApp],
  entryComponents: [
    MyApp,
    Page1,
    Page2,
    DetailPage
  ],
  providers: [
    AuthService,
    ApiService,
    {provide: ErrorHandler, useClass: IonicErrorHandler}
    ]
})
export class AppModule {}

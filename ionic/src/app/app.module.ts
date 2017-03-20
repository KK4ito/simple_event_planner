import { NgModule, ErrorHandler } from '@angular/core';
import { IonicApp, IonicModule, IonicErrorHandler, DeepLinkConfig } from 'ionic-angular';
import { HttpModule, Http } from '@angular/http';

import { MyApp } from './app.component';
import { Page1 } from '../pages/page1/page1';
import { Page2 } from '../pages/page2/page2';
import { DetailPage } from '../pages/detail/detail';

import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';

import { ApiService } from '../providers/api.service';
import { AuthService } from '../providers/auth.service';

import { ImageUri } from "../pipes/ImageUri";

import { NgUploaderModule } from 'ngx-uploader';
import { ProgressBarComponent } from "../components/progress-bar/progress-bar";

// AoT requires an exported function for factories
export function HttpLoaderFactory(http: Http) {
  return new TranslateHttpLoader(http);
}

export const deepLinkConfig: DeepLinkConfig = {
  links: [
    { component: Page1, name: "contact", segment: ""},
    { component: Page2, name: "hello", segment: "hello" },
    { component: DetailPage, name: "hello", segment: "event/:id", defaultHistory: [Page1] }
  ]
};

@NgModule({
  declarations: [
    MyApp,
    Page1,
    Page2,
    DetailPage,
    ImageUri,
    ProgressBarComponent
  ],
  imports: [
    IonicModule.forRoot(MyApp, {}, deepLinkConfig),
    HttpModule,
    NgUploaderModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [Http]
      }
    }),
  ],
  bootstrap: [IonicApp],
  entryComponents: [
    MyApp,
    Page1,
    Page2,
    DetailPage,
    ProgressBarComponent
  ],
  providers: [
    AuthService,
    ApiService,
    {provide: ErrorHandler, useClass: IonicErrorHandler}
    ]
})
export class AppModule {}

import { NgModule, ErrorHandler } from '@angular/core';
import { IonicApp, IonicModule, IonicErrorHandler } from 'ionic-angular';
import {HttpModule, Http} from '@angular/http';

import { MyApp } from './app.component';
import { DetailPage } from '../pages/detail/detail';
import { HomePage } from "../pages/home/home";

import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { Ng2CompleterModule } from "ng2-completer";

import { ApiService } from '../providers/api.service';
import { AuthService } from '../providers/auth.service';

import { ImageUri } from "../pipes/ImageUri";

import { NgUploaderModule } from 'ngx-uploader';
import { ProgressBarComponent } from "../components/progress-bar/progress-bar";
import { FileuploadComponent } from "../components/fileupload/fileupload";
import {UsersPage} from "../pages/users/users";
import {PermissionsPage} from "../pages/permissions/permissions";
import {SelectUserComponent} from "../components/select-user/select-user";
import {UpdateUserPage} from "../pages/update-user/update-user";
import {PictureUploadComponent} from "../components/picture-upload/picture-upload";
import {Autoresize} from "../components/autoresize/autoresize";
import {ProfilePage} from "../pages/profile/profile";

import { Md2DatepickerModule }  from '../../node_modules/md2/datepicker/index';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {BrowserModule} from "@angular/platform-browser";
import {InvitePage} from "../pages/invite/invite";
import { RecipientComponent } from '../components/recipient/recipient';
declare var tinymce: any;



// AoT requires an exported function for factories
export function HttpLoaderFactory(http: Http) {
  return new TranslateHttpLoader(http);
}

export const deepLinkConfig: any = {
  links: [
    { component: HomePage, name: "home", segment: ""},
    { component: DetailPage, name: "detail", segment: "event/:id", defaultHistory: [HomePage] }
  ]
};

@NgModule({
  declarations: [
    MyApp,
    HomePage,
    DetailPage,
    UsersPage,
    PermissionsPage,
    UpdateUserPage,
    ProfilePage,
    InvitePage,
    ImageUri,
    ProgressBarComponent,
    FileuploadComponent,
    SelectUserComponent,
    PictureUploadComponent,
    Autoresize,
    RecipientComponent
  ],
  imports: [
    BrowserModule,
    IonicModule.forRoot(MyApp, {}, deepLinkConfig),
    HttpModule,
    BrowserAnimationsModule,
    NgUploaderModule,
    Md2DatepickerModule,
    Ng2CompleterModule,
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
    HomePage,
    DetailPage,
    PermissionsPage,
    UpdateUserPage,
    UsersPage,
    ProfilePage,
    InvitePage,
    ProgressBarComponent,
    FileuploadComponent,
    SelectUserComponent,
    PictureUploadComponent
  ],
  providers: [
    AuthService,
    ApiService,
    {provide: ErrorHandler, useClass: IonicErrorHandler}
    ]
})
export class AppModule {}

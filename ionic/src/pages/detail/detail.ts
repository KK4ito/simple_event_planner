import { Component, NgZone, Inject } from '@angular/core';
import {NavController, NavParams, AlertController} from 'ionic-angular';
import { ApiService } from "../../providers/api.service";
import { Event } from '../../models/Event';
import { User } from "../../models/User";
import { File } from "../../models/File";
import { environment } from '../../../environments/environment';
import { NgUploaderOptions } from 'ngx-uploader';
import {DomSanitizer, SafeStyle, SafeUrl} from "@angular/platform-browser";
@Component({
  selector: 'page-detail',
  templateUrl: 'detail.html'
})
export class DetailPage {

  private progress: number = 0;
  private event: Event = new Event();
  private speakers: User[];
  private attendees: User[];
  private files: File[];
  private safeStyle: SafeStyle;

  constructor(@Inject(NgZone) private zone: NgZone, private _apiService: ApiService, public navParams: NavParams, private alertCtrl:AlertController, private sanitizer:DomSanitizer) {
    this._apiService.getEvent(this.navParams.get('id')).then(event => {
      this.event = event;
      this.safeStyle = sanitizer.bypassSecurityTrustStyle( 'url(\'' + environment.baseUrl + event.imageUri + '\')');
    });
    this._apiService.getSpeakers(this.navParams.get('id')).then(speakers => this.speakers = speakers);
    this._apiService.getAttendees(this.navParams.get('id')).then(attendees => this.attendees = attendees);
    this._apiService.getFiles(this.navParams.get('id')).then(files => this.files = files);

  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad DetailPage');
  }

  ionViewWillEnter() {
  }

  public download(url:string){
    window.location.href= environment.baseUrl + url;
  }

  uploadFinished(success, file: File) {
    let files = this.files.map(f => environment.baseUrl + f.uri);
    console.log('files', files);
    files.push(environment.baseUrl + file.uri);
    this._apiService.updateEvent(this.event.id, {
      files: files
    }).then(files => {
      this._apiService.getFiles(this.navParams.get('id')).then(files => this.files = files);
    });

  }

  deleteFile(ev, file: File) {
    ev.stopPropagation();
    this.alertCtrl.create({
      title: 'Delete file',
      message: 'Do you want to delete ' + file.name + '?',
      buttons: [
        {
          text: 'Cancel',
          role: 'cancel'
        },
        {
          text: 'Delete',
          handler: () => {
            let bla = this.files.filter(f => f.uri !== file.uri);
            let files = bla.map(f => environment.baseUrl + f.uri);
            this._apiService.updateEvent(this.event.id, {
              files: files
            }).then(() => {
              this._apiService.getFiles(this.navParams.get('id')).then(files => this.files = files);
            });          }
        }
      ]
    }).present();
  }
}

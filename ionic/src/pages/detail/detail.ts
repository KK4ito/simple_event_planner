import { Component, NgZone, Inject } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';
import { ApiService } from "../../providers/api.service";
import { Event } from '../../models/Event';
import { User } from "../../models/User";
import { File } from "../../models/File";
import { environment } from '../../../environments/environment';
import { NgUploaderOptions } from 'ngx-uploader';
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

  options: NgUploaderOptions;
  response: any;
  hasBaseDropZoneOver: boolean;

  constructor(@Inject(NgZone) private zone: NgZone, private _apiService: ApiService, public navParams: NavParams) {
    this._apiService.getEvent(this.navParams.get('id')).then(event => this.event = event);
    this._apiService.getSpeakers(this.navParams.get('id')).then(speakers => this.speakers = speakers);
    this._apiService.getAttendees(this.navParams.get('id')).then(attendees => this.attendees = attendees);
    this._apiService.getFiles(this.navParams.get('id')).then(files => this.files = files);

    this.options = new NgUploaderOptions({
      url: environment.baseUrl + '/api/files'
    });
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad DetailPage');
  }

  ionViewWillEnter() {
  }

  public download(url:string){
    window.location.href= environment.baseUrl + url;
  }

  handleUpload(data: any) {
    setTimeout(() => {
      this.zone.run(() => {
        this.response = data;
        console.log('data1', data);
        if (this.response.progress) {
          this.progress = this.response.progress.percent;
          console.log(this.progress);
        }
        if (data && data.response) {
          console.log('data2', data);
          this.response = JSON.parse(data.response);

          let files = this.files.map(f => environment.baseUrl + f.uri);
          console.log('files', files);
          files.push(environment.baseUrl + JSON.parse(data.response).uri);
          this._apiService.updateEvent(this.event.id, {
            files: files
          }).then(files => {
            this._apiService.getFiles(this.navParams.get('id')).then(files => this.files = files);
          });
        }
      });
    });
  }

  fileOverBase(e: boolean) {
    this.hasBaseDropZoneOver = e;
  }

  deleteFile(ev, uri: string) {
    ev.stopPropagation();
    let bla = this.files.filter(f => f.uri !== uri);
    let files = bla.map(f => environment.baseUrl + f.uri);
    this._apiService.updateEvent(this.event.id, {
      files: files
    }).then(() => {
      this._apiService.getFiles(this.navParams.get('id')).then(files => this.files = files);
    });
  }
}

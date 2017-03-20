import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';
import { ApiService } from "../../providers/api.service";
import { Event } from '../../models/Event';
import { User } from "../../models/User";
import { File } from "../../models/File";
import { environment } from '../../../environments/environment';

@Component({
  selector: 'page-detail',
  templateUrl: 'detail.html'
})
export class DetailPage {

  private event: Event = new Event();
  private speakers: User[];
  private attendees: User[];
  private files: File[];

  constructor(private _apiService: ApiService, public navParams: NavParams) {
    this._apiService.getEvent(this.navParams.get('id')).then(event => this.event = event);
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

}

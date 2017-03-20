import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from "@angular/router";
import {ApiService} from "../../providers/api.service";
import {User} from "../../models/User";
import {Event} from "../../models/Event";
import {File} from "../../models/File";
import { environment } from '../../../environments/environment';

declare var window:any;

@Component({
  selector: 'app-detail',
  templateUrl: './detail.component.html',
  styleUrls: ['./detail.component.scss']
})
export class DetailComponent implements OnInit {

  private id: number;
  private event:Event = new Event();
  private speakers:User[];
  private attendees:User[];
  private files:File[];

  constructor(private route: ActivatedRoute, private _apiService:ApiService) {
    this.route.params.subscribe((params: Params) => {
      this.id = +params['id'];
      this._apiService.getEvent(this.id).then(event => this.event = event);
      this._apiService.getSpeakers(this.id).then(speakers => this.speakers = speakers);
      this._apiService.getAttendees(this.id).then(attendees => this.attendees = attendees);
      this._apiService.getFiles(this.id).then(files => this.files = files);
    });
  }

  ngOnInit() {
  }

  public download(url:string){
    window.location.href=environment.baseUrl + url;
  }

}

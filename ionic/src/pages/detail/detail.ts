import {Component, NgZone, Inject, ChangeDetectorRef} from '@angular/core';
import {NavController, NavParams, AlertController, ToastController} from 'ionic-angular';
import {ApiService} from "../../providers/api.service";
import {Event} from '../../models/Event';
import {User} from "../../models/User";
import {File} from "../../models/File";
import {environment} from '../../../environments/environment';
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

  private editMode = false;
  private oldEvent;

  constructor(@Inject(NgZone) private zone: NgZone, private _apiService: ApiService, private toastCtrl: ToastController, public navParams: NavParams, private navCtrl: NavController, private alertCtrl: AlertController, private sanitizer: DomSanitizer, private changeDetectorRef: ChangeDetectorRef) {
    if(this.navParams.get('id') >= 0) {
      this._apiService.getEvent(this.navParams.get('id')).then(event => {
        this.event = event;
        this.safeStyle = sanitizer.bypassSecurityTrustStyle('url(\'' + environment.baseUrl + event.imageUri + '\')');
      });
      this._apiService.getSpeakers(this.navParams.get('id')).then(speakers => this.speakers = speakers);
      this._apiService.getAttendees(this.navParams.get('id')).then(attendees => this.attendees = attendees);
      this._apiService.getFiles(this.navParams.get('id')).then(files => this.files = files);
    }else {
      this.editMode = true;
    }
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad DetailPage');
  }

  ionViewWillEnter() {
  }

  public download(url: string) {
    window.location.href = environment.baseUrl + url;
  }

  uploadFinished(success, file: File) {
    let files = this.files.map(f => environment.baseUrl + f.uri);
    console.log('files', files);
    files.push(environment.baseUrl + file.uri);
    this._apiService.updateEvent({
      id: this.event.id,
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
            this._apiService.updateEvent({
              id: this.event.id,
              files: files
            }).then(() => {
              this._apiService.getFiles(this.navParams.get('id')).then(files => this.files = files);
            });
          }
        }
      ]
    }).present();
  }

  edit() {
    this.editMode = true;
    this.oldEvent = Object.assign({}, this.event);
  }

  save() {
    this.editMode = false;
    if(this.event.id >= 0) {
      this.saveEvent(this.event);
    }else{
      this.createEvent();
    }
  }

  createEvent(){
    this._apiService.createEvent(this.event).then((newEvent)=>{
      console.log('event', newEvent);
      this.navCtrl.push(DetailPage, {
        id: newEvent.id
      });
    });
  }

  saveEvent(event:Event, isRestore = false){
    // Set speaker list
    event.speakers = this.speakers.map((s) => '/api/user/' + s.id);
    this._apiService.updateEvent(event).then((event) => {
      this.event = event;
      if(isRestore) {
        this.toastCtrl.create({
          message: 'Event successfully restored.',
          duration: 3000,
          position: 'bottom right'
        }).present();
      }else{
        let toast = this.toastCtrl.create({
          message: 'Event successfully updated.',
          duration: 3000,
          showCloseButton: true,
          closeButtonText: "undo",
          position: 'bottom right'
        });
        toast.onDidDismiss((data, role) => {
          if (role == "close") {
            this.saveEvent(this.oldEvent, true);
          }
        });
        toast.present();
      }
    });
  }

  pictureChanged(file:File){
    this.safeStyle = this.sanitizer.bypassSecurityTrustStyle('url(\'' + environment.baseUrl + file.uri + '\')');
    this.event.image = file.uri;
    this.changeDetectorRef.detectChanges();
  }

  onSpeakerSelected(user:User){
    if(this.speakers.map(s => s.id).indexOf(user.id) > -1) return;
    this.speakers.push(user);
  }

  deleteSpeaker(ev, user: User){
    this.speakers = this.speakers.filter(item => item !== user);
  }
}

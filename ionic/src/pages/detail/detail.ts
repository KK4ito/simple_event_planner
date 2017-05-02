import {Component, NgZone, Inject, ChangeDetectorRef} from '@angular/core';
import {NavController, NavParams, AlertController, ToastController, AlertInputOptions} from 'ionic-angular';
import {ApiService} from "../../providers/api.service";
import {Event} from '../../models/Event';
import {User} from "../../models/User";
import {File} from "../../models/File";
import {RoleType} from "../../models/RoleType";
import {environment} from '../../../environments/environment';
import {DomSanitizer, SafeStyle} from "@angular/platform-browser";
import {EventAttendee} from "../../models/EventAttendee";
import {ProfilePage} from "../profile/profile";
import {AuthService} from "../../providers/auth.service";
@Component({
  selector: 'page-detail',
  templateUrl: 'detail.html'
})
export class DetailPage {

  private event: Event = new Event();
  private speakers: User[];
  private attendees: User[];
  private files: File[];
  private safeStyle: SafeStyle;

  private editMode = false;
  private oldEvent;

  private attendsLocked = true;
  private attends = false;
  private eventAttendee:EventAttendee;
  private menus = [{'value': 'VEGI', 'label': 'Vegi'}, {'value': 'NORMAL', 'label': 'Normal'}, {
    'value': 'NONE',
    'label': 'None'
  }];

  // Static binding workaround (http://stackoverflow.com/questions/39193538/how-to-bind-static-variable-of-component-in-html-in-angular-2)
  public RoleType = RoleType;

  constructor(@Inject(NgZone) private zone: NgZone, private _apiService: ApiService, private toastCtrl: ToastController, public navParams: NavParams, private navCtrl: NavController, private alertCtrl: AlertController, private sanitizer: DomSanitizer, private changeDetectorRef: ChangeDetectorRef, public authService: AuthService) {
    if (this.navParams.get('id') >= 0) {
      let self = this;
      this._apiService.getEvent(this.navParams.get('id')).then(event => {
        this.event = event;
        this.safeStyle = sanitizer.bypassSecurityTrustStyle('url(\'' + environment.baseUrl + event.imageUri + '\')');
        // TODO Provide logged in user id instead of hardcoded one
        this._apiService.getAttends(1, this.event.id)
          .then((eventAttendee) => {
            self.eventAttendee = eventAttendee;
            self.attends = true;
            // Needed because of initialization delay
            setTimeout(() => self.attendsLocked = false, 1000);
          })
          .catch(() => {
            self.attends = false;
            // Needed because of initialization delay
            setTimeout(() => self.attendsLocked = false, 1000);
          });

      });
      this._apiService.getSpeakers(this.navParams.get('id')).then(speakers => this.speakers = speakers);
      this._apiService.getAttendees(this.navParams.get('id')).then(attendees => this.attendees = attendees);
      this._apiService.getFiles(this.navParams.get('id')).then(files => {
        this.files = files;
      });
    } else {
      this.editMode = true;
    }
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
    if (this.event.id >= 0) {
      this.saveEvent(this.event);
    } else {
      this.createEvent();
    }
  }

  createEvent() {
    this._apiService.createEvent(this.event).then((newEvent) => {
      console.log('event', newEvent);
      this.navCtrl.push(DetailPage, {
        id: newEvent.id
      });
    });
  }

  saveEvent(event: Event, isRestore = false) {
    // Set speaker list
    event.speakers = this.speakers.map((s) => '/api/user/' + s.id);
    this._apiService.updateEvent(event).then((event) => {
      this.event = event;
      if (isRestore) {
        this.toastCtrl.create({
          message: 'Event successfully restored.',
          duration: 3000,
          position: 'bottom right'
        }).present();
      } else {
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

  pictureChanged(file: File) {
    this.safeStyle = this.sanitizer.bypassSecurityTrustStyle('url(\'' + environment.baseUrl + file.uri + '\')');
    this.event.image = file.uri;
    this.changeDetectorRef.detectChanges();
  }

  onSpeakerSelected(user: User) {
    if (this.speakers.map(s => s.id).indexOf(user.id) > -1) return;
    this.speakers.push(user);
  }

  deleteSpeaker(ev, user: User) {
    this.speakers = this.speakers.filter(item => item !== user);
  }

  openProfilePage() {
    this.navCtrl.setRoot(ProfilePage);
  }


  attendEvent() {
    if (!this.attendsLocked) {
      this.attendsLocked = true;
      if (!this.attends) {
        this._apiService.deleteEventAttendee(this.eventAttendee.id).then(()=> {
          this._apiService.getAttendees(this.navParams.get('id')).then(attendees => this.attendees = attendees);
          this.eventAttendee = undefined;
          this.attends = false;
          this.attendsLocked = false;
        }).catch(()=> this.attendsLocked = false);
      } else {
        let alert = this.alertCtrl.create({
          title: 'Select your menu',
          enableBackdropDismiss: false
        });
        for (var i = 0; i < this.menus.length; i++) {
          alert.addInput(<AlertInputOptions>{
            type: 'radio',
            value: this.menus[i].value,
            label: this.menus[i].label,
            checked: false
          });
        }
        alert.addButton({
          text: 'Cancel',
          handler: () => {
            this.attends = false;
            setTimeout(() => this.attendsLocked = false, 1000);
          }
        });
        alert.addButton({
          text: 'Ok',
          handler: data => {
            var eventAttendee = new EventAttendee();
            eventAttendee.event = '/event/' + this.event.id;
            // TODO use logged in user id
            eventAttendee.user = '/user/' + 1;
            eventAttendee.foodType = data;
            this._apiService.createEventAttendee(eventAttendee).then((result) => {
              this._apiService.getAttendees(this.navParams.get('id')).then(attendees => this.attendees = attendees);
              this.eventAttendee = result;
              this.attends = true;
              this.attendsLocked = false;
            }).catch(() => this.attendsLocked = false);
          }
        });
        alert.present();
      }
    }
  }
}

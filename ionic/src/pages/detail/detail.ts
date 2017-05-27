import {Component, ChangeDetectorRef} from '@angular/core';
import {
  NavController, NavParams, AlertController,
  ModalController, FabContainer
} from 'ionic-angular';
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
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {InvitePage} from "../invite/invite";
import {DetailPrintPage} from "../detail-print/detail-print";
import {SelectFoodPage} from "../select-food/select-food";
import {FoodType} from "../../models/FoodType";
import {TranslatedSnackbarService} from "../../providers/translated-snackbar.service";

@Component({
  selector: 'page-detail',
  templateUrl: 'detail.html'
})
export class DetailPage {

  private event: Event = new Event();
  private speakers: User[] = [];
  private attendees: User[] = [];
  private files: File[] = [];
  private safeStyle: SafeStyle;

  private editMode = false;
  private isSpeaker = false;
  private oldEvent;

  private attendsLocked = true;
  private attends = false;
  private eventAttendee:EventAttendee;

  private eventDuration = 0;

  // Static binding workaround (http://stackoverflow.com/questions/39193538/how-to-bind-static-variable-of-component-in-html-in-angular-2)
  public RoleType = RoleType;

  public eventForm: FormGroup;

  constructor(private _apiService: ApiService, private modalCtrl: ModalController, public navParams: NavParams, private translatedSnackbarService: TranslatedSnackbarService,  private navCtrl: NavController, private alertCtrl: AlertController, private sanitizer: DomSanitizer, private changeDetectorRef: ChangeDetectorRef, public authService: AuthService, public formBuilder: FormBuilder) {
    this.eventForm = formBuilder.group({
      name: ['', Validators.compose([Validators.required])],
      description: ['', Validators.compose([Validators.required])],
      location: ['', Validators.compose([Validators.required])],
      startTime: ['', Validators.compose([Validators.required])],
      closingTime: ['', Validators.compose([Validators.required])],
      endTime: ['', Validators.compose([Validators.required])],
    });

    if (this.navParams.get('id') >= 0) {
      let self = this;
      this._apiService.getEvent(this.navParams.get('id')).then(event => {
        this.event = event;
        this.eventDuration = (new Date(event.endTime).getTime() - new Date(event.startTime).getTime()) / 1000;

        this.eventForm.controls['name'].setValue(event.name);
        this.eventForm.controls['description'].setValue(event.description);
        this.eventForm.controls['location'].setValue(event.location);
        this.eventForm.controls['startTime'].setValue(event.startTime);
        this.eventForm.controls['closingTime'].setValue(event.closingTime);
        this.eventForm.controls['endTime'].setValue(event.endTime);
        this.safeStyle = sanitizer.bypassSecurityTrustStyle('url(\'' + environment.baseUrl + event.imageUri + '\')');
        var user = authService.getUser();
        if(user) {
          this._apiService.getAttends(user.id, this.event.id)
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
        }
      });
      this._apiService.getSpeakers(this.navParams.get('id')).then(speakers => {
        this.speakers = speakers;
        if(this.authService.getRole() > RoleType.ANONYMOUS) {
          this.isSpeaker = this.speakers.filter(speaker => {
              return speaker.email == this.authService.getUser().email;
            }).length > 0;
        }
      });
      this._apiService.getAttendees(this.navParams.get('id')).then(attendees => this.attendees = attendees);
      this._apiService.getFiles(this.navParams.get('id')).then(files => {
        this.files = files;
      });
    } else {
      this.editMode = true;
    }
  }

  saveForm() {
    console.log(this.eventForm.value);
  }

  download(url: string) {
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

  /**
   * Show the invite modal to send out invites
   */
  sendInvite() {
    this.modalCtrl.create(InvitePage, { event: this.event }).present();
  }

  edit(fab: FabContainer) {
    if(fab) fab.close();
    this.editMode = true;
    this.oldEvent = Object.assign({}, this.event);
  }

  save() {
    Object.assign(this.event, this.eventForm.value);
    this.editMode = false;
    if (this.event.id >= 0) {
      this.saveEvent(this.event);
    } else {
      this.createEvent();
    }
  }

  createEvent() {
    this._apiService.createEvent(this.event).then((newEvent) => {
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
        this.translatedSnackbarService.showSnackbar('EVENT_RESTORED');
      } else {
        this.translatedSnackbarService.showSnackbar('EVENT_UPDATED', 'UNDO').then(() =>{
          this.saveEvent(this.oldEvent, true);
        });
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
      if (this.attends) {
        this._apiService.deleteEventAttendee(this.eventAttendee.id).then(()=> {
          this._apiService.getAttendees(this.navParams.get('id')).then(attendees => this.attendees = attendees);
          this.eventAttendee = undefined;
          this.attends = false;
          this.attendsLocked = false;
        }).catch(()=> this.attendsLocked = false);
      } else {
        let modal = this.modalCtrl.create(SelectFoodPage);
        modal.onDidDismiss((data) => {
          let eventAttendee = new EventAttendee();
          eventAttendee.event = '/events/' + this.event.id;
          let user = this.authService.getUser();
          eventAttendee.user = '/users/' + user.id;
          if(data) {
            eventAttendee.foodType = data.selectedFood;
            eventAttendee.drink = data.drink;
          }else{
            eventAttendee.foodType = FoodType.NONE;
            eventAttendee.drink = false;
          }
          this._apiService.createEventAttendee(eventAttendee).then((result) => {
            this._apiService.getAttendees(this.navParams.get('id')).then(attendees => this.attendees = attendees);
            this.eventAttendee = result;
            this.attends = true;
            this.attendsLocked = false;
          }).catch(() => this.attendsLocked = false);

        });
        modal.present();
      }
    }
  }

  isClosed(){
    return Date.parse(this.event.closingTime) < new Date().getTime();
  }

  /**
   * Open print page
   */
  print() {
    this.navCtrl.push(DetailPrintPage, {
      id: this.navParams.get('id')
    });
  }
}

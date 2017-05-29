import { Component, ChangeDetectorRef } from '@angular/core';
import {
  NavController, NavParams, AlertController,
  ModalController, FabContainer
} from 'ionic-angular';
import { ApiService } from "../../providers/api.service";
import { Event } from '../../models/Event';
import { User } from "../../models/User";
import { File } from "../../models/File";
import { RoleType } from "../../models/RoleType";
import { environment } from '../../../environments/environment';
import { DomSanitizer, SafeStyle } from "@angular/platform-browser";
import { EventAttendee } from "../../models/EventAttendee";
import { ProfilePage } from "../profile/profile";
import { AuthService } from "../../providers/auth.service";
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { InvitePage } from "../invite/invite";
import { DetailPrintPage } from "../detail-print/detail-print";
import { SelectFoodPage } from "../select-food/select-food";
import { FoodType } from "../../models/FoodType";
import { TranslatedSnackbarService } from "../../providers/translated-snackbar.service";
import { TranslateService } from "@ngx-translate/core";

@Component({
  selector: 'page-detail',
  templateUrl: 'detail.html'
})
export class DetailPage {

  /**
   * Event of the current page
   * @type {Event}
   */
  public event: Event = new Event();

  /**
   * Array of speakers
   * @type {Array}
   */
  public speakers: User[] = [];

  /**
   * Array of attendees
   * @type {Array}
   */
  public attendees: User[] = [];

  /**
   * Array of files
   * @type {Array}
   */
  public files: File[] = [];

  /**
   * Variable to hold styles to be injected at runtime
   */
  public safeStyle: SafeStyle;

  /**
   * Indicates whether edit mode is on or not
   * @type {boolean}
   */
  public editMode = false;

  /**
   * Indicates whether the current user is a speaker or not
   * @type {boolean}
   */
  public isSpeaker = false;

  /**
   * Copy of the event. In case of undoing, we will set overwrite the "new" event with the "old" one.
   */
  private oldEvent;

  /**
   * Lock the UI until we have a response from the server
   * @type {boolean}
   */
  private attendsLocked = true;

  /**
   * Indicate whether the current user attends the event
   * @type {boolean}
   */
  public attends = false;

  /**
   * The event Attendee object
   */
  public eventAttendee: EventAttendee;

  /**
   * The duration of the event
   * @type {number}
   */
  public eventDuration: number = 0;

  // Static binding workaround (http://stackoverflow.com/questions/39193538/how-to-bind-static-variable-of-component-in-html-in-angular-2)
  /**
   * The role of the current user
   * @type {RoleType}
   */
  public RoleType = RoleType;

  /**
   * Angular form for the event
   */
  public eventForm: FormGroup;

  /**
   * Allowed types for fileupload
   * @type {string[]}
   */
  public allowedUploadExtensions: string[] = ['ppt', 'pptx', 'doc', 'docx', 'xls', 'xlsx', 'zip', 'txt', 'pdf', 'jpg', 'jpeg', 'png'];

  constructor(private _apiService: ApiService, private modalCtrl: ModalController, public navParams: NavParams, private translatedSnackbarService: TranslatedSnackbarService, private translateService: TranslateService, private navCtrl: NavController, private alertCtrl: AlertController, private sanitizer: DomSanitizer, private changeDetectorRef: ChangeDetectorRef, public authService: AuthService, public formBuilder: FormBuilder) {
    this.eventForm = formBuilder.group({
      name: ['', Validators.compose([Validators.required, Validators.minLength(3), Validators.maxLength(1024)])],
      description: ['', Validators.compose([Validators.required, Validators.maxLength(65535)])],
      location: ['', Validators.compose([Validators.required, Validators.minLength(3), Validators.maxLength(1024)])],
      startTime: ['', Validators.compose([Validators.required])],
      closingTime: ['', Validators.compose([Validators.required])],
      endTime: ['', Validators.compose([Validators.required])],
    }, { validator: this.validDates });

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
        if (user) {
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
        if (this.authService.getRole() > RoleType.ANONYMOUS) {
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

  /**
   * Validator to validate the different times in our form
   * E.g. Start time has to be lower than closing time etc.
   * @param group
   * @returns {null}
   */
  validDates(group: FormGroup) {
    let invalid = false;

    if (group.controls['startTime'].value < group.controls['closingTime'].value) {
      invalid = true;
    }

    if (group.controls['startTime'].value >= group.controls['endTime'].value) {
      invalid = true;
    }

    if (invalid) {
      group.controls['startTime'].setErrors({isValidDate: false});
    } else {
      console.log(group);
      group.controls['startTime'].setErrors(null);
    }

    return null;
  }

  /**
   * Download object
   * @param url
   */
  download(url: string) {
    window.location.href = environment.baseUrl + url;
  }

  /**
   * Success callback after file upload
   * @param success
   * @param file
   */
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

  /**
   * Delete file
   * @param ev
   * @param file
   */
  deleteFile(ev, file: File) {
    ev.stopPropagation();
    this.translateService.get(['DELETE_FILE', 'DELETE_FILE_CONFIRM', 'CANCEL', 'DELETE'], { filename: file.name }).subscribe(translated => {
      this.alertCtrl.create({
        title: translated['DELETE_FILE'],
        message: translated['DELETE_FILE_CONFIRM'],
        buttons: [
          {
            text: translated['CANCEL'],
            role: 'cancel'
          },
          {
            text: translated['DELETE'],
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

    });
  }

  /**
   * Show the invite modal to send out invites
   */
  sendInvite() {
    this.modalCtrl.create(InvitePage, { event: this.event }).present();
  }

  /**
   * Open edit fab
   * @param fab
   */
  edit(fab: FabContainer) {
    if (fab) fab.close();
    this.editMode = true;
    this.oldEvent = Object.assign({}, this.event);
  }

  /**
   * Save the current form (send it to the server)
   */
  save() {
    Object.assign(this.event, this.eventForm.value);
    this.editMode = false;
    if (this.event.id >= 0) {
      this.saveEvent(this.event);
    } else {
      this.createEvent();
    }
  }

  /**
   * Create a new event
   */
  createEvent() {
    let preparedEvent = this.event;
    preparedEvent.speakers = this.speakers.map((s) => '/api/user/' + s.id);
    this._apiService.createEvent(preparedEvent).then((newEvent) => {
      this.navCtrl.setRoot(DetailPage, {
        id: newEvent.id
      });
    });
  }

  /**
   * Save event
   * @param event
   * @param isRestore
   */
  saveEvent(event: Event, isRestore = false) {
    // Set speaker list
    event.speakers = this.speakers.map((s) => '/api/user/' + s.id);
    this._apiService.updateEvent(event).then((event) => {
      this.event = event;
      if (isRestore) {
        this.translatedSnackbarService.showSnackbar('EVENT_RESTORED');
      } else {
        this.translatedSnackbarService.showSnackbar('EVENT_UPDATED', 'UNDO').then(() => {
          this.saveEvent(this.oldEvent, true);
        });
      }
    });
  }

  /**
   * Callback when the picture is uploaded
   * @param file
   */
  pictureChanged(file: File) {
    this.safeStyle = this.sanitizer.bypassSecurityTrustStyle('url(\'' + environment.baseUrl + file.uri + '\')');
    this.event.image = file.uri;
    this.changeDetectorRef.detectChanges();
  }

  /**
   * Callback when a speaker has been selected
   * @param user
   */
  onSpeakerSelected(user: User) {
    if (this.speakers.map(s => s.id).indexOf(user.id) > -1) return;
    this.speakers.push(user);
  }

  /**
   * Delete a speaker
   * @param ev
   * @param user
   */
  deleteSpeaker(ev, user: User) {
    this.speakers = this.speakers.filter(item => item !== user);
  }

  /**
   * Open the profile page
   */
  openProfilePage() {
    this.navCtrl.setRoot(ProfilePage);
  }

  /**
   * Current user attends the current event
   */
  attendEvent() {
    if (!this.attendsLocked) {
      this.attendsLocked = true;
      if (this.attends) {
        this._apiService.deleteEventAttendee(this.eventAttendee.id).then(() => {
          this._apiService.getAttendees(this.navParams.get('id')).then(attendees => this.attendees = attendees);
          this.eventAttendee = undefined;
          this.attends = false;
          this.attendsLocked = false;
        }).catch(() => this.attendsLocked = false);
      } else {
        let modal = this.modalCtrl.create(SelectFoodPage);
        modal.onDidDismiss((data) => {
          let eventAttendee = new EventAttendee();
          eventAttendee.event = '/events/' + this.event.id;
          let user = this.authService.getUser();
          eventAttendee.user = '/users/' + user.id;
          if (data) {
            eventAttendee.foodType = data.selectedFood;
            eventAttendee.drink = data.drink;
          } else {
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

  /**
   * Check if the provided file is an image
   * @param file
   * @returns {boolean}
   */
  isImageFile(file) {
    if (file && file.file) {
      let splits = file.file.split('.');
      let extension = splits[splits.length - 1];
      if (extension === 'png'
        || extension === 'jpg'
        || extension === 'jpeg'
        || extension === 'gif') {
        return true;
      }
    }
    return false;
  }

  /**
   * Check if the current event is closed and attending is not possible anymore
   * @returns {boolean}
   */
  isClosed() {
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

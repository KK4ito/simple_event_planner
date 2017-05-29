import {Component, ViewChild, ElementRef} from '@angular/core';
import {NavController, NavParams, ViewController} from 'ionic-angular';
import {ApiService} from "../../providers/api.service";
import {Mail} from "../../models/Mail";
import {Event} from "../../models/Event";
import {TranslatedSnackbarService} from "../../providers/translated-snackbar.service";
declare let tinymce: any;

@Component({
  selector: 'page-invite',
  templateUrl: 'invite.html',
})

export class InvitePage {

  /**
   * "to" addresses
   * @type {Array}
   */
  public tos = [];

  /**
   * "cc" addresses
   * @type {Array}
   */
  public ccs = [];

  /**
   * Email subject
   * @type {string}
   */
  public subject = '';

  /**
   * Current event
   */
  public event: Event;

  /**
   * Email body
   */
  public body: string;

  /**
   * Reference to the tinymce element
   */
  @ViewChild('tinymceText') tinymceText: ElementRef;

  constructor(public navCtrl: NavController, public navParams: NavParams, private _apiService: ApiService, private viewCtrl: ViewController, private translatedSnackbarService: TranslatedSnackbarService) {
    this.event = navParams.data.event;
  }

  /**
   * Ionic lifecycle that fires after the view has loaded
   */
  ionViewDidLoad() {
    this._apiService.getInvitationTemplate().then(template => {
      if(template.to) {
        this.tos = template.to.split(",");
      }
      if(template.cc) {
        this.ccs = template.cc.split(",");
      }
      this.subject = template.subject;

      tinymce.init({
        skin_url: 'assets/skins/lightgray',
        selector: 'div.tinymce',
        theme: 'modern',
        plugins: 'placeholder image table link paste contextmenu textpattern autolink',
        placeholder_tokens: [
          { token: "eventDateDay", title: "eventDateDay" },
          { token: "eventDate", title: "eventDate" },
          { token: "eventTime", title: "eventTime" },
          { token: "name", title: "eventName" },
          { token: "eventRoom", title: "eventRoom" },
          { token: "eventDeadline", title: "eventDeadline" },
          { token: "eventLink", title: "eventLink" },
          { token: "koordinator", title: "koordinator" }
        ],
        insert_toolbar: 'placeholder quickimage quicktable',
        selection_toolbar: 'bold italic | quicklink h2 h3 blockquote',
        inline: false,
        paste_data_images: false,
      });

      tinymce.activeEditor.setContent(template.body, {format: 'raw'});
    });
  }

  /**
   * Send email
   */
  sendMail() {
    let mail = new Mail();
    mail.to = this.tos.join(",");
    mail.cc = this.ccs.join(",");
    mail.subject = this.subject;
    mail.body = tinymce.activeEditor.getContent();
    mail.eventId = this.event.id;

    this._apiService.createInvitationEmail(mail).then(res => {
      this.translatedSnackbarService.showSnackbar('EMAIL_SENT');
    }).catch(err => {
      this.translatedSnackbarService.showSnackbar('EMAIL_FAILED');
    })
  }

  /**
   * Dismiss modal
   */
  dismiss() {
    this.viewCtrl.dismiss();
  }
}

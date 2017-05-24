import {Component, ViewChild, ElementRef} from '@angular/core';
import {NavController, NavParams, ViewController, ToastController} from 'ionic-angular';
import {ApiService} from "../../providers/api.service";
import {Mail} from "../../models/Mail";
import {Event} from "../../models/Event";
declare var tinymce: any;

@Component({
  selector: 'page-invite',
  templateUrl: 'invite.html',
})

export class InvitePage {

  public tos = [];
  public ccs = [];
  public subject = '';
  public event: Event;
  public body: string;

  @ViewChild('tinymceText') tinymceText: ElementRef;

  constructor(public navCtrl: NavController, public navParams: NavParams, private _apiService: ApiService, private viewCtrl: ViewController, private toastCtrl: ToastController) {
    this.event = navParams.data.event;
  }

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
          { token: "foo", title: "Foo example"}
        ],
        insert_toolbar: 'placeholder quickimage quicktable',
        selection_toolbar: 'bold italic | quicklink h2 h3 blockquote',
        inline: false,
        paste_data_images: false,
      });

      tinymce.activeEditor.setContent(template.body, {format: 'raw'});
    });
  }

  sendMail(){
    let mail = new Mail();
    mail.to = this.tos.join(",");
    mail.cc = this.ccs.join(",");
    mail.subject = this.subject;
    mail.body = tinymce.activeEditor.getContent();
    mail.eventId = this.event.id;

    this._apiService.createInvitationEmail(mail).then(res => {
      console.log(res);
      this.toastCtrl.create({
        message: 'Email sent successfully.',
        duration: 3000,
        position: 'bottom right'
      }).present();
      this.viewCtrl.dismiss();
    }).catch(err => {
      console.log(err);
      this.toastCtrl.create({
        message: 'Email could not be sent.',
        duration: 3000,
        position: 'bottom right'
      }).present();
    })
  }

  dismiss() {
    this.viewCtrl.dismiss();
  }
}

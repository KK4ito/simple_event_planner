import {Component} from '@angular/core';
import {NavController, NavParams} from 'ionic-angular';
declare var tinymce: any;

/**
 * Generated class for the InvitePage page.
 *
 * See http://ionicframework.com/docs/components/#navigation for more info
 * on Ionic pages and navigation.
 */

@Component({
  selector: 'page-invite',
  templateUrl: 'invite.html',
})

export class InvitePage {

  public tos =['schoenbaechler.lukas@gmail.com'];
  public css =['asdf@asdf.com'];

  constructor(public navCtrl: NavController, public navParams: NavParams) {

  }

  ionViewDidLoad() {

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

  }

  sendMail(){
    alert('send');
  }


}

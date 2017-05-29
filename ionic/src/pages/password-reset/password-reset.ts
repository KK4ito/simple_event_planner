import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';
import {AuthService} from "../../providers/auth.service";
import {ApiService} from "../../providers/api.service";
import {TranslatedSnackbarService} from "../../providers/translated-snackbar.service";
import {ProfilePage} from "../profile/profile";
import {PasswordServiceProvider} from "../../providers/password.service";

@Component({
  selector: 'page-password-reset',
  templateUrl: 'password-reset.html',
})
export class PasswordResetPage {

  /**
   * Email address
   */
  private email: string;

  /**
   * Password reset token
   */
  private passwordResetToken: string;

  /**
   * Password
   */
  private password: string;

  /**
   * Password confirmation
   */
  private passwordConfirm: string;

  constructor(public navCtrl: NavController, public navParams: NavParams, public authService: AuthService, public apiService: ApiService, private translatedSnackbarService: TranslatedSnackbarService, private _passwordServiceProvider: PasswordServiceProvider) {
    this.passwordResetToken = this.navParams.get('resetToken');
  }

  /**
   * Send the new password to the server
   */
  resetPassword() {
    if (this.passwordConfirm === this.password) {
      if (this._passwordServiceProvider.validatePassword(this.passwordConfirm)) {
        this.authService.resetPassword(this.passwordResetToken, this.password).then(res => {
          this.translatedSnackbarService.showSnackbar('PASSWORD_UPDATED');
          this.navCtrl.setRoot(ProfilePage);
        }).catch(err => console.log(err));
      } else {
        this.translatedSnackbarService.showSnackbar('PASSWORD_NOT_SECURE');
      }
    } else {
      this.translatedSnackbarService.showSnackbar('PASSWORD_MISMATCH');
    }
  }

  /**
   * Request a new password from the service
   */
  requestPasswordReset() {
    this.authService.requestPasswordReset(this.email).then(res => {
      this.translatedSnackbarService.showSnackbar('PASSWORD_TOKEN_SENT');
      this.navCtrl.pop();
    }).catch(err => {
      this.translatedSnackbarService.showSnackbar('PASSWORD_TOKEN_NOT_SENT');
      this.email = "";
    });
  }

}

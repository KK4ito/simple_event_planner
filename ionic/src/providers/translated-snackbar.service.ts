import {Injectable} from '@angular/core';
import {ToastController} from "ionic-angular";
import {TranslateService} from "@ngx-translate/core";

@Injectable()
export class TranslatedSnackbarService {

  constructor(private toastController: ToastController, private translate: TranslateService) {
  }

  public showSnackbar(messageKey: string, actionKey: string = null, messageValues: any = {}, duration: number = 3000): Promise<any> {
    return new Promise((resolve, reject) => {
      var toTranslate = [messageKey];
      if(actionKey) toTranslate.push(actionKey);
      this.translate.get(toTranslate, messageValues).subscribe((res: any) => {
        let toast = this.toastController.create({
          message: res[messageKey],
          duration: duration,
          showCloseButton: (actionKey) ? true : false,
          closeButtonText: (actionKey) ? res[actionKey] : '',
          position: 'bottom right'
        });
        toast.onDidDismiss((data, role) => {
          if (role == "close") {
            resolve(data);
          }
        });
        toast.present();
      });
    });
  }
}

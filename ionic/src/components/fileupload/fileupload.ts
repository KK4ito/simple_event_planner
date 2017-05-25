import {Component, NgZone, Inject, Output, EventEmitter, Input, ViewChild} from '@angular/core';
import { environment } from '../../../environments/environment';
import {NgUploaderOptions, NgFileDropDirective} from 'ngx-uploader';
import { File } from "../../models/File";
import { CookieService } from 'ngx-cookie';

@Component({
  selector: 'fileupload',
  templateUrl: 'fileupload.html'
})
export class FileuploadComponent {

  @Input() height: number;
  @Input() filter:string[];
  @Output() onFinished: EventEmitter<[boolean, File]> = new EventEmitter();

  @ViewChild(NgFileDropDirective) private fileSelect: NgFileDropDirective;


  options: NgUploaderOptions;
  response: any;
  hasBaseDropZoneOver: boolean;
  progress: number;

  constructor(@Inject(NgZone) private zone: NgZone, private _cookieService: CookieService) {
    this.options = new NgUploaderOptions({
      url: environment.baseUrl + '/api/files',
      maxSize: 2097152,
      customHeaders: {
        'Accept':'application/json',
        'X-XSRF-TOKEN': this._cookieService.get('XSRF-TOKEN')
      },
      maxUploads: 2,
      autoUpload: true,
      filterExtensions: true,
      withCredentials: true,
      allowedExtensions: ['txt', 'pdf', 'jpg', 'png']
    });
  }

  handleUpload(data: any) {
    setTimeout(() => {
      this.zone.run(() => {
        this.response = data;
        console.log('data1', data);

        if (this.response.progress) {
          this.progress = this.response.progress.percent;
          console.log(this.progress);
        }
        if (data && data.response) {
          console.log('data2', data);
          this.response = JSON.parse(data.response);
          this.onFinished.emit([true, this.response])
        }
      });
    });
  }

  fileOverBase(e: boolean) {
    this.hasBaseDropZoneOver = e;
  }

}

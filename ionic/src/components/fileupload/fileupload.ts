import {Component, NgZone, Inject, Output, EventEmitter, Input, ViewChild} from '@angular/core';
import { environment } from '../../../environments/environment';
import {NgUploaderOptions, NgFileDropDirective} from 'ngx-uploader';
import { File } from "../../models/File";

@Component({
  selector: 'fileupload',
  templateUrl: 'fileupload.html'
})
export class FileuploadComponent {

  @Input() filter:string[];
  @Output() onFinished: EventEmitter<[boolean, File]> = new EventEmitter();

  @ViewChild(NgFileDropDirective) private fileSelect: NgFileDropDirective;


  options: NgUploaderOptions;
  response: any;
  hasBaseDropZoneOver: boolean;
  progress: number;

  constructor(@Inject(NgZone) private zone: NgZone) {
    this.options = new NgUploaderOptions({
      url: environment.baseUrl + '/api/files',
      maxSize: 2097152,
      customHeaders: {
        'Accept':'application/json'
      },
      maxUploads: 2,
      autoUpload: true,
      filterExtensions: true,
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

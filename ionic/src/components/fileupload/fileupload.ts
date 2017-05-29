import { Component, NgZone, Inject, Output, EventEmitter, Input, ViewChild } from '@angular/core';
import { environment } from '../../../environments/environment';
import { NgUploaderOptions, NgFileDropDirective } from 'ngx-uploader';
import { File } from "../../models/File";
import { CookieService } from 'ngx-cookie';

@Component({
  selector: 'fileupload',
  templateUrl: 'fileupload.html'
})
export class FileuploadComponent {

  @Input() height: number;
  @Input() allowedExtensions: string[] = ['jpg', 'jpeg', 'png'];
  @Input() maxUploads: number = 1;
  @Output() onFinished: EventEmitter<[boolean, File]> = new EventEmitter();

  @ViewChild(NgFileDropDirective) private fileSelect: NgFileDropDirective;


  options: NgUploaderOptions;
  response: any;
  hasBaseDropZoneOver: boolean;
  progress: number;

  constructor( @Inject(NgZone) private zone: NgZone, private _cookieService: CookieService) { }

  ngOnInit() {
    this.options = new NgUploaderOptions({
      url: environment.baseUrl + '/api/files',
      maxSize: 20000000, // 20 MB
      customHeaders: {
        'Accept': 'application/json',
        'X-XSRF-TOKEN': this._cookieService.get('XSRF-TOKEN')
      },
      maxUploads: this.maxUploads,
      autoUpload: true,
      filterExtensions: true,
      withCredentials: true,
      allowedExtensions: this.allowedExtensions
    });
  }

  handleUpload(data: any) {
    setTimeout(() => {
      this.zone.run(() => {
        this.response = data;
        if (this.response.progress) {
          this.progress = this.response.progress.percent;
        }
        if (data && data.response) {
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

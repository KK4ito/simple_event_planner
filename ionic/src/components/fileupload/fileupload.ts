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

  /**
   * Height of the fileupload box
   */
  @Input() height: number;

  /**
   * Accepted extensions
   * @type {string[]}
   */
  @Input() allowedExtensions: string[] = ['jpg', 'jpeg', 'png'];

  /**
   * Maximum number of concurrent uploads
   * @type {number}
   */
  @Input() maxUploads: number = 1;

  /**
   * EventEmitter that will notify the parent of new file uploads
   * @type {EventEmitter}
   */
  @Output() onFinished: EventEmitter<[boolean, File]> = new EventEmitter();

  /**
   * Options for the file upload. eg. headers
   */
  options: NgUploaderOptions;

  /**
   * Response of the last upload
   */
  response: any;

  /**
   * This will be true if a file is about to be dropped on the file upload area
   */
  hasBaseDropZoneOver: boolean;

  /**
   * The progress of the current file upload
   */
  progress: number;

  constructor( @Inject(NgZone) private zone: NgZone, private _cookieService: CookieService) { }

  /**
   * Lifecycle event to initialize the fileuploader.
   * Options are constructed here because the @Input variables are not defined in the constructor.
   */
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

  /**
   * Callback after file has been uploaded
   * @param data
   */
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

  /**
   * Function that sets the hasBaseDropZoneOver on hover
   * @param e
   */
  fileOverBase(e: boolean) {
    this.hasBaseDropZoneOver = e;
  }

}

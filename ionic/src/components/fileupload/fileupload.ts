import {Component, NgZone, Inject, Output, EventEmitter, Input, ViewChild} from '@angular/core';
import { environment } from '../../../environments/environment';
import { UploadOutput, UploadInput, UploadFile, humanizeBytes } from 'ngx-uploader';
import { File } from "../../models/File";
import { CookieService } from 'ngx-cookie';

declare let window: any;

@Component({
  selector: 'fileupload',
  templateUrl: 'fileupload.html'
})
export class FileuploadComponent {

  @Input() height: number;
  @Input() filter:string[];
  @Output() onFinished: EventEmitter<[boolean, File]> = new EventEmitter();

  files: UploadFile[];
  uploadInput: EventEmitter<UploadInput>;
  humanizeBytes: Function;
  dragOver: boolean;

  progress: number;

  constructor(@Inject(NgZone) private zone: NgZone, private _cookieService: CookieService) {
    this.files = []; // local uploading files array
    this.uploadInput = new EventEmitter<UploadInput>(); // input events, we use this to emit data to ngx-uploader
    this.humanizeBytes = humanizeBytes;
    /*

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
    */
  }

  onUploadOutput(output: UploadOutput): void {
    console.log(output); // lets output to see what's going on in the console

    if (output.type === 'allAddedToQueue') { // when all files added in queue
      console.log(this._cookieService.getAll());
      const event: UploadInput = {
        type: 'uploadAll',
        url: environment.baseUrl + '/api/files',
        method: 'POST',
        concurrency: 0,
        headers: {
          'X-XSRF-TOKEN': this._cookieService.get('XSRF-TOKEN')
        }
      };
      this.uploadInput.emit(event);
    } else if (output.type === 'addedToQueue') {
      this.files.push(output.file); // add file to array when added
    } else if (output.type === 'uploading') {
      // update current data in files array for uploading file
      const index = this.files.findIndex(file => file.id === output.file.id);
      this.files[index] = output.file;
    } else if (output.type === 'removed') {
      // remove file from array when removed
      this.files = this.files.filter((file: UploadFile) => file !== output.file);
    } else if (output.type === 'dragOver') { // drag over event
      this.dragOver = true;
    } else if (output.type === 'dragOut') { // drag out event
      this.dragOver = false;
    } else if (output.type === 'drop') { // on drop event
      this.dragOver = false;
    }
  }

  startUpload(): void {  // manually start uploading
    const event: UploadInput = {
      type: 'uploadAll',
      url: '/upload',
      method: 'POST',
      data: { foo: 'bar' },
      concurrency: 1 // set sequential uploading of files with concurrency 1
    }

    this.uploadInput.emit(event);
  }
  /*
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
*/
}

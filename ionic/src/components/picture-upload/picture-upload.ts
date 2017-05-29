import {Component, Input, Output, EventEmitter, ChangeDetectorRef} from '@angular/core';
import {SafeStyle, DomSanitizer} from "@angular/platform-browser";
import {environment} from '../../../environments/environment';
import {File} from "../../models/File";

@Component({
  selector: 'picture-upload',
  templateUrl: 'picture-upload.html'
})
export class PictureUploadComponent {

  /**
   * Current image url
   */
  @Input() image: string;

  /**
   * Height of the upload box
   */
  @Input() height: number;

  /**
   * EventEmitter that will notify the parent of new file uploads
   * @type {EventEmitter}
   */
  @Output() onFinished: EventEmitter<[File]> = new EventEmitter();

  /**
   * Variable to set the background url
   */
  private placeHolderStyle: SafeStyle;

  /**
   * Variable to check if edit mode is active
   * @type {boolean}
   */
  private isEdit = false;

  constructor(private sanitizer: DomSanitizer, private changeDetectorRef: ChangeDetectorRef) {
  }

  /**
   * Ionic lifecycle event that fires after the view has been initialized
   */
  ngAfterViewInit() {
    if(this.image == undefined || this.image == ""){
      this.image = "/default/avatar.png";
    }
    this.placeHolderStyle = this.sanitizer.bypassSecurityTrustStyle('url(\'' + environment.baseUrl + this.image + '\')');
    this.changeDetectorRef.detectChanges();
  }

  /**
   * Activate edit mode
   */
  edit() {
    this.isEdit = true;
  }

  /**
   * Callback that is called with the uploaded file
   * @param success
   * @param file
   */
  uploadFinished(success, file: File) {
    if(success) {
      this.placeHolderStyle = this.sanitizer.bypassSecurityTrustStyle('url(\'' + environment.baseUrl + file.uri + '\')');
      this.isEdit = false;
      this.onFinished.emit([file]);
    }

  }
}

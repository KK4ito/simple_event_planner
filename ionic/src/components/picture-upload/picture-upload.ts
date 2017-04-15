import {Component, Input, Output, EventEmitter, ChangeDetectorRef} from '@angular/core';
import {SafeStyle, DomSanitizer} from "@angular/platform-browser";
import {environment} from '../../../environments/environment';
import {File} from "../../models/File";

/*
  Generated class for the PictureUpload component.

  See https://angular.io/docs/ts/latest/api/core/index/ComponentMetadata-class.html
  for more info on Angular 2 Components.
*/
@Component({
  selector: 'picture-upload',
  templateUrl: 'picture-upload.html'
})
export class PictureUploadComponent {

  @Input() image: string;
  @Input() height: number;
  @Output() onFinished: EventEmitter<[File]> = new EventEmitter();

  private placeHolderStyle: SafeStyle;
  private isEdit = false;

  constructor(private sanitizer: DomSanitizer, private changeDetectorRef: ChangeDetectorRef) {
  }

  ngAfterViewInit() {
    if(this.image == undefined || this.image == ""){
      this.image = "/default/avatar.png";
    }
    this.placeHolderStyle = this.sanitizer.bypassSecurityTrustStyle('url(\'' + environment.baseUrl + this.image + '\')');
    this.changeDetectorRef.detectChanges();
  }

  clicked(){
    this.isEdit = true;
  }

  uploadFinished(success, file: File) {
    if(success){
      this.placeHolderStyle = this.sanitizer.bypassSecurityTrustStyle('url(\'' + environment.baseUrl + file.uri + '\')');
      this.isEdit = false;
      this.onFinished.emit([file]);
    }

  }
}

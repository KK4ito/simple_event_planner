import { Component, OnInit } from '@angular/core';
import { MdDialog } from "@angular/material";

import { DialogComponent } from '../../components/dialog/dialog.component';

@Component({
  selector: 'app-presentations',
  templateUrl: './presentations.component.html',
  styleUrls: ['./presentations.component.scss']
})
export class PresentationsComponent implements OnInit {
  presentations: any;

  constructor(public dialog: MdDialog) {
    this.presentations = [
      {
        title: "Machine Learning",
        presenters: [
          {
            name: "Andreas"
          }
        ],
        date: new Date()
      },
      {
        title: "Machine Learning",
        presenters: [
          {
            name: "Andreas"
          }
        ],
        date: new Date()
      }
    ];
  }

  ngOnInit() {
  }

  openDialog() {
    let dialogRef = this.dialog.open(DialogComponent, {
      height: '400px',
      width: '600px',
    });
    dialogRef.afterClosed().subscribe(result => {
      console.log(result);
    });
  }
}

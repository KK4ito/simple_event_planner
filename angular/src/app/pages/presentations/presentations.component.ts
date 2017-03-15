import { Component, OnInit } from '@angular/core';
import { MdDialog } from "@angular/material";
import { Router } from "@angular/router";

import { DialogComponent } from '../../components/dialog/dialog.component';

@Component({
  selector: 'app-presentations',
  templateUrl: './presentations.component.html',
  styleUrls: ['./presentations.component.scss']
})
export class PresentationsComponent implements OnInit {
  presentations: any;

  constructor(private router: Router, public dialog: MdDialog) {
    this.presentations = [
      {
        id: 1,
        title: "Machine Learning",
        presenters: [
          {
            name: "Andreas"
          }
        ],
        date: new Date()
      },
      {
        id: 2,
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

  openLink(id) {
    this.router.navigate(['/detail', id]);
  }

  openDialog(ev) {
    ev.stopPropagation();

    let dialogRef = this.dialog.open(DialogComponent, {
      height: '400px',
      width: '600px',
    });
    dialogRef.afterClosed().subscribe(result => {
      console.log(result);
    });
  }
}

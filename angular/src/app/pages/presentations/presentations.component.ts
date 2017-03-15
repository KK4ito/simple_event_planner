import { Component, OnInit } from '@angular/core';
import { MdDialog } from "@angular/material";
import { Router } from "@angular/router";
import { ApiService } from '../../providers/api.service';

import { DialogComponent } from '../../components/dialog/dialog.component';

@Component({
  selector: 'app-presentations',
  templateUrl: './presentations.component.html',
  styleUrls: ['./presentations.component.scss']
})
export class PresentationsComponent implements OnInit {
  events: any;

  constructor(private router: Router, private _apiService: ApiService, public dialog: MdDialog) {
    this.events = [
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

    this._apiService.getEventsList().then(data => {
      console.log(data);
      this.events = data._embedded.events;

    });
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

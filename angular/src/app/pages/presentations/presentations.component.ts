import { Component, OnInit } from '@angular/core';
import { MdDialog } from "@angular/material";
import { Router } from "@angular/router";
import { ApiService } from '../../providers/api.service';
import { Event } from '../../models/Event';

import { DialogComponent } from '../../components/dialog/dialog.component';

@Component({
  selector: 'app-presentations',
  templateUrl: './presentations.component.html',
  styleUrls: ['./presentations.component.scss']
})
export class PresentationsComponent implements OnInit {
  events: Event[];

  constructor(private router: Router, private _apiService: ApiService, public dialog: MdDialog) {
    this._apiService.getEvents().then(data => this.events = data);
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

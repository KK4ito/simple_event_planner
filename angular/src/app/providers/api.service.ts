import { Injectable } from '@angular/core';
import { AuthService } from './auth.service';

@Injectable()
export class ApiService {

  constructor(private _authService: AuthService) { }

  getEventsList() {
    return this._authService.get('events');
  }
}

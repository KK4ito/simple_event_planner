import {Injectable} from '@angular/core';
import {AuthService} from './auth.service';
import {Event} from '../models/Event';
import {User} from "../models/User";
import {File} from "../models/File";
import {EventAttendee} from "../models/EventAttendee";
import {RoleType} from "../models/RoleType";
import {EventAttendeeFlat} from "../models/EventAttendeeFlat";
import {Mail} from "../models/Mail";

@Injectable()
export class ApiService {

  constructor(private _authService: AuthService) {
  }

  getUsers(): Promise<User[]> {
    return this._authService.getMultiple('users');
  }

  getEvents(): Promise<Event[]> {
    return this._authService.getMultiple('events');
  }

  getEvent(eventId: number): Promise<Event> {
    return this._authService.getSingle('events/' + eventId);
  }

  getSpeakers(eventId: number): Promise<User[]> {
    return this._authService.getMultiple('events/' + eventId + '/speakers');
  }

  getAttendees(eventId: number): Promise<User[]> {
    return this._authService.getMultiple('users/search/attendees', 'event=' + eventId);
  }

  getPrint(eventId: number): Promise<EventAttendeeFlat[]> {
    return this._authService.getMultiple('print/' + eventId);
  }

  getUsersByRole(roleType: RoleType): Promise<User[]> {
    return this._authService.getMultiple('users/search/role', 'role=' + roleType);
  }

  getFiles(eventId: number): Promise<File[]> {
    return this._authService.getMultiple('events/' + eventId + '/files');
  }

  createEvent(event: Event): Promise<Event> {
    return this._authService.post('events', event);
  }

  createUser(user: User): Promise<User> {
    user.internal = false;
    user.role = RoleType.REGISTERED;
    return this._authService.post('users', user);
  }

  updateUser(user: User): Promise<User> {
    return this._authService.put('users/' + user.id, user);
  }

  updateUserPartial(id:number, user: any): Promise<User> {
    return this._authService.patch('users/' + id, user);
  }

  deleteUser(id: number): Promise<User> {
    return this._authService.delete('users/' + id);
  }

  updateEvent(event): Promise<Event> {
    return this._authService.patch('events/' + event.id, event);
  }

  getUsersByName(name: string): Promise<User[]> {
    return this._authService.getMultiple('users/search/name', 'name=' + encodeURI(name.toUpperCase()));
  }

  getAttends(userId: number, eventId: number): Promise<EventAttendee> {
    return new Promise((resolve, reject) => {
      this._authService
        .getMultiple('eventAttendees/search/attends', 'user=\/user\/' + userId + '&event=\/event\/' + eventId)
        .then((result: User[]) => {
          if(result.length > 0) resolve(result[0]);
          reject();
        })
        .catch((err) => reject(err));
    });
  }

  deleteEventAttendee(id: number): Promise<EventAttendee> {
    return this._authService.delete('eventAttendees/' + id);
  }
  createEventAttendee(eventAttendee: EventAttendee): Promise<EventAttendee> {
    return this._authService.post('eventAttendees', eventAttendee);
  }

  createInvitationEmail(mail: Mail): Promise<Mail> {
    return this._authService.post('mail', mail);
  }

  getInvitationTemplate(): Promise<Mail> {
    return this._authService.getSingle('template');
  }

  requestPasswordReset(email) {
    return this._authService.post('requestPasswordReset', { email: email })
  }

  resetPassword(token, password) {
    return this._authService.post('resetPassword', {
      token: token,
      password: password
    })
  }
}

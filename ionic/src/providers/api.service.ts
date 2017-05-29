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

  /**
   * Get user list
   * @returns {Promise<T>}
   */
  getUsers(): Promise<User[]> {
    return this._authService.getMultiple('users');
  }

  /**
   * Get event list
   * @returns {Promise<T>}
   */
  getEvents(): Promise<Event[]> {
    return this._authService.getMultiple('events');
  }

  /**
   * Get event by event ID
   * @param eventId
   * @returns {Promise<T>}
   */
  getEvent(eventId: number): Promise<Event> {
    return this._authService.getSingle('events/' + eventId);
  }

  /**
   * Get speaker by event ID
   * @param eventId
   * @returns {Promise<T>}
   */
  getSpeakers(eventId: number): Promise<User[]> {
    return this._authService.getMultiple('events/' + eventId + '/speakers');
  }

  /**
   * Get attendees by event ID
   * @param eventId
   * @returns {Promise<T>}
   */
  getAttendees(eventId: number): Promise<User[]> {
    return this._authService.getMultiple('users/search/attendees', 'event=' + eventId);
  }

  /**
   * Get print info by event ID
   * @param eventId
   * @returns {Promise<T>}
   */
  getPrint(eventId: number): Promise<EventAttendeeFlat[]> {
    return this._authService.getMultiple('print/' + eventId);
  }

  /**
   * Get users by role
   * @param roleType
   * @returns {Promise<T>}
   */
  getUsersByRole(roleType: RoleType): Promise<User[]> {
    return this._authService.getMultiple('users/search/role', 'role=' + roleType);
  }

  /**
   * Get files by event ID
   * @param eventId
   * @returns {Promise<T>}
   */
  getFiles(eventId: number): Promise<File[]> {
    return this._authService.getMultiple('events/' + eventId + '/files');
  }

  /**
   * Create event
   * @param event
   * @returns {Promise<any>}
   */
  createEvent(event: Event): Promise<Event> {
    return this._authService.post('events', event);
  }

  /**
   * Create User
   * @param user
   * @returns {Promise<any>}
   */
  createUser(user: User): Promise<User> {
    user.internal = false;
    user.role = RoleType.REGISTERED;
    return this._authService.post('users', user);
  }

  /**
   * Update User
   * @param user
   * @returns {Promise<any>}
   */
  updateUser(user: User): Promise<User> {
    return this._authService.put('users/' + user.id, user);
  }

  /**
   * Update partial user (only some values)
   * @param id
   * @param user
   * @returns {Promise<any>}
   */
  updateUserPartial(id:number, user: any): Promise<User> {
    return this._authService.patch('users/' + id, user);
  }

  deleteUser(id: number): Promise<User> {
    return this._authService.delete('users/' + id);
  }

  /**
   * Update event
   * @param event
   * @returns {Promise<any>}
   */
  updateEvent(event): Promise<Event> {
    return this._authService.patch('events/' + event.id, event);
  }

  /**
   * Get users by name
   * @param name
   * @returns {Promise<T>}
   */
  getUsersByName(name: string): Promise<User[]> {
    return this._authService.getMultiple('users/search/name', 'name=' + encodeURI(name.toUpperCase()));
  }

  /**
   * Get if the current user attends an event
   * @param userId
   * @param eventId
   * @returns {Promise<T>}
   */
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

  /**
   * Delete event attendees
   * @param id
   * @returns {Promise<any>}
   */
  deleteEventAttendee(id: number): Promise<EventAttendee> {
    return this._authService.delete('eventAttendees/' + id);
  }

  /**
   * Create event attendees
   * @param eventAttendee
   * @returns {Promise<any>}
   */
  createEventAttendee(eventAttendee: EventAttendee): Promise<EventAttendee> {
    return this._authService.post('eventAttendees', eventAttendee);
  }

  /**
   * Create invitation email
   * @param mail
   * @returns {Promise<any>}
   */
  createInvitationEmail(mail: Mail): Promise<Mail> {
    return this._authService.post('mail', mail);
  }

  /**
   * Get invitation template
   * @returns {Promise<T>}
   */
  getInvitationTemplate(): Promise<Mail> {
    return this._authService.getSingle('template');
  }
}

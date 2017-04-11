import { Injectable } from '@angular/core';
import { AuthService } from './auth.service';
import { Event} from '../models/Event';
import {User} from "../models/User";
import {File} from "../models/File";
import {Role} from "../models/Role";

@Injectable()
export class ApiService {

  constructor(private _authService: AuthService) { }

  getUsers(): Promise<User[]> {
    return this._authService.getMultiple('users');
  }

  getEvents(): Promise<Event[]> {
    return this._authService.getMultiple('events');
  }

  getEvent(eventId:number): Promise<Event> {
    return this._authService.getSingle('events/' + eventId);
  }

  getSpeakers(eventId:number): Promise<User[]> {
    return this._authService.getMultiple('events/' + eventId + '/speakers');
  }

  getAttendees(eventId:number): Promise<User[]> {
    return this._authService.getMultiple('users/search/attendees', 'event=' + eventId);
  }

  getUsersByRole(role:Role): Promise<User[]> {
    return this._authService.getMultiple('users/search/role', 'role=' + role.toString());
  }

  getFiles(eventId:number): Promise<File[]> {
    return this._authService.getMultiple('events/' + eventId + '/files');
  }

  createEvent(object): Promise<Event> {
    return this._authService.post('events', object);
  }

  updateEvent(id, object): Promise<Event> {
    return this._authService.put('events/' + id, object);
  }

  updateUser(id, object): Promise<Event> {
    return this._authService.put('users/' + id, object);
  }

  getUsersByName(name:string): Promise<User[]>{
    return this._authService.getMultiple('users/search/name', 'name=' + encodeURI(name.toUpperCase()));
  }
}

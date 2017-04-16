import { Injectable } from '@angular/core';
import {Http, Headers} from '@angular/http';
import { environment } from '../../environments/environment';
import {User} from "../models/User";

@Injectable()
export class AuthService {

  public user:User;
  config: any;

  constructor(private http: Http) {
    console.log(environment);
    this.config = {
      baseUrl: environment.baseUrl + '/api'
    };

    var user = JSON.parse(localStorage.getItem('user')) as User;
    if(user) {
      this.user = user;
      this.refreshAuthToken();
    }
  }

  public isAuthed(){
    return this.user == null;
  }

  public login(user:User){
    this.user = user;
    localStorage.setItem('user', JSON.stringify(user));
    this.refreshAuthToken();
  }

  public logout(){
    this.user = null;
    localStorage.removeItem('user');
    this.refreshAuthToken();
  }

  private refreshAuthToken(){
    let anyHttp = this.http as any;
    if(!this.user){
      anyHttp._defaultOptions.headers = new Headers();
    }else{
      var tok = this.user.email + ':' + this.user.password;
      anyHttp._defaultOptions.headers.append('Authorization', "Basic " + btoa(tok));
    }
  }

  /**
   * Get request to an endpoint with all the required headers set automatically
   *
   * @param endpoint
   * @param parameters
   * @returns {Promise<T>|Promise}
   */
  getMultiple<T>(endpoint, parameters = ''): Promise<T> {
    return new Promise((resolve, reject) => {
      var url = this.config.baseUrl + '/' + endpoint + ((parameters === '') ? '' : '?' + parameters);
      this.http.get(url)
        .subscribe(data => {
          // wait until not uninit
          var obj = data.json()._embedded;
          resolve(obj[Object.keys(obj)[0]]);
        }, error => {
          reject(error);
        });
    });
  }

  /**
   * Get request to an endpoint with all the required headers set automatically
   *
   * @param endpoint
   * @param parameters
   * @returns {Promise<T>|Promise}
   */
  getSingle<T>(endpoint, parameters = ''): Promise<T> {
    return new Promise((resolve, reject) => {
      this.http.get(this.config.baseUrl + '/' + endpoint + '/' + parameters)
        .subscribe(data => {
          // wait until not uninit
          resolve(data.json());
        }, error => {
          reject(error);
        });
    });
  }

  /**
   * Puts an object to an endpoint with all the required headers set automatically
   *
   * @param endpoint
   * @param object
   * @returns {Promise<T>|Promise}
   */
  put(endpoint, object): Promise<any> {
    return new Promise((resolve, reject) => {
      this.http.put(this.config.baseUrl + '/' + endpoint + '/', object)
        .subscribe(data => {
          try { // TODO: Is there a better way to do this?
            resolve(data.json());
          } catch (e) {
            resolve();
          }
        }, error => {
          reject(error);
        });

    });
  }

  /**
   * Posts an object to an endpoint with all the required headers set automatically
   *
   * @param endpoint
   * @param object
   * @returns {Promise<T>|Promise}
   */
  post(endpoint, object): Promise<any> {
    return new Promise((resolve, reject) => {
      this.http.post(this.config.baseUrl + '/' + endpoint + '/', object)
        .subscribe(data => {
          resolve(data.json());
        }, error => {
          reject(error);
        });
    });
  }

  /**
   * Deletes an object
   *
   * @param endpoint
   * @returns {Promise<T>|Promise}
   */
  delete(endpoint): Promise<any> {
    return new Promise((resolve, reject) => {
      this.http.delete(this.config.baseUrl + '/' + endpoint + '/')
        .subscribe(data => {
          //this.log('delete response', data.json());
          //resolve(data.json());
          resolve();
        }, error => {
          reject(error);
        });
    });
  }
}

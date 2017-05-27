import {Injectable} from '@angular/core';
import {Http, Headers} from '@angular/http';
import {environment} from '../../environments/environment';
import {User} from "../models/User";
import {Events} from "ionic-angular";
import {RoleType} from "../models/RoleType";

@Injectable()
export class AuthService {

  private user: User;
  config: any;

  constructor(private http: Http, public events: Events) {

    this.config = {
      baseUrl: environment.baseUrl + '/api'
    };
    this.login().catch(() => console.log('The error above was expected (session has no logged in user), please ignore.'));
  }

  /**
   * Get the current user
   *
   * @returns {User}
   */
  public getUser(): User {
    return this.user;
  }

  /**
   * Get the role of the current user
   *
   * @returns {RoleType}
   */
  public getRole(): RoleType {
    if (this.user != null) {
      return this.user.role;
    }
    return RoleType.ANONYMOUS;
  }

  /**
   * Log the user in using Basic Auth
   *
   * @param user
   * @returns {Promise<User>}
   */
  public login(user: User = null): Promise<User> {
    let self = this;
    let headers = new Headers();
    if (user) {
      headers.append("Authorization", "Basic " + btoa(user.email + ':' + user.password));
    }
    return new Promise((resolve, reject) => {
      let url = this.config.baseUrl + '/login/login';
      this.http.get(url, {withCredentials: true, headers: headers}).toPromise().then(data => {
        // wait until not uninit
        self.user = data.json();
        this.events.publish('user:changed', self.user);
        resolve(self.user);
      }).catch(error => {
        this.events.publish('user:changed', self.user);
        reject();
      });
    });
  }

  /**
   * Requests a password token to the passed email
   *
   * @param email
   * @returns {Promise<User>}
   */
  requestPasswordReset(email: string): Promise<any> {
    return new Promise((resolve, reject) => {
      this.http.post(this.config.baseUrl + '/login/requestPasswordReset', {email: email}, {withCredentials: true})
        .subscribe(() => {
          resolve();
        }, error => {
          reject(error);
        });
    });
  }

  /**
   * Resets a password with the passed tokan
   *
   * @param token
   * @param password
   * @returns {Promise<User>}
   */
  resetPassword(token: string, password: string): Promise<any> {
    return new Promise((resolve, reject) => {
      this.http.post(this.config.baseUrl + '/login/resetPassword', {
        token: token,
        password: password
      }, {withCredentials: true})
        .subscribe(() => {
          resolve();
        }, error => {
          reject(error);
        });
    });
  }

  /**
   * Log the user out
   */
  logout() {
    this.http.get(this.config.baseUrl + '/login/logout', {withCredentials: true}).toPromise().then(() => {
      this.user = null;
      this.events.publish('user:changed', this.user);
    });
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
      this.http.get(url, {withCredentials: true})
        .subscribe(data => {
          // wait until not uninit
          var json = data.json();
          if (json.hasOwnProperty('_embedded')) {
            var obj = json._embedded;
            resolve(obj[Object.keys(obj)[0]]);
          } else {
            resolve(json);
          }
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
      this.http.get(this.config.baseUrl + '/' + endpoint + '/' + parameters, {withCredentials: true})
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
      this.http.put(this.config.baseUrl + '/' + endpoint + '/', object, {withCredentials: true})
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
   * Patches an object to an endpoint with all the required headers set automatically
   *
   * @param endpoint
   * @param object
   * @returns {Promise<T>|Promise}
   */
  patch(endpoint, object): Promise<any> {
    return new Promise((resolve, reject) => {
      this.http.patch(this.config.baseUrl + '/' + endpoint + '/', object, {withCredentials: true})
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
      this.http.post(this.config.baseUrl + '/' + endpoint + '/', object, {withCredentials: true})
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
      this.http.delete(this.config.baseUrl + '/' + endpoint + '/', {withCredentials: true})
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

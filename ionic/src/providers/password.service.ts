import { Injectable } from '@angular/core';

/**
 * A service that validates passwords
 */
@Injectable()
export class PasswordServiceProvider {

  /**
   * Empty constructor
   */
  constructor() { }

  /**
   * Validate if the password meets our security rules
   *
   * @param password
   * @returns {boolean}
   */
  validatePassword(password) {
    if (!password) return false;
    if (typeof password !== 'string') return false;

    const regex = /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[!?,.$]).{10,128}$/g;

    return regex.test(password);
  }

}

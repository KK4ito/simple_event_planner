import { Injectable } from '@angular/core';

@Injectable()
export class PasswordServiceProvider {

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
    if (!password.length) return false;
    if (!(password === password.toLowerCase())) return false;
    if (!(password === password.toUpperCase())) return false;
    if (!this.hasNumber(password)) return false;

    return true;
  }

  /**
   * Check if the string contains a password
   *
   * @param password
   * @returns {boolean}
   */
  private hasNumber(password) {
    return /\d/.test(password);
  }
}

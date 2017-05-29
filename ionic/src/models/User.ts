import {RoleType} from "./RoleType";

export class User {
  /**
   * Email
   */
  email: string;

  /**
   * First name
   */
  firstName: string;

  /**
   * User ID
   */
  id: number;

  /**
   * Internal = switch-AAI, external users are the ones created by the coordinator
   */
  internal: boolean;

  /**
   * Last name
   */
  lastName: string;

  /**
   * User password
   */
  password: string;

  /**
   * The user role
   */
  role: RoleType;

  /**
   * The uri to the profile picture
   */
  imageUri: string;

  /**
   * The profile picture
   */
  image: string;

  /**
   * For external users: Opt out of notification emails
   */
  optOut: boolean;
}

// Workaround since typescript enums just can be mapped to int and not to string at the moment
export class RoleType
{
  /**
   * The administrator role
   * @type {number}
   */
  public static ADMINISTRATOR = 2;

  /**
   * Regular, registered user. Switch-AAI and external users are both "Registered"
   * @type {number}
   */
  public static REGISTERED = 1;

  /**
   * Anonymous user that did not log in
   * @type {number}
   */
  public static ANONYMOUS = 0;
}

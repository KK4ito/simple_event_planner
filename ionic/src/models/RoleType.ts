// Workaround since typescript enums just can be mapped to int and not to string at the moment
export class RoleType
{
  public static ADMINISTRATOR = 2;
  public static REGISTERED = 1;
  public static ANONYMOUS = 0;
}

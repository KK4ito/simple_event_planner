// Workaround since typescript enums just can be mapped to int and not to string at the moment
export class FoodType {
  /**
   * Vegi sandwich
   * @type {string}
   */
  static VEGI = "VEGI";

  /**
   * Meat sandwich
   * @type {string}
   */
  static MEAT = "MEAT";

  /**
   * No sandwich
   * @type {string}
   */
  static NONE = "NONE";
}

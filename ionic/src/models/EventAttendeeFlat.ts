import {FoodType} from "./FoodType";

export class EventAttendeeFlat {
  /**
   * First name of the attendee
   */
  firstName: string;

  /**
   * Last name of the attendee
   */
  lastName: string;

  /**
   * Indicate if this is an internal or external user (Switch AAI is external)
   */
  internal: boolean;

  /**
   * What kind of food did the attendee order?
   */
  foodType: FoodType;

  /**
   * Did the attendee order a drink?
   */
  drink: boolean;
}

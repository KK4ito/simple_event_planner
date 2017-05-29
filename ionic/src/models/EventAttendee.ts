import {FoodType} from "./FoodType";

export class EventAttendee {
  /**
   * EventAttendee id
   */
  id: number;

  /**
   * The resource_uri of the user that is attending
   */
  user: string;

  /**
   * The resource_uri of the event the attendee is attending
   */
  event: string;

  /**
   * What kind of food did the attendee order?
   */
  foodType: FoodType;

  /**
   * Did the attendee order a drink?
   */
  drink: boolean;
}

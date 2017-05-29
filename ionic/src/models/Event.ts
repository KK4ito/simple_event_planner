export class Event {
  /**
   * The datetime when an event-deadline closes.
   */
  closingTime: string;

  /**
   * The date when an event was created.
   */
  created: string;

  /**
   * The descripition of an event.
   */
  description: string;

  /**
   * The location where the event will take place.
   */
  location: string;

  /**
   * Whether or not the automatically-to-send-mails were already sent.
   */
  closingMailSend: boolean;

  /**
   * The time when an event finishes.
   */
  endTime: string;

  /**
   * The id of an event.
   */
  id: number;

  /**
   * The title of an event.
   */
  name: string;

  /**
   * The image accompanying the event.
   */
  image: string;

  /**
   * The url to the image of the event.
   */
  imageUri: string;

  /**
   * The datetime when an event starts.
   */
  startTime: string;

  /**
   * The date when an event was last updated.
   */
  updated: string;

  /**
   * A list of all speakers that will speak at the event.
   */
  speakers: string[];
}

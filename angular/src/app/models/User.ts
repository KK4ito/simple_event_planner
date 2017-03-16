import {Event} from "./Event";

export class User {
		email: string;
		firstName: string;
		id: number;
		internal: boolean;
		lastName: string;
		password: string;

		attendees: Event[];
		speakers: Event[];
}


import {User} from "./User";
import {File} from "./File";

export class Event {
		closingTime: string;
		created: string;
		description: string;
		endTime: string;
		id: number;
		name: string;
		startTime: string;
		updated: string;

		attendees: User[];
		files: File[];
		speakers: User[];
}

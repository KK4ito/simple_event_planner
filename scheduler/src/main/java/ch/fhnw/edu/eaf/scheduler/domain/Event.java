package ch.fhnw.edu.eaf.scheduler.domain;

import java.util.Collection;
import java.util.Date;

/**
 * An event-object
 */
public class Event {

    /**
     * The id of an event.
     */
    public Long id;

    /**
     * Date when the event was created.
     */
    public Date created;

    /**
     * Date when the event was last updated.
     */
    public Date updated;

    /**
     * The name/title of the event
     */
    public String name;

    /**
     * The description of the event.
     */
    public String description;

    /**
     * The location of the event.
     */
    public String location;

    /**
     * The time when the event starts.
     */
    public Date startTime;

    /**
     * The time when the event is "closed", that is the registration-deadline for the event.
     */
    public Date closingTime;

    /**
     * The time when the event finishes.
     */
    public Date endTime;

    /**
     * Indicates whether the "deadline"-Mails to the sv-group, referenten and raumkoordination were sent.
     */
    public boolean closingMailSend;

    /**
     * Files accompanying this event.
     */
    public File image;

    public Collection<File> files;
    public Collection<User> speakers;
    public Collection<EventAttendee> attendees;

}

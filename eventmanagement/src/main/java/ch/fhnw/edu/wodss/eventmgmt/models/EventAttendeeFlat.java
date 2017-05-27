package ch.fhnw.edu.wodss.eventmgmt.models;

import java.io.Serializable;

/**
 * "Slim version" of the event-attendees enriched by some user-properties
 *
 * Created by lukasschonbachler on 27.03.17.
 */
public class EventAttendeeFlat implements Serializable {

    /**
     * The firstname of the event-attendee
     */
    public String firstName;

    /**
     * The lastname of the event-attendee
     */
    public String lastName;

    /**
     * Indicates whether or not the attendee is internal (i.e. has an aai-login)
     */
    public boolean internal;

    /**
     * The selected food-type of the attendee (vegi, normal or none)
     */
    public String foodType;

    /**
     * Indicates whether or not the attendee selected a drink.
     */
    public boolean drink;
}

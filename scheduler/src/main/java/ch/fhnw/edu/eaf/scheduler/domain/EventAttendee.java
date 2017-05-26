package ch.fhnw.edu.eaf.scheduler.domain;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * An attendee of an event.
 */
public class EventAttendee {

    /**
     * The different sandwich-types available.
     */
    public enum FoodType {
        VEGI,
        NORMAL,
        NONE
    }

    /**
     * The id of the event-attendee.
     */
    public Long Id;

    /**
     * The event the current attendee is attending.
     */
    public Event event;

    /**
     * The user that attends the event.
     */
    public User user;

    /**
     * The foodtype chosen by the user/attendee.
     */
    public FoodType foodType;

    /**
     * Indicates whether or not an attendee wants a drink or not.
     */
    public boolean drink;
}

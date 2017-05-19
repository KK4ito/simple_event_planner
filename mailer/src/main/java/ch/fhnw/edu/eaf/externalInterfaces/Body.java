package ch.fhnw.edu.eaf.externalInterfaces;

import java.util.Collection;

/**
 * Created by apple on 19.05.17.
 */
public interface Body {

    Event getEvent();
    User getSpeaker();
    Collection<EventAttendee> getAttendees();

}

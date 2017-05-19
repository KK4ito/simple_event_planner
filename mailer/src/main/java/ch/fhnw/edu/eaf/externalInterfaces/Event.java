package ch.fhnw.edu.eaf.externalInterfaces;

import java.util.Collection;
import java.util.Date;

/**
 * Created by apple on 19.05.17.
 */
public interface Event {

    Long getId();
    Date getCreated();
    Date getUpdated();
    String getName();
    String getDescription();
    String getLocation();
    Date getStartTime();
    Date getClosingTime();
    Date getEndTime();
    Collection<User> getSpeakers();
    Collection<EventAttendee> getAttendees();

}

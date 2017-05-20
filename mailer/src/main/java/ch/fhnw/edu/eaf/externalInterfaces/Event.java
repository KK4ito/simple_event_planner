package ch.fhnw.edu.eaf.externalInterfaces;

import java.util.Collection;
import java.util.Date;

/**
 * Created by apple on 19.05.17.
 */
public class Event {

    public Long id;
    public Date created;
    public Date updated;
    public String name;
    public String description;
    public String location;
    public Date startTime;
    public Date closingTime;
    public Date endTime;
    public Collection<User> speakers;
    public Collection<EventAttendee> attendees;

}

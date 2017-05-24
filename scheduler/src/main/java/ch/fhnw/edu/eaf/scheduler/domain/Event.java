package ch.fhnw.edu.eaf.scheduler.domain;

import java.util.Collection;
import java.util.Date;

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
    public boolean closingMailSend;
    public File image;
    public Collection<File> files;
    public Collection<User> speakers;
    public Collection<EventAttendee> attendees;

}

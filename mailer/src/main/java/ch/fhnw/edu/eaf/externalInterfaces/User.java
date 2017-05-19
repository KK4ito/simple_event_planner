package ch.fhnw.edu.eaf.externalInterfaces;

import java.util.Collection;

/**
 * Created by apple on 19.05.17.
 */
public interface User {

    Long getId();
    String getLastName();
    String getFirstName();
    String getEmail();
    boolean getInternal();
    RoleType getRole();
    Collection<EventAttendee> getAttends();

}

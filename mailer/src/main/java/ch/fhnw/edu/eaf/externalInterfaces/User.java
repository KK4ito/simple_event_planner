package ch.fhnw.edu.eaf.externalInterfaces;

import java.util.Collection;

/**
 * Created by apple on 19.05.17.
 */
public class User {

    public Long id;
    public String lastName;
    public String firstName;
    public String email;
    public boolean internal;
    public RoleType role;
    public Collection<EventAttendee> attends;

}

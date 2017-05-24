package ch.fhnw.edu.eaf.scheduler.domain;

import java.util.Collection;


public class User {

    public Long Id;
    public String lastName;
    public String firstName;
    public String email;
    public boolean internal;
    public boolean optOut;
    private String password;
    public RoleType role;

    public enum RoleType {
        ANONYMOUS,
        REGISTERED,
        ADMINISTRATOR
    }
    public File image;
    public Collection<EventAttendee> attends;
}

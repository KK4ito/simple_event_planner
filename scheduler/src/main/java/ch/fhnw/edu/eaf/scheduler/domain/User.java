package ch.fhnw.edu.eaf.scheduler.domain;

import java.util.Collection;

/**
 * A user-object
 */
public class User {

    /**
     * The id of a user
     */
    public Long Id;
    public String lastName;
    public String firstName;
    public String email;

    /**
     * Indicates whether a user is "internal" that is an aai-member or not.
     */
    public boolean internal;

    /**
     * Indicates whether or not the user chose to opt-out of the mail-notifications or not.
     */
    public boolean optOut;
    private String password;

    /**
     * The role of the user (see below for available role-types).
     */
    public RoleType role;

    public enum RoleType {
        ANONYMOUS,
        REGISTERED,
        ADMINISTRATOR
    }

    /**
     * The image of the user.
     */
    public File image;

    /**
     * A link to eventattendees
     */
    public Collection<EventAttendee> attends;
}

package ch.fhnw.edu.eaf.eventmgmt.domain;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue
    private Long Id;

    private String lastName;

    private String firstName;

    private String email;

    private boolean internal;

    private String password;

    public Collection<Event> getAttendees() {
        return attendees;
    }

    public void setAttendees(Collection<Event> attendees) {
        this.attendees = attendees;
    }

    /*
        @ManyToMany
        private List<Event> speakerEvents;

    */
    @ManyToMany
    @JoinTable(name = "attendees", joinColumns = @JoinColumn(name = "iduser"), inverseJoinColumns = @JoinColumn(name = "idevent"))
    private Collection<Event> attendees;


    protected User() {
    }

    public User(String lastName, String firstName, boolean internal, String password) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.internal = internal;
        this.password = password;
    }

    public Long getId() {
        return Id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String name) {
        this.lastName = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}

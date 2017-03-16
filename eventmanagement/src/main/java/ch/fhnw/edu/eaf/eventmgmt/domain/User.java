package ch.fhnw.edu.eaf.eventmgmt.domain;

import javax.persistence.*;
import java.util.Collection;

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


    @ManyToMany
    @JoinTable(name = "speakers", joinColumns = @JoinColumn(name = "iduser"), inverseJoinColumns = @JoinColumn(name = "idevent"))
    private Collection<Event> speakers;

    @ManyToMany
    @JoinTable(name = "attendees", joinColumns = @JoinColumn(name = "iduser"), inverseJoinColumns = @JoinColumn(name = "idevent"))
    private Collection<Event> attendees;

    public User() {
    }

    public Long getId() {
        return Id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public boolean isInternal() {
        return internal;
    }

    public void setInternal(boolean internal) {
        this.internal = internal;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<Event> getSpeakers() {
        return speakers;
    }

    public void setSpeakers(Collection<Event> speakers) {
        this.speakers = speakers;
    }

    public Collection<Event> getAttendees() {
        return attendees;
    }

    public void setAttendees(Collection<Event> attendees) {
        this.attendees = attendees;
    }
}

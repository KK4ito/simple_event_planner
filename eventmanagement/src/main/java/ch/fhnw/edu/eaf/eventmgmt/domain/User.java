package ch.fhnw.edu.eaf.eventmgmt.domain;

import javax.persistence.*;

@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;

    private String lastName;

    private String firstName;

    private String email;

    private boolean internal;

    private String password;

    /*
    @ManyToMany
    private List<Event> speakerEvents;

    @ManyToMany
    private List<Event> attendeeEvents;
    */

    protected User() {
    }

    public User(String lastName, String firstName, boolean internal, String password) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.internal = internal;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

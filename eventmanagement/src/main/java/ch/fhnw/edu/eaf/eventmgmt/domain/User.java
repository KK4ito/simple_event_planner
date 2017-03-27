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

    @OneToOne
    private File image;

    public User() {
    }

    @OneToMany(mappedBy = "user")
    private Collection<EventAttendee> attends;

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

    public File getImage() {
        return image;
    }

    public String getImageUri() {
        if (image == null) return "/default/avatar.png";
        // TODO Load base path from configuration
        return "/api/download/" + image.getId();
    }

    public void setImage(File image) {
        this.image = image;
    }
}

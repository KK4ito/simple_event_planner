package ch.fhnw.edu.eaf.eventmgmt.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;

@Entity
public class User {
    @Id
    @GeneratedValue
    private Long Id;

    @NotNull
    @Size(min = 1, max = 1024)
    private String lastName;

    @NotNull
    @Size(min = 1, max = 1024)
    private String firstName;

    @NotNull
    @Size(min = 5, max = 2048)
    private String email;

    private boolean internal;

    @NotNull
    @Size(min = 32, max = 1024)
    private String password;

    @Enumerated(EnumType.ORDINAL)
    private RoleType role;

    public enum RoleType {
        ANONYMOUS,
        REGISTERED,
        ADMINISTRATOR
    }

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

    public int getRole() {
        return role.ordinal();
    }

    public void setRole(RoleType role) {
        this.role = role;
    }
}

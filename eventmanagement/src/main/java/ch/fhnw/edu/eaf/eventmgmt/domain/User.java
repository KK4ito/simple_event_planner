package ch.fhnw.edu.eaf.eventmgmt.domain;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
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
    @Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
    private String email;

    private boolean internal;

    private boolean optOut;

    @NotNull
    @Size(min = 32, max = 1024)
    private String password;

    @Enumerated(EnumType.ORDINAL)
    private RoleType role;

    public boolean isOptOut() {
        return optOut;
    }

    public void setOptOut(boolean optOut) {
        this.optOut = optOut;
    }

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
        // Create instance
        Argon2 argon2 = Argon2Factory.create();

        // Read password from user
        char[] userPassword = password.toCharArray();

        try {
            // Hash password
            this.password = argon2.hash(2, 65536, 1, userPassword);
        } finally {
            // Wipe confidential data
            argon2.wipeArray(userPassword);
        }
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

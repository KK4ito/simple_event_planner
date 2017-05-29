package ch.fhnw.edu.wodss.eventmgmt.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Date;

/**
 * A user-entity.
 */
@Entity
public class User {
    /**
     * The id of the user.
     */
    @Id
    @GeneratedValue
    private Long Id;

    /**
     * The lastname of the user.
     */
    @NotNull
    @Size(min = 1, max = 1024)
    private String lastName;

    /**
     * The firstname of the user.
     */
    @NotNull
    @Size(min = 1, max = 1024)
    private String firstName;

    /**
     * The email-address of the user.
     */
    @NotNull
    @Size(max = 4096)
    @Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
    private String email;

    /**
     * Indicates whether or not the user is internal (e.g. has an aai-logon)
     */
    private boolean internal;

    /**
     * Indicates whether or not the user opted out of receiving any information (e.g. mails) from the platform.
     */
    private boolean optOut;

    /**
     * The password for the user.
     *
     * Is only set if the user has the internal-flag = false. The password is stored heavily encrypted
     * with the help of argon2 (see setter-method for this attribute).
     */
    @Size(max = 1024)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    /**
     * The role of the user.
     */
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

    public String getPasswordResetToken() {
        return passwordResetToken;
    }

    public void setPasswordResetToken(String passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
    }

    public Date getPasswordResetTokenExpirationDate() {
        return passwordResetTokenExpirationDate;
    }

    public void setPasswordResetTokenExpirationDate(Date passwordResetTokenExpirationDate) {
        this.passwordResetTokenExpirationDate = passwordResetTokenExpirationDate;
    }

    private String passwordResetToken;
    private Date passwordResetTokenExpirationDate;

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

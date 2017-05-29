package ch.fhnw.edu.wodss.eventmgmt.endpoints;

import ch.fhnw.edu.wodss.eventmgmt.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * The user-repository.
 *
 * Provides various methods (additionally to the ones provided by JpaRepository) to get and set
 * user-data.
 */
@RepositoryRestResource
@CrossOrigin
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Gets all users who attend the event with the passed id.
     *
     * @param event     The id of the event to get all users for
     * @return          Iterable of all users of the event
     */
    @Query(value = "SELECT User.* FROM User WHERE User.id IN (SELECT EVENT_ATTENDEE.user_id FROM EVENT_ATTENDEE WHERE EVENT_ATTENDEE.event_id = :event)", nativeQuery = true)
    public Iterable<User> attendees(@Param("event") int event);

    /**
     * Gets all users with the role equal to the passed role.
     * This method can only be used by administrators.
     *
     * @param role      The role to get the users for.
     * @return          Iterable of all users with the passed role equal to their role
     */
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    @Query(value = "SELECT User.* FROM User WHERE User.role = :role", nativeQuery = true)
    public Iterable<User> role(@Param("role") int role);

    /**
     * Gets the user with their firstname or lastname like the passed name.
     * This method requires administrator-authority.
     *
     * @param name      The name to look for.
     * @return          A list of all the users whose first- or lastname is like the passed name.
     */
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    @Query(value = "SELECT u FROM User AS u WHERE UPPER(u.firstName) like %:name% OR UPPER(u.lastName) like %:name% OR UPPER(u.email) like %:name%")
    public List<User> name(@Param("name") String name);

    /**
     * Finds all Users with a given email-address.
     * This method is not exported and only available internally.
     *
     * @param email     The mail-address the user should have
     * @return          List of users whose mail address is equal to the passed one.
     */
    @RestResource(exported = false)
    @Query(value = "SELECT u FROM User AS u WHERE UPPER(u.email) = UPPER(:email)")
    public List<User> findByEmail(@Param("email") String email);

    /**
     * Finds all external users (i.e. those who have no aai-logon) who did not choose
     * to "opt out" (that is to receive no more information about upcoming events).
     *
     * This method is only available internally. The calling user has to have
     * administrator-authority.
     *
     * @return          List of all external users who did not yet choose to opt out.
     */
    @RestResource(exported = false)
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    @Query(value = "SELECT u FROM User AS u WHERE u.optOut = false AND u.internal = false")
    public List<User> externalOptIn();

    /**
     * Finds all users whose passwordResetToken is equal to the one that is passed
     * to this method.
     *
     * @param token     The passwordResetToken
     * @return          List of users whose passwordResetToken is equal to the passed one.
     */
    @RestResource(exported = false)
    @Query(value = "SELECT u FROM User AS u WHERE u.passwordResetToken = :token")
    public List<User> findByToken(@Param("token") String token);

    /**
     * Updates the passwordResetToken and the passwordResetToken-Expiration-Date of the user
     * with the email-address equal to the mail-address that is passed to this method.
     *
     * @param token         The passwordResetToken to set
     * @param date          The expirationDate to set
     * @param email         The email of the respective user
     */
    @Modifying
    @Transactional
    @RestResource(exported = false)
    @Query(value = "UPDATE User AS u SET u.passwordResetToken = :token, u.passwordResetTokenExpirationDate = :date WHERE u.email = :email")
    public void updateTokenAndExpirationDateByEmail(@Param("token") String token, @Param("date") Date date, @Param("email") String email);

    /**
     * Sets the passwordResetToken and the passwordResetToken-Expiration-Date of the user with the email
     * equal to the passed mail-address to null, and sets his password to be the one that is passed
     * to this method.
     *
     * @param password      The new password to set for the user
     * @param email         The email of the user to set the password for
     */
    @Modifying
    @Transactional
    @RestResource(exported = false)
    @Query(value = "UPDATE User AS u SET u.password = :password, u.passwordResetToken = null, u.passwordResetTokenExpirationDate = null WHERE u.email = :email")
    public void setTokenAndExpirationDateAndPasswordByEmail(@Param("password") String password, @Param("email") String email);

    /**
     * Saves a user to the db.
     * The calling user has either to have authority administrator or permission "user-owner"
     *
     * @param u         The user to save
     * @return          The saved user-object
     */
    @PreAuthorize("hasAuthority('ADMINISTRATOR') or hasPermission(#u, 'USER_OWNER')")
    @Override
    public User save(User u);

    /**
     * Deletes a user.
     * The calling user has to have authority of administrator.
     *
     * @param u         The user to delete
     */
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    @Override
    public void delete(User u);
}

package ch.fhnw.edu.eaf.eventmgmt.persistence;

import ch.fhnw.edu.eaf.eventmgmt.domain.Event;
import ch.fhnw.edu.eaf.eventmgmt.domain.User;
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

@RepositoryRestResource
@CrossOrigin
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT User.* FROM User WHERE User.id IN (SELECT EVENT_ATTENDEE.user_id FROM EVENT_ATTENDEE WHERE EVENT_ATTENDEE.event_id = :event)", nativeQuery = true)
    public Iterable<User> attendees(@Param("event") int event);

    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    @Query(value = "SELECT User.* FROM User WHERE User.role = :role", nativeQuery = true)
    public Iterable<User> role(@Param("role") int role);

    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    @Query(value = "SELECT u FROM User AS u WHERE UPPER(u.firstName) like %:name% OR (u.lastName) like %:name%")
    public List<User> name(@Param("name") String name);

    @RestResource(exported = false)
    @Query(value = "SELECT u FROM User AS u WHERE UPPER(u.email) = UPPER(:email)")
    public List<User> findByEmail(@Param("email") String email);

    @RestResource(exported = false)
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    @Query(value = "SELECT u FROM User AS u WHERE u.optOut = false AND u.internal = true")
    public List<User> externalOptIn();

    @RestResource(exported = false)
    @Query(value = "SELECT u FROM User AS u WHERE u.passwordResetToken = :token")
    public List<User> findByToken(@Param("token") String token);

    @Modifying
    @Transactional
    @RestResource(exported = false)
    @Query(value = "UPDATE User AS u SET u.passwordResetToken = :token, u.passwordResetTokenExpirationDate = :date WHERE u.email = :email")
    public void updateTokenAndExpirationDateByEmail(@Param("token") String token, @Param("date") Date date, @Param("email") String email);

    @Modifying
    @Transactional
    @RestResource(exported = false)
    @Query(value = "UPDATE User AS u SET u.password = :password, u.passwordResetToken = null, u.passwordResetTokenExpirationDate = null WHERE u.email = :email")
    public void setTokenAndExpirationDateAndPasswordByEmail(@Param("password") String password, @Param("email") String email);

    @PreAuthorize("hasAuthority('ADMINISTRATOR') or hasPermission(#u, 'USER_OWNER') or hasPermission('PASSWORD_RESET')")
    @Override
    public User save(User u);

    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    @Override
    public void delete(User u);
}

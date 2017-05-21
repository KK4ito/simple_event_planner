package ch.fhnw.edu.eaf.eventmgmt.persistence;

import ch.fhnw.edu.eaf.eventmgmt.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT User.* FROM User WHERE User.id IN (SELECT EVENT_ATTENDEE.user_id FROM EVENT_ATTENDEE WHERE EVENT_ATTENDEE.event_id = :event)", nativeQuery = true)
    public Iterable<User> attendees(@Param("event") int event);

    @Query(value = "SELECT User.* FROM User WHERE User.role = :role", nativeQuery = true)
    public Iterable<User> role(@Param("role") int role);

    @Query(value = "SELECT u FROM User AS u WHERE UPPER(u.firstName) like %:name% OR (u.lastName) like %:name%")
    public List<User> name(@Param("name") String name);

    @Query(value = "SELECT u FROM User AS u WHERE UPPER(u.email) = UPPER(:email)")
    public List<User> me(@Param("email") String email);

    @Query(value = "SELECT u FROM User AS u WHERE u.optOut = false AND u.internal = true")
    public List<User> externalOptIn();
}

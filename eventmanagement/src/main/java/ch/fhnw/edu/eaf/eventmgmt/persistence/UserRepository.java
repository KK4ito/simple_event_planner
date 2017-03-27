package ch.fhnw.edu.eaf.eventmgmt.persistence;

import ch.fhnw.edu.eaf.eventmgmt.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT User.* FROM User WHERE User.id IN (SELECT EVENT_ATTENDEE.user_id FROM EVENT_ATTENDEE WHERE EVENT_ATTENDEE.event_id = :event)", nativeQuery = true)
    public Iterable<User> find(@Param("event") int event);

}

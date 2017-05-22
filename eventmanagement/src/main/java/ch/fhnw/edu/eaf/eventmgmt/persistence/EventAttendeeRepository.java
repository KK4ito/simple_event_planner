package ch.fhnw.edu.eaf.eventmgmt.persistence;

import ch.fhnw.edu.eaf.eventmgmt.domain.Event;
import ch.fhnw.edu.eaf.eventmgmt.domain.EventAttendee;
import ch.fhnw.edu.eaf.eventmgmt.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@RepositoryRestResource
//@CrossOrigin
public interface EventAttendeeRepository extends JpaRepository<EventAttendee, Long> {

    @Query(value = "SELECT ea FROM EventAttendee AS ea WHERE ea.user = :user AND ea.event = :event")
    public Iterable<EventAttendee> attends(@Param("user") User user, @Param("event") Event event);

    @RestResource(exported = false)
    @Query(value = "SELECT ea FROM EventAttendee AS ea WHERE ea.event.id = :eventId")
    public List<EventAttendee> findAllByEventId(@Param("eventId") long eventId);
}

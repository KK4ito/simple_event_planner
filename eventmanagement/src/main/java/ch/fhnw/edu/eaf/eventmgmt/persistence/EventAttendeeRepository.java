package ch.fhnw.edu.eaf.eventmgmt.persistence;

import ch.fhnw.edu.eaf.eventmgmt.domain.Event;
import ch.fhnw.edu.eaf.eventmgmt.domain.EventAttendee;
import ch.fhnw.edu.eaf.eventmgmt.domain.File;
import ch.fhnw.edu.eaf.eventmgmt.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RepositoryRestResource
public interface EventAttendeeRepository extends JpaRepository<EventAttendee, Long> {

    @Query(value = "SELECT ea FROM EventAttendee AS ea WHERE ea.user = :user AND ea.event = :event")
    public Iterable<EventAttendee> attends(@Param("user") User user, @Param("event") Event event);

    @RestResource(exported = false)
    @Query(value = "SELECT ea FROM EventAttendee AS ea WHERE ea.event.id = :eventId")
    public List<EventAttendee> findAllByEventId(@Param("eventId") long eventId);

    // Prevents POST, PUT, PATCH /eventAttendees/id
    @PreAuthorize("hasAuthority('REGISTERED') and hasPermission(#e, 'EVENTATTENDEE_OWNER')")
    @Override
    public EventAttendee save(EventAttendee e);

    // Prevents DELETE /eventAttendees/:id
    @PreAuthorize("hasAuthority('REGISTERED') and hasPermission(#e, 'EVENTATTENDEE_OWNER')")
    @Override
    public void delete(EventAttendee e);
}

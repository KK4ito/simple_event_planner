package ch.fhnw.edu.wodss.eventmgmt.endpoints;

import ch.fhnw.edu.wodss.eventmgmt.entities.Event;
import ch.fhnw.edu.wodss.eventmgmt.entities.EventAttendee;
import ch.fhnw.edu.wodss.eventmgmt.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

/**
 * The eventAttendee-repository.
 *
 * Provides various methods (additionally to the ones provided by JpaRepository) to get and set
 * eventAttendee-data.
 */
@RepositoryRestResource
public interface EventAttendeeRepository extends JpaRepository<EventAttendee, Long> {

    /**
     * Gets all eventAttendees for the passed user and events.
     *
     * @param user      The user which attended an event.
     * @param event     The event the user attended
     * @return          Iterable of all found eventAttendees.
     */
    @Query(value = "SELECT ea FROM EventAttendee AS ea WHERE ea.user = :user AND ea.event = :event")
    public Iterable<EventAttendee> attends(@Param("user") User user, @Param("event") Event event);

    /**
     * Gets all eventAttendees that attended the event with the passed id.
     *
     * @param eventId       The id of the event
     * @return              List of all eventAttendees that attended the event with the id equal to the passed one
     */
    @RestResource(exported = false)
    @Query(value = "SELECT ea FROM EventAttendee AS ea WHERE ea.event.id = :eventId")
    public List<EventAttendee> findAllByEventId(@Param("eventId") long eventId);

    /**
     * Saves an eventAttendee.
     *
     * @param e     The eventAttendee to save.
     * @return      The saved eventAttendee.
     */
    // Prevents POST, PUT, PATCH /eventAttendees/id
    @PreAuthorize("hasAuthority('REGISTERED') and hasPermission(#e, 'EVENTATTENDEE_OWNER')")
    @Override
    public EventAttendee save(EventAttendee e);

    /**
     * Deletes the passed eventAttendee from the db.
     *
     * @param e     The eventAttendee to delete.
     */
    // Prevents DELETE /eventAttendees/:id
    @PreAuthorize("hasAuthority('REGISTERED') and hasPermission(#e, 'EVENTATTENDEE_OWNER')")
    @Override
    public void delete(EventAttendee e);
}

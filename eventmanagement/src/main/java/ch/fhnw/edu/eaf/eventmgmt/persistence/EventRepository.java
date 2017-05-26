package ch.fhnw.edu.eaf.eventmgmt.persistence;

import ch.fhnw.edu.eaf.eventmgmt.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * The event-repository.
 *
 * Provides various methods (additionally to the ones provided by JpaRepository) to get and set
 * event-data.
 */
@RepositoryRestResource
public interface EventRepository extends JpaRepository<Event, Long> {

    /**
     * Gets all events that have a closing-date that lies in the past and for which no closingMail was send yet.
     * This method can only be used if the calling user has the authority of "Service".
     *
     * @return      Iterable with all found events.
     */
    @PreAuthorize("hasAuthority('SERVICE')")
    @Query(value = "SELECT * FROM EVENT WHERE CLOSING_MAIL_SEND = FALSE  AND CLOSING_TIME < NOW()", nativeQuery = true)
    public Iterable<Event> closingEvents();

    /**
     * Saves an event to the db.
     * This method can only be used by a user with authority "Administrator" or permission "Event-Owner"
     *
     * @param e         The event to save to the db.
     * @return          The saved event.
     */
    @PreAuthorize("hasAuthority('ADMINISTRATOR') or hasPermission(#e, 'EVENT_OWNER')")
    @Override
    public Event save(Event e);

    /**
     * Deletes an event from the db.
     * This method can only be called by users with authority "Administrator".
     *
     * @param e     The deleted event.
     */
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    @Override
    public void delete(Event e);

}

package ch.fhnw.edu.wodss.eventmgmt.endpoints;

import ch.fhnw.edu.wodss.eventmgmt.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.transaction.Transactional;

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
    @PreAuthorize("(hasAuthority('ADMINISTRATOR') or hasAuthority('SERVICE') or hasPermission(#e, 'EVENT_OWNER')) and hasPermission(#e, 'EVENT_BUSINESS_LOGIC')")
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

//    /**
//     * Sets the closingMailSend-Flag of an event to true.
//     *
//     * @param id   The id of the event to set the flag to true
//     */
////    @Modifying
////    @Transactional
////    @RestResource(exported = false)
////    @RestResource(path = "closeEvent")
//    @Query(value = "SELECT e FROM Event AS e WHERE e.id = 1")
////    @Query(value= "UPDATE Event AS e SET e.closingMailSend = true WHERE e.Id = 1")
//    public void closeEvent();

}

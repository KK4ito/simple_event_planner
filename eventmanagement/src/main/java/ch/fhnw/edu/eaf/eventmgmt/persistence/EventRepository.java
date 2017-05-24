package ch.fhnw.edu.eaf.eventmgmt.persistence;

import ch.fhnw.edu.eaf.eventmgmt.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;

@RepositoryRestResource
public interface EventRepository extends JpaRepository<Event, Long> {

    @PreAuthorize("hasAuthority('SERVICE')")
    @Query(value = "SELECT * FROM EVENT WHERE CLOSING_MAIL_SEND = FALSE  AND CLOSING_TIME < NOW()", nativeQuery = true)
    public Iterable<Event> closingEvents();

    @PreAuthorize("hasAuthority('ADMINISTRATOR') or hasPermission(#e, 'EVENT_OWNER')")
    @Override
    public Event save(Event e);

    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    @Override
    public void delete(Event e);


}

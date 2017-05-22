package ch.fhnw.edu.eaf.eventmgmt.persistence;

import ch.fhnw.edu.eaf.eventmgmt.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;

@RepositoryRestResource
public interface EventRepository extends JpaRepository<Event, Long> {

    // TODO set correct role
    @Query(value = "SELECT * FROM EVENT WHERE CLOSING_MAIL_SEND = FALSE  AND CLOSING_TIME < NOW()", nativeQuery = true)
    public Iterable<Event> closingEvents();

    /*
    // GET all
    @Override
    public Page<Event> findAll(Pageable pageable);

    // Get single
    @Override
    public Person findOne(String id);
    */

    @PreAuthorize("hasAuthority('REGISTERED')")
    @Override
    public Event save(Event e);

    // DELETE
    //@PreAuthorize("hasRole('ADMIN')")
    //@PreAuthorize("hasPermission(#contact, 'admin')")
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    @Override
    public void delete(Event e);


}

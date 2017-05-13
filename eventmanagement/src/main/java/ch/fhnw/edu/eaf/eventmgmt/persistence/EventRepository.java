package ch.fhnw.edu.eaf.eventmgmt.persistence;

import ch.fhnw.edu.eaf.eventmgmt.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface EventRepository extends JpaRepository<Event, Long> {

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

    // POST and PUT
    //@PreAuthorize("hasRole('ADMIN')")
    @Override
    public Event save(Event e);

    // DELETE
    //@PreAuthorize("hasRole('ADMIN')")
    //@PreAuthorize("hasPermission(#contact, 'admin')")
    @Override
    public void delete(Event e);


}

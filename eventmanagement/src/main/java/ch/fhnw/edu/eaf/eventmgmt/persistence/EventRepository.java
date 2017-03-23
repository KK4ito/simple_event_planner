package ch.fhnw.edu.eaf.eventmgmt.persistence;

import ch.fhnw.edu.eaf.eventmgmt.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;

@RepositoryRestResource
public interface EventRepository extends JpaRepository<Event, Long> {

    /*
    // GET all
    @Override
    public Page<Event> findAll(Pageable pageable);

    // Get single
    @Override
    public Person findOne(String id);
    */

    // POST and PUT
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public Event save(Event e);

    // DELETE
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void delete(Event e);


}

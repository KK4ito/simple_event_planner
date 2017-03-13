package ch.fhnw.edu.eaf.eventmgmt.persistence;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import ch.fhnw.edu.eaf.eventmgmt.domain.Event;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RestResource;

public interface EventRepository extends JpaRepository<Event, Long> {

    // Uncomment to disallow methods

    /*

    // Prevents GET /people/:id
    @Override
    @RestResource(exported = false)
    public Event findOne(Long id);

    // Prevents GET /people
    @Override
    @RestResource(exported = false)
    public Page<Event> findAll(Pageable pageable);

    // Prevents POST /people and PATCH /people/:id
    @Override
    @RestResource(exported = false)
    public Event save(Event s);

    // Prevents DELETE /people/:id
    @Override
    @RestResource(exported = false)
    public void delete(Event t);

    */
}

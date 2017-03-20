package ch.fhnw.edu.eaf.eventmgmt.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.fhnw.edu.eaf.eventmgmt.domain.Event;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface EventRepository extends JpaRepository<Event, Long> {

}

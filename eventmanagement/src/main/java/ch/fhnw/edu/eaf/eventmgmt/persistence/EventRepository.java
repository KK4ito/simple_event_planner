package ch.fhnw.edu.eaf.eventmgmt.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.fhnw.edu.eaf.eventmgmt.domain.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
	/*
	List<Event> findByLastName(String lastName);

	List<Event> findByFirstName(String firstName);

	List<Event> findByEmail(String email);
	*/
}

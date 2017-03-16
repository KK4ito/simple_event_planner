package ch.fhnw.edu.eaf.eventmgmt.persistence;

import ch.fhnw.edu.eaf.eventmgmt.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface UserRepository extends JpaRepository<User, Long> {

    /*
    @Query("SELECT u FROM User u JOIN u.attendees a WHERE a.Id = :id")
    public Iterable<User> getAttendeesByEventId(@Param("id") Long id);

	List<Event> findByLastName(String lastName);

	List<Event> findByFirstName(String firstName);

	List<Event> findByEmail(String email);
	*/
}

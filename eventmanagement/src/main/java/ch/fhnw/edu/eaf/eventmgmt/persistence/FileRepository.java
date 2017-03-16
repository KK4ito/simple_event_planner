package ch.fhnw.edu.eaf.eventmgmt.persistence;

import ch.fhnw.edu.eaf.eventmgmt.domain.Event;
import ch.fhnw.edu.eaf.eventmgmt.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface FileRepository extends JpaRepository<File, Long> {

    // Uncomment to disallow methods

    @Query("FROM File f where event = :eventId")
    public Iterable<File> getFilesByEventId(@Param("eventId") Event event);

    /*

    // Prevents GET /people/:id
    @Override
    @RestResource(exported = false)
    public File findOne(Long id);

    // Prevents GET /people
    @Override
    @RestResource(exported = false)
    public Page<File> findAll(Pageable pageable);

    // Prevents POST /people and PATCH /people/:id
    @Override
    @RestResource(exported = false)
    public File save(File s);

    // Prevents DELETE /people/:id
    @Override
    @RestResource(exported = false)
    public void delete(File t);

    */
}

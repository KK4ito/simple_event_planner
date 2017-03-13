package ch.fhnw.edu.eaf.eventmgmt.persistence;

import ch.fhnw.edu.eaf.eventmgmt.domain.Event;
import ch.fhnw.edu.eaf.eventmgmt.domain.File;
import org.apache.http.MethodNotSupportedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@RepositoryRestResource
public interface FileRepository extends JpaRepository<File, Long> {

    // Uncomment to disallow methods



    @Query("FROM File f where event = :eventId")
    public Iterable<File> getByEventId(@Param("eventId") Event event);

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

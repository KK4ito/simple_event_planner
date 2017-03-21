package ch.fhnw.edu.eaf.eventmgmt.persistence;

import ch.fhnw.edu.eaf.eventmgmt.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface CustomFileRepository extends JpaRepository<File, Long> {

    @Query(value = "SELECT * FROM file WHERE id NOT IN (SELECT files_id FROM event_files) AND id NOT IN (SELECT image_id FROM event WHERE image_id IS NOT NULL) AND id NOT IN (SELECT image_id FROM user WHERE image_id IS NOT NULL)", nativeQuery = true)
    public Iterable<File> unusedFiles();
}

package ch.fhnw.edu.eaf.eventmgmt.persistence;

import ch.fhnw.edu.eaf.eventmgmt.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * The custom-file-repository.
 *
 * Provides various methods (additionally to the ones provided by JpaRepository) to get and set
 * custom-file-data.
 */
@RepositoryRestResource
public interface CustomFileRepository extends JpaRepository<File, Long> {

    /**
     * Get all files that are not related to any events or users.
     *
     * @return      An iterable with all found unused files.
     */
    @Query(value = "SELECT * FROM file WHERE id NOT IN (SELECT files_id FROM event_files) AND id NOT IN (SELECT image_id FROM event WHERE image_id IS NOT NULL) AND id NOT IN (SELECT image_id FROM user WHERE image_id IS NOT NULL)", nativeQuery = true)
    public Iterable<File> unusedFiles();

    /**
     * Saves a file.
     *
     * @param f     The file to save
     * @return
     */
    // Prevents POST, PUT, PATCH /files/id
    @Override
    @RestResource(exported = false)
    public File save(File f);

    /**
     * Delete a file.
     *
     * @param f     The file to delete.
     */
    // Prevents DELETE /files/:id
    @Override
    @RestResource(exported = false)
    public void delete(File f);

    /**
     * Get all files.
     *
     * @return              A list of all files.
     */
    // Prevents GET /files
    @Override
    @RestResource(exported = false)
    public List<File> findAll();

    /**
     * Finds the specific file with the passed id.
     *
     * @param id        The id of the file to find.
     * @return          The found file.
     */
    // Prevents GET /files/:id
    @Override
    @RestResource(exported = false)
    public File findOne(Long id);

}

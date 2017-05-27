package ch.fhnw.edu.wodss.eventmgmt.endpoints;

import ch.fhnw.edu.wodss.eventmgmt.entities.File;
import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;

/**
 * The file-repository.
 *
 * Provides various methods (additionally to the ones provided by JpaRepository) to get and set
 * file-data.
 */
@RestController
public class FileRepository {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @PersistenceContext
    private EntityManager em;

    /**
     * Saves an uploaded file to the db.
     *
     * @param file      The file to save to the db.
     * @return          A response-entity with status 200 and the file in the body (if the file could be saved)
     * or status 400 and empty body otherwise.
     */
    @RequestMapping(value = "${spring.data.rest.basePath}/files", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    @PreAuthorize("hasAuthority('REGISTERED')")
    public ResponseEntity<File> post(@RequestParam("file") MultipartFile file) {
        try {
            File f = new File(file.getOriginalFilename(), file.getBytes(), file.getContentType());
            em.persist(f);
            return ResponseEntity.ok().body(f);
        } catch (IOException e) {
            log.error("Couldn't save file", e);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    /**
     * Gets a file with the passed id.
     *
     * @param id            The id of the file to get.
     * @return              ResponseEntity with status 200 and the file in the body, status 404 and empty body otherwise.
     */
    @RequestMapping(value = "${spring.data.rest.basePath}/download/{id:.+}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> display(@PathVariable("id") Long id) {
        File file = em.find(File.class, id);
        if (file != null) {
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"" + StringEscapeUtils.escapeJava(file.getName()) +"\"")
                    .contentType(MediaType.parseMediaType(file.getContentType()))
                    .body(file.getData());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}

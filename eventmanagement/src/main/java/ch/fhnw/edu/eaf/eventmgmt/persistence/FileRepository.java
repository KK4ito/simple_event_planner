package ch.fhnw.edu.eaf.eventmgmt.persistence;

import ch.fhnw.edu.eaf.eventmgmt.domain.File;
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

@RestController
public class FileRepository {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @PersistenceContext
    private EntityManager em;

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

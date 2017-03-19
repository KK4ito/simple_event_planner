package ch.fhnw.edu.eaf.eventmgmt.persistence;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;

@Controller
public class FileRepository {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ServletContext context;

    @Value("${upload.path}")
    private String uploadPath;

    private void checkUploadDirectory() {
        // Make sure the upload directory exists and is read & writeable
        File uploadDirectory = new File(uploadPath);
        uploadDirectory.mkdirs();
        if (!uploadDirectory.exists() || !uploadDirectory.canWrite() || !uploadDirectory.canRead()) {
            log.error("Upload directory not correctly configured: " + uploadPath);
            throw new IllegalArgumentException("Ooops, something went wrong");
        }
    }

    private String saveFile(MultipartFile file, File saveFile) {
        try {
            byte[] bytes = file.getBytes();
            BufferedOutputStream stream =
                    new BufferedOutputStream(new FileOutputStream(saveFile));
            stream.write(bytes);
            stream.close();

            log.error("File saved to " + saveFile.getAbsolutePath());
            return "{\"name\": " + JSONObject.quote(this.uploadPath + file.getOriginalFilename(), false) + "}";
        } catch (Exception e) {
            log.error("Error", e.getMessage());
            throw new IllegalArgumentException("Ooops, something went wrong");
        }
    }

    @RequestMapping(value = "${spring.data.rest.basePath}/files", method = RequestMethod.POST)
    public
    @ResponseBody
    String post(@RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {

            checkUploadDirectory();

            File saveFile = new File(new File(uploadPath) + File.separator + file.getOriginalFilename());
            if (saveFile.exists()) {
                log.warn("File already exists: " + saveFile.getAbsolutePath());
                throw new IllegalArgumentException("File already exists");
            }
            return this.saveFile(file, saveFile);
        }
        throw new IllegalArgumentException("File is empty");
    }

    @RequestMapping(value = "${spring.data.rest.basePath}/files", method = RequestMethod.PUT)
    public
    @ResponseBody
    String put(@RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {

            checkUploadDirectory();

            File saveFile = new File(new File(uploadPath) + File.separator + file.getOriginalFilename());
            if (saveFile.exists()) {
                saveFile.delete();
                return this.saveFile(file, saveFile);
            }
            throw new IllegalArgumentException("File not found");
        }
        throw new IllegalArgumentException("File is empty");
    }

    @RequestMapping(value = "${spring.data.rest.basePath}/files", method = RequestMethod.DELETE)
    public
    @ResponseBody
    String put(@RequestParam("name") String name) {

        checkUploadDirectory();

        File saveFile = new File(new File(uploadPath) + File.separator + name);
        if (saveFile.exists()) {
            saveFile.delete();
            return "{\"status\": \"OK\"}";
        }
        throw new IllegalArgumentException("File not found");

    }


}
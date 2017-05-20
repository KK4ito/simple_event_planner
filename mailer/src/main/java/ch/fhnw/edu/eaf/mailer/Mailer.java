package ch.fhnw.edu.eaf.mailer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.mail.*;
import javax.mail.internet.MimeMessage;


@RestController
public class Mailer {

    @Autowired
    private customJavaMailSender javaMailSender;

    @Value("${mail.referent.subject}")
    private String referentSubject;

    @Value("${mail.referent.text}")
    private String referentText;

    @Value("${mail.svgroup.to}")
    private String svgroupTo;

    @Value("${mail.svgroup.subject}")
    private String svgroupSubject;

    @Value("${mail.svgroup.text}")
    private String svgroupText;

    @Value("${mail.raumkoordination.to}")
    private String raumkoordinationTo;

    @Value("${mail.raumkoordination.subject}")
    private String raumkoordinationSubject;

    @Value("${mail.raumkoordination.text}")
    private String raumkoordinationText;

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @PostConstruct
    public void afterConstruct() {
        try {
            javaMailSender.sendMail("jonas.frehner@students.fhnw.ch", "jonas.frehner@students.fhnw.ch", "test", "test");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends an email.
     *
     * @param type      The type of the email.
     * @param mail      A mail object, consisting of to, cc, subject, text and parameters
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "${spring.data.rest.basePath}/send/{type:.+}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Void> post(@PathVariable("type") String type, @RequestBody() Mail mail) {
        try {
            switch (type){
                case "invitation":
                    javaMailSender.sendMail(mail.to, mail.cc, mail.subject, MailHelper.prepareText(this.referentText, mail.body));
                    break;
                case "referent":
                    javaMailSender.sendMail(mail.to, "", this.referentSubject, MailHelper.prepareText(this.referentText, mail.body));
                    break;
                case "svgroup":
                    javaMailSender.sendMail(this.svgroupTo, "", this.svgroupSubject, MailHelper.prepareText(this.svgroupText, mail.body));
                    break;
                case "raumkoordination":
                    javaMailSender.sendMail(this.raumkoordinationTo, "", this.raumkoordinationSubject, MailHelper.prepareText(this.raumkoordinationText, mail.body));
                    break;
                default:
                    log.error(this.getClass().getName(), "Sending mail failed", "Type not found");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            log.info(this.getClass().getName(), "Sending mail successfull");
        } catch (MessagingException e) {
            log.error(this.getClass().getName(), "Sending mail failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
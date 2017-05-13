package ch.fhnw.edu.eaf.mailer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Map;
import java.util.Properties;


@RestController
public class Mailer {

    @Value("${mailer.from}")
    private String from;

    @Value("${mailer.smtp.host}")
    private String smtpHost;

    @Value("${mailer.smtp.port}")
    private String smtpPort;

    @Value("${mailer.smtp.auth}")
    private String smtpAuth;

    @Value("${mailer.smtp.starttls.enable}")
    private String smtpStarttlsEnable;

    @Value("${mailer.smtp.username}")
    private String smtpUsername;

    @Value("${mailer.smtp.password}")
    private String smtpPassword;

    @Value("${mailer.svgroup.to}")
    private String svgroupTo;

    @Value("${mailer.svgroup.cc}")
    private String svgroupCc;

    @Value("${mailer.svgroup.subject}")
    private String svgroupSubject;

    @Value("${mailer.svgroup.text}")
    private String svgroupText;

    private Logger log = LoggerFactory.getLogger(this.getClass());
    private Session session;

    @PostConstruct
    public void postConstruct() {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.auth", smtpAuth);
        properties.put("mail.smtp.starttls.enable", smtpStarttlsEnable);
        properties.put("mail.smtp.port", smtpPort);

        session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(smtpUsername, smtpPassword);
                    }
                }
        );

    }

    @CrossOrigin
    @RequestMapping(value = "${spring.data.rest.basePath}/send/{type:.+}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Void> post(@PathVariable("type") String type, @RequestParam("parameters") String[] parameters) {
        try {
            switch (type){
                case "invitation":
                    this.sendMail(this.svgroupTo, this.svgroupCc, this.svgroupSubject, this.prepareText(this.svgroupText, parameters));
                    break;
                case "referent":
                    this.sendMail(this.svgroupTo, this.svgroupCc, this.svgroupSubject, this.prepareText(this.svgroupText, parameters));
                    break;
                case "svgroup":
                    this.sendMail(this.svgroupTo, this.svgroupCc, this.svgroupSubject, this.prepareText(this.svgroupText, parameters));
                    break;
                case "raumkoordination":
                    this.sendMail(this.svgroupTo, this.svgroupCc, this.svgroupSubject, this.prepareText(this.svgroupText, parameters));
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


    private void sendMail(String recipients, String cc, String subject, String message) throws MessagingException {
            MimeMessage msg = new MimeMessage(session);
            // msg.setFrom(new InternetAddress(from));
            msg.addRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
            msg.addRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
            msg.setSubject(subject);
            msg.setText(message);
            Transport.send(msg);
    }

    private String prepareText(String body, String[] parameters){
        return body;
    }


}
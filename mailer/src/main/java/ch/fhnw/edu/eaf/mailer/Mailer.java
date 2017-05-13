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

    @Value("${mail.invitation.text}")
    private String text;

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

        String[] p = new String[1];
        this.sendMail("schoenbaechler.lukas@gmail.com,jonas.frehner@students.fhnw.ch", "jonas.frehner@students.fhnw.ch", "Subject", text, p);
    }

    @CrossOrigin
    @RequestMapping(value = "${spring.data.rest.basePath}/send", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Void> post(@RequestParam("file") MultipartFile file) {

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    public void sendMail(String recipients, String cc, String subject, String message, String[] parameters) {

        try {

            MimeMessage msg = new MimeMessage(session);
            // msg.setFrom(new InternetAddress(from));
            msg.addRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
            msg.addRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
            msg.setSubject(subject, "UTF-8");
            msg.setText(message, "UTF-8");
            Transport.send(msg);

        } catch(MessagingException e) {
            e.printStackTrace();
        }

    }


}
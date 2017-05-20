package ch.fhnw.edu.eaf.mailer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stringtemplate.v4.ST;

import javax.annotation.PostConstruct;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Map;


@RestController
public class Mailer {

    @Value("${mailer.token}")
    private String token;

    @Autowired
    private JavaMailSender javaMailSender;

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

    /**
     * Sends an email.
     *
     * @param mail      A mail object, consisting of to, cc, subject, text and parameters
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "${spring.data.rest.basePath}/send/{type:.+}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Void> post(@RequestBody() Mail mail) {
        try {
            this.sendMail(mail.to, mail.cc, mail.subject, MailHelper.prepareText(mail.body, mail.parameters));
            log.info(this.getClass().getName(), "Sending mail successfull");
        } catch (MessagingException e) {
            log.error(this.getClass().getName(), "Sending mail failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);



//        try {
//            switch (type){
//                case "invitation":
//                    javaMailSender.sendMail(mail.to, mail.cc, mail.subject, MailHelper.prepareText(this.referentText, mail.body));
//                    break;
//                case "referent":
//                    javaMailSender.sendMail(mail.to, "", this.referentSubject, MailHelper.prepareText(this.referentText, mail.body));
//                    break;
//                case "svgroup":
//                    javaMailSender.sendMail(this.svgroupTo, "", this.svgroupSubject, MailHelper.prepareText(this.svgroupText, mail.body));
//                    break;
//                case "raumkoordination":
//                    javaMailSender.sendMail(this.raumkoordinationTo, "", this.raumkoordinationSubject, MailHelper.prepareText(this.raumkoordinationText, mail.body));
//                    break;
//                default:
//                    log.error(this.getClass().getName(), "Sending mail failed", "Type not found");
//                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//            }
//            log.info(this.getClass().getName(), "Sending mail successfull");
//        } catch (MessagingException e) {
//            log.error(this.getClass().getName(), "Sending mail failed", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    /**
     * Sends an email to the passed recipients.
     *
     * @param recipients    Comma-separated list of email-addresses the email is to be sent to
     * @param cc            Comma-separated list of email-addresses to be included as 'cc' in the email
     * @param subject       The subject of the email
     * @param message       The message of the email
     * @throws MessagingException
     */
    public void sendMail(String recipients, String cc, String subject, String message) throws MessagingException {

        MimeMessage mail = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(recipients);
            //Todo: Add check if stuff is set or not
//            helper.setReplyTo("");
//            helper.setFrom("");
            helper.setSubject(subject);
//            helper.setCc(cc);
            helper.setText(message);
        } catch(MessagingException e) {
            e.printStackTrace();
        }
        javaMailSender.send(mail);
    }


    /*
    private void sendMail(String recipients, String cc, String subject, String message) throws MessagingException {
            MimeMessage msg = new MimeMessage();
            // msg.setFrom(new InternetAddress(from));
            msg.addRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
            msg.addRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
            msg.setSubject(subject, "UTF-8");
            msg.setText(message, "UTF-8");
            Transport.send(msg);
    }
    */

}
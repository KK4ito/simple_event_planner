package ch.fhnw.edu.eaf.mailer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;
import org.stringtemplate.v4.ST;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import java.util.Iterator;
import java.util.Map;


@RestController
public class Mailer {

    static final long ONE_MINUTE_IN_MILLISECONDS=60000;

    @Value("${mailer.token}")
    private String token;

    @Autowired
    private JavaMailSender javaMailSender;

    private Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * Sends an email.
     *
     * @param mail      A mail object, consisting of to, cc, subject, text and parameters
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "${spring.data.rest.basePath}/send", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Void> post(@RequestBody() Mail mail) {
        try {
            this.sendMail(mail.to, mail.cc, mail.subject, this.prepareText(mail.body, mail.parameters));
            log.info(this.getClass().getName(), "Sending mail successfull");
        } catch (MessagingException e) {
            log.error(this.getClass().getName(), "Sending mail failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);
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


    public String prepareText(String body, Map<String, String> parameters){
        ST template = new ST(body);

        Iterator it = parameters.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            if(Boolean.getBoolean(entry.getValue().toString())) {
                template.add(entry.getKey().toString(), Boolean.parseBoolean(entry.getValue().toString()));
            } else {
                template.add(entry.getKey().toString(), entry.getValue().toString());
            }
        }

        String text = template.render();
        return text;
    }

}
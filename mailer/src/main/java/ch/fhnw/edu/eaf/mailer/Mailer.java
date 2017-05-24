package ch.fhnw.edu.eaf.mailer;

import com.google.gson.Gson;
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

    @Value("${mailer.from}")
    private String from;

    @Value("${mailer.replyTo}")
    private String replyTo;

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
    public ResponseEntity<AnswerWrapper> post(@RequestBody() Mail mail) {
        if(!mail.token.equals(token)) {
            return new ResponseEntity<AnswerWrapper>(new AnswerWrapper("Not_Acceptable"), HttpStatus.NOT_ACCEPTABLE);
        } else {
            try {
                this.sendMail(mail.to, mail.cc, mail.subject, this.prepareText(mail.body, mail.keys, mail.values));
                log.info(this.getClass().getName(), "Sending mail successfull");
            } catch (MessagingException e) {
                log.error(this.getClass().getName(), "Sending mail failed", e);
                return new ResponseEntity<AnswerWrapper>(new AnswerWrapper("Internal_Server_Error"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<AnswerWrapper>(new AnswerWrapper("OK"), HttpStatus.OK);
        }
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
            if(recipients.length() > 0) {
                for(String singleTo: recipients.split(",")) {
                    helper.addTo(singleTo);
                }
            }
            helper.setReplyTo(replyTo);
            helper.setFrom(from);
            helper.setSubject(subject);
            if(cc.length() > 0) {
                for(String singleCc: cc.split(",")) {
                    helper.addCc(singleCc);
                }
            }
            helper.setText(message);
            javaMailSender.send(mail);
        } catch(MessagingException e) {
            System.out.println(recipients);
            System.out.println(cc);
            System.out.println(subject);
            e.printStackTrace();
        }
    }


    public String prepareText(String body, String[] keys, String[] values){
        ST template = new ST(body);

        for(int i = 0; i<keys.length; i++) {
            if(Boolean.getBoolean(values[i])) {
                template.add(keys[i], Boolean.getBoolean(values[i]));
            } else {
                template.add(keys[i], values[i]);
            }
        }

        String text = template.render();
        return text;
    }

}
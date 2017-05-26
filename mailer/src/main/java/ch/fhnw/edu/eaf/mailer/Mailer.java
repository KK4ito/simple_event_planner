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

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


/**
 * Sends mails.
 */
@RestController
public class Mailer {

    /**
     * The from-address of the mail.
     */
    @Value("${mailer.from}")
    private String from;

    /**
     * The reply-to-address of the mail.
     */
    @Value("${mailer.replyTo}")
    private String replyTo;

    /**
     * The token that all services that want to send mails has to send as well.
     */
    @Value("${mailer.token}")
    private String token;

    /**
     * The autowired javamailsender - sends the mails.
     */
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
        //Test if the sent token is equal to the defined token.
        if(!token.equals(mail.token)) {
            return new ResponseEntity<AnswerWrapper>(new AnswerWrapper("Not_Acceptable"), HttpStatus.NOT_ACCEPTABLE);
        } else {
            try {
                this.sendMail(mail.to, mail.cc, mail.subject, this.prepareText(mail.body, mail.keys, mail.values));
                log.info(this.getClass().getName(), "Sending mail successful");
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
            if(cc != null && cc.length() > 0) {
                for(String singleCc: cc.split(",")) {
                    helper.addCc(singleCc);
                }
            }
            helper.setText(message, true);
            javaMailSender.send(mail);
            log.info("Sent mail to: " + recipients + " | Cc: " + cc + " | Subject: " + subject + " | Message: " + message);
        } catch(MessagingException e) {
            log.error("FAILED sending mail to: " + recipients + " | Cc: " + cc + " | Subject: " + subject + " | Message: " + message);
            e.printStackTrace();
        }
    }

    /**
     * Parses the body and replaces all placeholders in it with the passed values.
     *
     * The placeholders have the format of $placeholder$. The keys-Array contains all the placeholders as String-names,
     * the values contain the values with which the placeholders are to be replaced.
     *
     * IMPORTANT: keys- and values-Array have to be in the same order! keys[i] will be replaced by values[i]
     *
     * @param body       The mail-body containing placeholders in the format $placeholder$
     * @param keys       The names of the placeholders.
     * @param values     The values to replace the placeholders with.
     * @return
     */
    public String prepareText(String body, String[] keys, String[] values){
        ST template = new ST(body, '$', '$');

        log.info("Replacing text: " + body + " | Keys: " + keys.toString() + " | Values: " + values.toString());

        for(int i = 0; i<keys.length; i++) {
            //Booleans need to be boolean, so we need to parse the string as a boolean
            if("true".equalsIgnoreCase(values[i])) {
                template.add(keys[i], true);
            } else if("false".equalsIgnoreCase(values[i])) {
                template.add(keys[i], false);
            } else {
                template.add(keys[i], values[i]);
            }
        }

        String text = template.render();
        return text;
    }

}
package ch.fhnw.edu.eaf.mailer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class customJavaMailSender extends JavaMailSenderImpl {

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

        MimeMessage mail = createMimeMessage();
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
        send(mail);
    }
}

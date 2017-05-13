package ch.fhnw.edu.eaf.scheduler;

import org.springframework.beans.factory.annotation.Value;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


/**
 *
 */
public class Mailer {

    @Value("${mailer.from}")
    private String from;

    @Value("${mailer.server}")
    private String server;

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


    private static Mailer instance;

    private final Session session;

    private Mailer() {
        Properties properties = System.getProperties();

        properties.put("mail.smtp.auth", smtpAuth);
        properties.put("mail.smtp.starttls.enable", smtpStarttlsEnable);
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", smtpPort);

        session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(smtpUsername, smtpPassword);
                    }
                }
        );
    }

    public static Mailer getInstance() {
        if(instance == null) {
            instance = new Mailer();
        }
        return instance;
    }

    public void sendMail(String recipients, String cc, String subject, String message) {

        try {

            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            msg.addRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
            msg.addRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
            msg.setSubject(subject);
            msg.setText(message);
            Transport.send(msg);

        } catch(MessagingException e) {
            e.printStackTrace();
        }

    }

}

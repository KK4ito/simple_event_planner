package ch.fhnw.edu.wodss.eventmgmt.endpoints;

import ch.fhnw.edu.wodss.eventmgmt.models.AnswerWrapper;
import ch.fhnw.edu.wodss.eventmgmt.entities.Event;
import ch.fhnw.edu.wodss.eventmgmt.models.Mail;
import ch.fhnw.edu.wodss.eventmgmt.entities.User;
import org.pac4j.core.profile.CommonProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * The endpoint for sending mails.
 */
@RestController
public class MailerRepository {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private EventAttendeeRepository eventAttendeeRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * The baseurl to build urls connecting to events
     */
    @Value("${mailer.eventsBaseUrl}")
    private String eventsBaseUrl;

    /**
     * The token for the mail-microservice. Without this token
     * we cannot send any mails.
     */
    @Value("${mailer.token}")
    private String token;

    /**
     * INVITATION
     * Different attributes to send invitation-emails
     */
    @Value("${mail.invitation.to}")
    private String invitationTo;

    @Value("${mail.invitation.from}")
    private String invitationFrom;

    @Value("${mail.invitation.cc}")
    private String invitationCc;

    @Value("${mail.invitation.subject}")
    private String invitationSubject;

    @Value("${mail.invitation.text}")
    private String invitationText;

    /**
     * The logical name of the mailer
     */
    @Value("${microservices.mailer}")
    private String microservicesMailer;

    /**
     * The base path to reach the api of the mailer.
     */
    @Value("${microservices.mailer.basePath}")
    private String mailerBasePath;

    @Autowired
    private HttpServletRequest context;

    /**
     * Sends the passed mail.
     *
     * @param mail      The mail-object containing the recipients, cc, subject, body, keys and values.
     * @return
     */
    @RequestMapping(value="/api/mail", method= RequestMethod.POST)
    public ResponseEntity<AnswerWrapper> sendInvitation(@RequestBody Mail mail) {

        long eventId = mail.eventId;

        Event event = eventRepository.findOne(eventId);

        String url = "http://" + microservicesMailer + "/" + mailerBasePath + "/send";

        //Set up basic mailproperties
        Mail generatedMail = new Mail();
        generatedMail.from = invitationFrom;

        generatedMail.to = mail.to;
        generatedMail.cc = mail.cc;
        generatedMail.subject = mail.subject;
        generatedMail.body = mail.body;
        generatedMail.token = token;

        String linkUrl = eventsBaseUrl + event.getId();

        //Save necessary keys and values.
        String[] keys = new String[8];
        String[] values = new String[8];

        keys[0] = "eventDateDay";
        values[0] = this.getEventDay(event.getStartTime());

        keys[1] = "eventTime";
        values[1] = this.getEventTime(event.getStartTime());

        keys[2] = "name";
        values[2] = event.getName();

        keys[3] = "eventRoom";
        values[3] = event.getLocation();

        keys[4] = "eventDeadline";
        values[4] = this.getEventDateTime(event.getClosingTime());

        keys[5] = "eventLink";
        values[5] = linkUrl;

        keys[6] = "koordinator";

        //Get the name of the currently logged in user. This user is used to sign the mail.
        HttpSession session = context.getSession();
        CommonProfile profile = (CommonProfile) session.getAttribute("profile");
        User user = (User) profile.getAttribute("user");
        values[6] = user.getFirstName() + " " + user.getLastName();

        keys[7] = "eventDate";
        values[7] = this.getEventDate(event.getStartTime());

        generatedMail.keys = keys;
        generatedMail.values = values;

        ResponseEntity<AnswerWrapper> result = restTemplate.postForEntity(url, generatedMail, AnswerWrapper.class);
        if(result.getStatusCode() == HttpStatus.OK) {
            return new ResponseEntity<AnswerWrapper>(new AnswerWrapper("OK"), HttpStatus.OK);
        } else {
            return new ResponseEntity<AnswerWrapper>(new AnswerWrapper(result.getStatusCode().toString()), result.getStatusCode());
        }
    }

    /**
     * Endpoint to get the template for the invitation-email.
     *
     * @return  String      Mail-template for the invitation
     */
    @RequestMapping(value="/api/template", method = RequestMethod.GET)
    public ResponseEntity<Mail> getTemplate() {

        //Get all external users who did not choose to opt out of event information
        List<User> external = userRepository.externalOptIn();
        StringBuilder s = new StringBuilder();

        //Build the list of recipients (comma-separated String of email-addresses)
        for(User u: external) {
            s.append(u.getEmail());
        }

        Mail mail = new Mail();
        s.append(mail.to);
        mail.from = invitationFrom;

        mail.cc = invitationCc;
        mail.subject = invitationSubject;
        mail.body = invitationText;

        return new ResponseEntity<Mail>(mail, HttpStatus.OK);
    }

    /**
     * Helper-method to extract the date in the format of dd.MM.yyyy out of a passed Date.
     *
     * @param date      Date to process
     * @return String   The processed date in the format of 12.03.2017
     */
    public String getEventDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String eventDate = dateFormat.format(date);
        return eventDate;
    }

    /**
     * Helper-method to extract the time in the format of HH:mm out of a passed Date.
     *
     * @param date      Date to process
     * @return String   The processed time in the format of 12:45
     */
    public String getEventTime(Date date) {
        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String eventTime = timeFormat.format(date);
        return eventTime;
    }

    /**
     * Helper-method to extract the weekday out of a passed Date.
     *
     * @param date      Date to process
     * @return String   The extraced weekday e.g. "Dienstag"
     */
    public String getEventDay(Date date) {
        DateFormat dateDayFormat = new SimpleDateFormat("EEEE");
        String eventDateDay = dateDayFormat.format(date);
        return eventDateDay;
    }

    /**
     * Helper-method to extract the date and time in the format of HH:mm dd.MM.yyyy out of a passed Date.
     *
     * @param date      Date to process
     * @return String   The processed date in the format of 12:45 12.03.2017
     */
    public String getEventDateTime(Date date) {
        DateFormat dateTimeFormat = new SimpleDateFormat("HH:mm dd.MM.yyyy");
        String eventDateTime = dateTimeFormat.format(date);
        return eventDateTime;
    }


}

package ch.fhnw.edu.eaf.eventmgmt;

import ch.fhnw.edu.eaf.eventmgmt.domain.Event;
import ch.fhnw.edu.eaf.eventmgmt.domain.EventAttendee;
import ch.fhnw.edu.eaf.eventmgmt.domain.User;
import ch.fhnw.edu.eaf.eventmgmt.persistence.EventAttendeeRepository;
import ch.fhnw.edu.eaf.eventmgmt.persistence.EventRepository;
import ch.fhnw.edu.eaf.eventmgmt.persistence.UserRepository;
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
import java.util.List;
import java.util.Map;

@RestController
public class MailerController {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private EventAttendeeRepository eventAttendeeRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Value("${mailer.eventsBaseUrl}")
    private String eventsBaseUrl;

    @Value("${mailer.token}")
    private String token;

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

    @Value("${microservices.mailer}")
    private String microservicesMailer;

    @Value("${microservices.mailer.basePath}")
    private String mailerBasePath;

    @Autowired
    private HttpServletRequest context;

    @RequestMapping(value="/api/mail", method= RequestMethod.POST)
    public ResponseEntity<AnswerWrapper> sendInvitation(@RequestBody Mail mail) {

        long eventId = mail.eventId;

        Event event = eventRepository.findOne(eventId);

        String url = "http://" + microservicesMailer + "/" + mailerBasePath + "/send";
        Mail generatedMail = new Mail();
        generatedMail.from = invitationFrom;

        generatedMail.to = mail.to;
        generatedMail.cc = mail.cc;
        generatedMail.subject = mail.subject;
        generatedMail.body = mail.body;
        generatedMail.token = token;

        String linkUrl = eventsBaseUrl + event.getId();

        String[] keys = new String[8];
        String[] values = new String[8];

        keys[0] = "eventDateDay";
        values[0] = MailHelper.getEventDay(event.getStartTime());

        keys[1] = "eventTime";
        values[1] = MailHelper.getEventTime(event.getStartTime());

        keys[2] = "name";
        values[2] = event.getName();

        keys[3] = "eventRoom";
        values[3] = event.getLocation();

        keys[4] = "eventDeadline";
        values[4] = MailHelper.getEventDateTime(event.getClosingTime());

        keys[5] = "eventLink";
        values[5] = linkUrl;

        keys[6] = "koordinator";
        HttpSession session = context.getSession();
        CommonProfile profile = (CommonProfile) session.getAttribute("profile");
        User user = (User) profile.getAttribute("user");
        values[6] = user.getFirstName() + " " + user.getLastName();

        keys[7] = "eventDate";
        values[7] = MailHelper.getEventDate(event.getStartTime());

        generatedMail.keys = keys;
        generatedMail.values = values;

        ResponseEntity<AnswerWrapper> result = restTemplate.postForEntity(url, generatedMail, AnswerWrapper.class);
        if(result.getStatusCode() == HttpStatus.OK) {
            return new ResponseEntity<AnswerWrapper>(new AnswerWrapper("OK"), HttpStatus.OK);
        } else {
            return new ResponseEntity<AnswerWrapper>(new AnswerWrapper(result.getStatusCode().toString()), result.getStatusCode());
        }
    }

    @RequestMapping(value="/api/template", method = RequestMethod.GET)
    public ResponseEntity<Mail> getTemplate() {

        List<User> external = userRepository.externalOptIn();
        StringBuilder s = new StringBuilder();
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


}

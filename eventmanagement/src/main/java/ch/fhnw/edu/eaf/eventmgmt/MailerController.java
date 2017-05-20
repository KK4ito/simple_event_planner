package ch.fhnw.edu.eaf.eventmgmt;

import ch.fhnw.edu.eaf.eventmgmt.domain.Event;
import ch.fhnw.edu.eaf.eventmgmt.domain.EventAttendee;
import ch.fhnw.edu.eaf.eventmgmt.domain.User;
import ch.fhnw.edu.eaf.eventmgmt.persistence.EventAttendeeRepository;
import ch.fhnw.edu.eaf.eventmgmt.persistence.EventRepository;
import ch.fhnw.edu.eaf.eventmgmt.persistence.UserRepository;
import jdk.nashorn.internal.ir.RuntimeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
public class MailerController {

    @Autowired
    private EventAttendeeRepository eventAttendeeRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

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

    @RequestMapping(value="/mail", method= RequestMethod.POST)
    public ResponseEntity<String> sendInvitation(@RequestBody long eventId) {


        List<EventAttendee> eventAttendees = eventAttendeeRepository.findAllByEventId(eventId);
        Event event = eventRepository.findOne(eventId);

        Map<String, String> params = MailHelper.getParamsForInvitation(event, eventAttendees);

        String url = "";
        Mail mail = new Mail();
        mail.from = invitationFrom;
        mail.to = invitationTo;
        mail.cc = invitationCc;
        mail.subject = invitationSubject;
        mail.body = invitationText;
        mail.parameters = params;

        String result = restTemplate.postForObject(url, mail, String.class);

        return new ResponseEntity<String>("OK", HttpStatus.OK);
    }

    @RequestMapping(value="/template", method = RequestMethod.GET)
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

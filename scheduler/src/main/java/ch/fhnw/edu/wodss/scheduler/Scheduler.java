package ch.fhnw.edu.wodss.scheduler;

import ch.fhnw.edu.wodss.scheduler.domain.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Responsible for scheduling different tasks.
 *
 * Created by lukasschonbachler on 21.03.17.
 */
@Component
public class Scheduler {

    static final long ONE_MINUTE_IN_MILLISECONDS=60000;

    @Autowired
    RestTemplate restTemplate;

    /**
     * The email of the internal service-user used by the microservice.
     */
    @Value("${microservices.service.user}")
    private String serviceEmail;

    /**
     * Password for the service-user-account.
     */
    @Value("${microservices.service.password}")
    private String servicePassword;

    /**
     * Logical name of the mailer-microservice.
     */
    @Value("${mailer.mailer}")
    private String mailer;

    /**
     * Logical name of the eventmanagement-microservice.
     */
    @Value("${mailer.eventmanagement}")
    private String eventmanagement;

    /**
     * Token for the mailer-microservice. Without it, the scheduler cannot send any mails.
     */
    @Value("${mailer.token}")
    private String token;

    /**
     * The base-url to access the events. Used in the scheduler to generate a url for the user to click on.
     */
    @Value("${mailer.eventsBaseUrl}")
    private String eventsBaseUrl;

    /**
     * Name of the "koordinator", used to sign the mails sent by the scheduler.
     */
    @Value("${mailer.koordinator}")
    private String koordinator;


    /******************************************
     * REFERENTEN-MAIL-PROPERTIES
     * Cc-Address, subject and text for the mail to the Referenten.
     ******************************************/
    @Value("${mail.referent.cc}")
    private String referentCc;

    @Value("${mail.referent.subject}")
    private String referentSubject;

    @Value("${mail.referent.text}")
    private String referentText;


    /******************************************
     * SV-GROUP-MAIL-PROPERTIES
     * To-Address, cc-Address, subject and text for the mail to the Referenten.
     ******************************************/
    @Value("${mail.svgroup.to}")
    private String svgroupTo;

    @Value("${mail.svgroup.cc}")
    private String svgroupCc;

    @Value("${mail.svgroup.subject}")
    private String svgroupSubject;

    @Value("${mail.svgroup.text}")
    private String svgroupText;

    /******************************************
     * RAUMKOORDINATION-MAIL-PROPERTIES
     * To-Address, cc-Address, subject and text for the mail to the Referenten.
     ******************************************/
    @Value("${mail.raumkoordination.to}")
    private String raumkoordinationTo;

    @Value("${mail.raumkoordination.cc}")
    private String raumkoordinationCc;

    @Value("${mail.raumkoordination.subject}")
    private String raumkoordinationSubject;

    @Value("${mail.raumkoordination.text}")
    private String raumkoordinationText;


    private Logger log = LoggerFactory.getLogger(this.getClass());

    private final RestTemplate template;

    private Scheduler() {

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                false);
        mapper.registerModule(new Jackson2HalModule());

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Arrays.asList(MediaTypes.HAL_JSON));
        converter.setObjectMapper(mapper);
        this.template = new RestTemplateBuilder().messageConverters(converter).build();
        HttpClient httpClient = HttpClients.createDefault();
        this.template.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
    }

    /**
     * Sends various mails when the "closing"-date of an event lies in the past.
     *
     * The fixed delay specifies the timeintervall in miliseconds for executing the specified task.
     */
    //@Scheduled(cron = "1 * * * *")
    @Scheduled(fixedDelay = 10000)
    public void sendMailsWhenClosingDateInPast() {

        String eventmanagementUrl = "http://" + eventmanagement + "/api/";

        //Set correct authentication-headers
        HttpEntity<?> closingEventEntity = new HttpEntity(getAuthHeaders());

        log.debug("Checking for closing events");
        //Get all events whose closing-date lies in the past
        ResponseEntity<PagedResources<Event>> eventResponseEntity = restTemplate.exchange(
                eventmanagementUrl + "events/search/closingEvents",
                HttpMethod.GET,
                closingEventEntity,
                new ParameterizedTypeReference<PagedResources<Event>>() {
                }
        );

        Collection<Event> events = eventResponseEntity.getBody().getContent();
        if(events.size() == 0) log.info("sendMailsWhenClosingDateInPast: no new events received");
        for (Event event : events) {
            //Sanity checks
            if(event == null) continue;
            try {

                //Get all eventattendees for the event
                ResponseEntity<PagedResources<EventAttendee>> eventAttendeeResponseEntity = restTemplate.exchange(
                        eventmanagementUrl + "events/" + event.id + "/attendees",
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        new ParameterizedTypeReference<PagedResources<EventAttendee>>() {
                        }
                );
                Collection<EventAttendee> eventAttendees = eventAttendeeResponseEntity.getBody().getContent();

                //Get all speakers for the event
                ResponseEntity<PagedResources<User>> speakerResponseEntity = restTemplate.exchange(
                        eventmanagementUrl + "events/" + event.id + "/speakers",
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        new ParameterizedTypeReference<PagedResources<User>>() {
                        }
                );
                Collection<User> speakers = speakerResponseEntity.getBody().getContent();

                //Send a mail to the referenten, the raumkoordination and the sv-group
                sendReferentMail(event, eventAttendees, speakers);
                sendRaumkoordinationMail(event, eventAttendees, speakers);
                sendSvGroupMail(event, eventAttendees, speakers);

                //Set a request-factory for the restTemplate
                HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
                restTemplate.setRequestFactory(requestFactory);

                //A patch-request updates all the attributes it is given. Since we only want to update the closingMailSend-flag
                //we create a wrapper (that is in this class as a private class) and pass this in the patch-request
//                EventWrapper ew = new EventWrapper();
//                ew.Id = event.id;

//                HttpEntity<EventWrapper> eventHttpEntity = new HttpEntity<EventWrapper>(ew, getAuthHeaders());
                HttpEntity<Long> eventHttpEntity = new HttpEntity<Long>(event.id, getAuthHeaders());

                //Update the event to denote that the mails were sent
                restTemplate.exchange(
                        eventmanagementUrl + "events/" + event.id,
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        new ParameterizedTypeReference<PagedResources<EventWrapper>>() {
                        }
//                        eventHttpEntity,
//                        EventWrapper.class
                );
                log.info("Event successful updated: " + event.id);
            } catch (RestClientException e) {
                e.printStackTrace();
                log.info("Event update failed: " + event.id + " / " + e.getLocalizedMessage());
            }
        }
    }

    /**
     * Set the correct authorization-headers so that the scheduler can access
     * protected urls on the eventmanagement-microservice.
     *
     * @return HttpHeaders      Authentication-headers
     */
    public HttpHeaders getAuthHeaders() {
        log.debug("Constructing authentication headers");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String auth = serviceEmail + ":" + servicePassword;
        byte[] encodedAuth = Base64.getEncoder().encode(
                auth.getBytes(Charset.forName("US-ASCII"))
        );
        String authHeader = "Basic " + new String(encodedAuth);
        headers.set("Authorization", authHeader);
        return headers;
    }

    /**
     * Deletes all files from the db that are not linke to an event.
     */
    //@Scheduled(fixedDelay = 5000)
    //@Scheduled(cron="*/5 * * * * MON-FRI")
    public void deleteUnusedFiles() {
        ResponseEntity<PagedResources<File>> responseEntity =
                template
                        .exchange(
                                SchedulerApplication.BASE_URL_EVENTMANAGEMENT + "api/files/search/unusedFiles/",
                                HttpMethod.GET,
                                HttpEntity.EMPTY,
                                new ParameterizedTypeReference<PagedResources<File>>() {
                                }
                        );

        for (File file : responseEntity.getBody().getContent()) {
            try {
                template.delete(SchedulerApplication.BASE_URL_EVENTMANAGEMENT + "api/files/" + file.getId());
                log.info("Deleted successful: " + file.getId() + " / " + file.getName());
            } catch (RestClientException e) {
                log.error("Delete failed:" + file.getId() + " / " + file.getName() + " / " + e.getLocalizedMessage());
            }
        }
    }


    /**
     * Sends the passed mail.
     *
     * @param mail      A mail instance
     */
    public void sendMail(Mail mail) {
        String mailerUrl = "http://" + mailer + "/api/send";
        try {
            ResponseEntity<AnswerWrapper> result = restTemplate.postForEntity(mailerUrl, mail, AnswerWrapper.class);
            if(result.getStatusCode() == HttpStatus.NOT_ACCEPTABLE) {
                log.error("Mail to " + mail.to + " was not send: Token was invalid");
            } else if(result.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
                log.error("Mail to " + mail.to + " could not be send: Internal server error on mailerservice");
            } else if(result.getStatusCode() == HttpStatus.OK){
                log.info("Mail to " + mail.to + " was send successfully.");
            };
        } catch(RestClientException e) {
            e.printStackTrace();
            log.error("Internal Server error");
        }
    }

    /**
     * Creates a mail from the passed parameters.
     *
     * @param event                 The event to send the mail for
     * @param eventAttendees        All eventAttendees that have signed up for the event
     * @param speakers              The speakers on the event
     */
    public void sendReferentMail(Event event, Collection<EventAttendee> eventAttendees, Collection<User> speakers) {
        Mail mail = new Mail();
        mail.cc = referentCc;
        mail.subject = referentSubject;
        mail.body = referentText;
        mail.token = token;

        StringBuilder tos = new StringBuilder();

        //If we have multiple speakers, add each of their email-address to the recipients
        Iterator it = speakers.iterator();
        while(it.hasNext()) {
            User s = (User)it.next();
            tos.append(s.email);
            if(it.hasNext()) {
                tos.append(",");
            }
        }

        mail.to = tos.toString();

        int numOfAttendees = eventAttendees.size();

        String url = eventsBaseUrl + event.id;

        String[] keys = new String[9];
        String[] values = new String[9];

        keys[0] = "eventDate";
        values[0] = this.getEventDate(event.startTime);

        keys[1] = "numOfAttendees";
        values[1] = Integer.toString(numOfAttendees);

        keys[2] = "eventTimeMinus15";
        values[2] = this.getEventTimeMinus15(event.startTime);

        keys[3] = "eventTime";
        values[3] = this.getEventTime(event.startTime);

        keys[4] = "eventRoom";
        values[4] = event.location;

        keys[5] = "eventLink";
        values[5] = url;

        keys[6] = "koordinator";
        values[6] = koordinator;

        keys[7] = "name";
        values[7] = event.name;

        //If only one of the speakers is external we set the internal-flag to false
        //that means they may receive some help setting up the projector etc.
        boolean internal = true;
        for(User s: speakers) {
            if(!s.internal) {
                internal = false;
            }
        }

        keys[8] = "internal";
        values[8] = Boolean.toString(internal);

        mail.keys = keys;
        mail.values = values;

        log.debug("Sending referenten mail to " + mail.to);
        this.sendMail(mail);

    }

    /**
     * Creates a mail from the passed parameters.
     *
     * @param event                 The event to send the mail for
     * @param eventAttendees        All eventAttendees that have signed up for the event
     * @param speakers              The speakers on the event
     */
    public void sendRaumkoordinationMail(Event event, Collection<EventAttendee> eventAttendees, Collection<User> speakers) {
        Mail mail = new Mail();
        mail.to = raumkoordinationTo;
        mail.cc = raumkoordinationCc;
        mail.subject = raumkoordinationSubject;
        mail.body = raumkoordinationText;
        mail.token = token;

        int numOfAttendees = eventAttendees.size();

        boolean internal = true;

        for(User s: speakers) {
            if(!s.internal) {
                internal = false;
            }
        }

        String[] keys = new String[6];
        String[] values = new String[6];

        keys[0] = "internal";
        values[0] = Boolean.toString(internal);

        keys[1] = "numOfAttendeesOver40";
        values[1] = Boolean.toString(numOfAttendees > 40);

        keys[2] = "eventTimeMinus15";
        values[2] = this.getEventTimeMinus15(event.startTime);

        keys[3] = "eventTime";
        values[3] = this.getEventTime(event.startTime);

        keys[4] = "eventRoom";
        values[4] = event.location;

        keys[5] = "koordinator";
        values[5] = koordinator;

        mail.keys = keys;
        mail.values = values;

        log.debug("Sending raumkoordination mail to " + mail.to);
        this.sendMail(mail);
    }

    /**
     * Creates a mail from the passed parameters.
     *
     * @param event                 The event to send the mail for
     * @param eventAttendees        All eventAttendees that have signed up for the event
     * @param speakers              The speakers on the event
     */
    public void sendSvGroupMail(Event event, Collection<EventAttendee> eventAttendees, Collection<User> speakers) {
        Mail mail = new Mail();
        mail.to = svgroupTo;
        mail.cc = svgroupCc;
        mail.subject = svgroupSubject;
        mail.body = svgroupText;
        mail.token = token;

        int numOfAttendees = eventAttendees.size();

        String[] keys = new String[8];
        String[] values = new String[8];

        keys[0] = "eventDate";
        values[0] = this.getEventDate(event.startTime);

        keys[1] = "numOfAttendees";
        values[1] = Integer.toString(numOfAttendees);

        keys[2] = "eventTimeMinus15";
        values[2] = this.getEventTimeMinus15(event.startTime);

        keys[3] = "eventRoom";
        values[3] = event.location;

        int numOfVegiSandwichPlus1 = 0;
        int numOfMeatSandwich = 0;
        int numOfDrinksPlus1 = 0;
        //Count the attendees and add to the vegi-/meat-sandwiches and drinks
        for(EventAttendee ea: eventAttendees) {
            if(ea.foodType == EventAttendee.FoodType.VEGI) {
                numOfVegiSandwichPlus1++;
            } else if(ea.foodType == EventAttendee.FoodType.NORMAL) {
                numOfMeatSandwich++;
            }
            if(ea.drink) {
                numOfDrinksPlus1++;
            }
        }

        //Add as many drinks and vegi-sandwiches as there are speakers
        keys[4] = "numOfDrinksPlus1";
        numOfDrinksPlus1 += speakers.size();
        values[4] = Integer.toString(numOfDrinksPlus1);

        keys[5] = "numOfVegiSandwichPlus1";
        numOfVegiSandwichPlus1 += speakers.size();
        values[5] = Integer.toString(numOfVegiSandwichPlus1);

        keys[6] = "numOfMeatSandwich";
        values[6] = Integer.toString(numOfMeatSandwich);

        keys[7] = "koordinator";
        values[7] = koordinator;

        mail.keys = keys;
        mail.values = values;

        log.debug("Sending svGroup mail to " + mail.to);
        this.sendMail(mail);
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
     * Helper-method to extract the time minus 15 Minutes in the format of HH:mm out of a passed Date.
     *
     * The time minus 15 Minutes is used to inform e.g. the raumkoordination when to help the external
     * speakers.
     *
     * @param date      Date to process
     * @return String   The processed date in the format of 12:45
     */
    public String getEventTimeMinus15(Date date) {
        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String eventTimeMinus15 = timeFormat.format(date.getTime() - (15 * ONE_MINUTE_IN_MILLISECONDS));
        return eventTimeMinus15;
    }
}

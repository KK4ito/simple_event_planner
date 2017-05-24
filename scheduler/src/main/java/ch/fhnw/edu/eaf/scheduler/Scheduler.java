package ch.fhnw.edu.eaf.scheduler;

import ch.fhnw.edu.eaf.scheduler.domain.*;
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
 * Created by lukasschonbachler on 21.03.17.
 */
@Component
public class Scheduler {

    static final long ONE_MINUTE_IN_MILLISECONDS=60000;

    @Autowired
    RestTemplate restTemplate;

    @Value("${microservices.service.user}")
    private String serviceEmail;

    @Value("${microservices.service.password}")
    private String servicePassword;

    @Value("${mailer.mailer}")
    private String mailer;

    @Value("${mailer.token}")
    private String token;

    @Value("${mailer.eventsBaseUrl}")
    private String eventsBaseUrl;

    @Value("${mailer.eventmanagement}")
    private String eventmanagement;

    @Value("${mailer.koordinator}")
    private String koordinator;

    @Value("${mail.referent.cc}")
    private String referentCc;

    @Value("${mail.referent.subject}")
    private String referentSubject;

    @Value("${mail.referent.text}")
    private String referentText;

    @Value("${mail.svgroup.to}")
    private String svgroupTo;

    @Value("${mail.svgroup.cc}")
    private String svgroupCc;

    @Value("${mail.svgroup.subject}")
    private String svgroupSubject;

    @Value("${mail.svgroup.text}")
    private String svgroupText;

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

    //@Scheduled(cron = "1 * * * *")
    @Scheduled(fixedDelay = 10000)
    public void sendMailsWhenClosingDateInPast() {

        String eventmanagementUrl = "http://" + eventmanagement + "/api/";


        ResponseEntity<PagedResources<Event>> eventResponseEntity = restTemplate.exchange(
                eventmanagementUrl + "events/search/closingEvents",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<PagedResources<Event>>() {
                }
        );

        Collection<Event> events = eventResponseEntity.getBody().getContent();
        if(events.size() == 0) log.info("sendMailsWhenClosingDateInPast: no new events received");
        for (Event event : events) {
            //Sanity checks
            if(event == null) continue;
            try {

                ResponseEntity<PagedResources<EventAttendee>> eventAttendeeResponseEntity = restTemplate.exchange(
                        eventmanagementUrl + "users/search/attendees?event=" + event.id,
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        new ParameterizedTypeReference<PagedResources<EventAttendee>>() {
                        }
                );
                Collection<EventAttendee> eventAttendees = eventAttendeeResponseEntity.getBody().getContent();

                ResponseEntity<PagedResources<User>> speakerResponseEntity = restTemplate.exchange(
                        eventmanagementUrl + "events/" + event.id + "/speakers",
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        new ParameterizedTypeReference<PagedResources<User>>() {
                        }
                );
                Collection<User> speakers = speakerResponseEntity.getBody().getContent();

                sendReferentMail(event, eventAttendees, speakers);
                sendRaumkoordinationMail(event, eventAttendees, speakers);
                sendSvGroupMail(event, eventAttendees);

                HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
                restTemplate.setRequestFactory(requestFactory);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                String auth = serviceEmail + ":" + servicePassword;
                byte[] encodedAuth = Base64.getEncoder().encode(
                        auth.getBytes(Charset.forName("US-ASCII"))
                );
                String authHeader = "Basic " + new String(encodedAuth);
                headers.set("Authorization", authHeader);
                HttpEntity<Event> eventHttpEntity = new HttpEntity<Event>(event, headers);

                event.closingMailSend = true;
                restTemplate.exchange(
                        eventmanagementUrl + "events/" + event.id,
                        HttpMethod.PATCH,
                        eventHttpEntity,
                        Event.class);
                log.info("Event successful updated: " + event.id);
            } catch (RestClientException e) {
                log.info("Event update failed: " + event.id + " / " + e.getLocalizedMessage());
            }
        }
    }

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
        }
    }

    public void sendReferentMail(Event event, Collection<EventAttendee> eventAttendees, Collection<User> speakers) {
        Mail mail = new Mail();
        mail.cc = referentCc;
        mail.subject = referentSubject;
        mail.body = referentText;
        mail.token = token;

        StringBuilder tos = new StringBuilder();

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

        String[] keys = new String[7];
        String[] values = new String[7];

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

        mail.keys = keys;
        mail.values = values;

        this.sendMail(mail);

    }


    public void sendRaumkoordinationMail(Event event, Collection<EventAttendee> eventAttendees, Collection<User> speakers) {
        Mail mail = new Mail();
        mail.to = raumkoordinationTo;
        mail.cc = raumkoordinationCc;
        mail.subject = raumkoordinationSubject;
        mail.body = raumkoordinationText;
        mail.token = token;

        int numOfAttendees = eventAttendees.size();

        Map<String, String> params = new HashMap<>();
        params.put("eventDate", this.getEventDate(event.startTime));
        params.put("numOfAttendees", Integer.toString(numOfAttendees));
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

        this.sendMail(mail);
    }


    public void sendSvGroupMail(Event event, Collection<EventAttendee> eventAttendees) {
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
        for(EventAttendee ea: eventAttendees) {
            if(ea.foodType == EventAttendee.FoodType.VEGI) numOfVegiSandwichPlus1++;
            if(ea.foodType == EventAttendee.FoodType.NORMAL) numOfMeatSandwich++;
            if(ea.drink) numOfDrinksPlus1++;
        }

        keys[4] = "numOfDrinksPlus1";
        numOfDrinksPlus1++;
        values[4] = Integer.toString(numOfDrinksPlus1);

        keys[5] = "numOfVegiSandwichPlus1";
        numOfVegiSandwichPlus1++;
        values[5] = Integer.toString(numOfVegiSandwichPlus1);

        keys[6] = "numOfMeatSandwich";
        values[6] = Integer.toString(numOfMeatSandwich);

        keys[7] = "koordinator";
        values[7] = koordinator;

        mail.keys = keys;
        mail.values = values;

        this.sendMail(mail);
    }


    public String getEventDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String eventDate = dateFormat.format(date);
        return eventDate;
    }

    public String getEventTime(Date date) {
        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String eventTime = timeFormat.format(date);
        return eventTime;
    }

    public String getEventTimeMinus15(Date date) {
        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String eventTimeMinus15 = timeFormat.format(date.getTime() - (15 * ONE_MINUTE_IN_MILLISECONDS));
        return eventTimeMinus15;
    }

    public String getdateDay(Date date) {
        DateFormat dateDayFormat = new SimpleDateFormat("E");
        String eventDateDay = dateDayFormat.format(date);
        return eventDateDay;
    }
}

package ch.fhnw.edu.eaf.scheduler;

import ch.fhnw.edu.eaf.scheduler.domain.Event;
import ch.fhnw.edu.eaf.scheduler.domain.File;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by lukasschonbachler on 21.03.17.
 */
@Component
public class Scheduler {

    /*
    @Value("${mail.referent.subject}")
    private String referentSubject;

    @Value("${mail.referent.text}")
    private String referentText;

    @Value("${mail.svgroup.to}")
    private String svgroupTo;

    @Value("${mail.svgroup.subject}")
    private String svgroupSubject;

    @Value("${mail.svgroup.text}")
    private String svgroupText;

    @Value("${mail.raumkoordination.to}")
    private String raumkoordinationTo;

    @Value("${mail.raumkoordination.subject}")
    private String raumkoordinationSubject;

    @Value("${mail.raumkoordination.text}")
    private String raumkoordinationText;

        try {
            switch (type){
                case "invitation":
                    javaMailSender.sendMail(mail.to, mail.cc, mail.subject, MailHelper.prepareText(this.referentText, mail.body));
                    break;
                case "referent":
                    javaMailSender.sendMail(mail.to, "", this.referentSubject, MailHelper.prepareText(this.referentText, mail.body));
                    break;
                case "svgroup":
                    javaMailSender.sendMail(this.svgroupTo, "", this.svgroupSubject, MailHelper.prepareText(this.svgroupText, mail.body));
                    break;
                case "raumkoordination":
                    javaMailSender.sendMail(this.raumkoordinationTo, "", this.raumkoordinationSubject, MailHelper.prepareText(this.raumkoordinationText, mail.body));
                    break;
                default:
                    log.error(this.getClass().getName(), "Sending mail failed", "Type not found");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            log.info(this.getClass().getName(), "Sending mail successfull");
        } catch (MessagingException e) {
            log.error(this.getClass().getName(), "Sending mail failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);
*/

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
    @Scheduled(fixedDelay = 5000)
    public void sendMailsWhenClosingDateInPast() {
        ResponseEntity<PagedResources<Event>> responseEntity =
                template
                        .exchange(
                                SchedulerApplication.BASE_URL_EVENTMANAGEMENT + "api/events/search/closingEvents/",
                                HttpMethod.GET,
                                HttpEntity.EMPTY,
                                new ParameterizedTypeReference<PagedResources<Event>>() {
                                }
                        );

        Collection<Event> events = responseEntity.getBody().getContent();
        if(events.size() == 0) log.info("sendMailsWhenClosingDateInPast: no new events received");
        for (Event event : events) {
            try {
                event.setClosingMailSend(true);
                this.template.exchange(SchedulerApplication.BASE_URL_EVENTMANAGEMENT + "api/events/" + event.getId(), HttpMethod.PATCH, new HttpEntity<Event>(event), Event.class);
                log.info("Event successful updated: " + event.getId());
            } catch (RestClientException e) {
                log.info("Event update failed: " + event.getId() + " / " + e.getLocalizedMessage());
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
}

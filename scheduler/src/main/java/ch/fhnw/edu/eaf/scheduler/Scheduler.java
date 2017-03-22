package ch.fhnw.edu.eaf.scheduler;

import ch.fhnw.edu.eaf.scheduler.domain.File;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

/**
 * Created by lukasschonbachler on 21.03.17.
 */
@Component
public class Scheduler {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Scheduled(fixedDelay = 5000)
    //@Scheduled(cron="*/5 * * * * MON-FRI")
    public void deleteUnusedFiles() {

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                false);
        mapper.registerModule(new Jackson2HalModule());

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Arrays.asList(MediaTypes.HAL_JSON));
        converter.setObjectMapper(mapper);

        RestTemplate template = new RestTemplateBuilder().messageConverters(converter).build();

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
                template.delete( SchedulerApplication.BASE_URL_EVENTMANAGEMENT + "api/files/" + file.getId());
                log.info("Deleted successful: " + file.getId() + " / " + file.getName());
            } catch (RestClientException e) {
                log.error("Delete failed:" + file.getId() + " / " + file.getName() + " / " + e.getLocalizedMessage());
            }
        }
    }
}

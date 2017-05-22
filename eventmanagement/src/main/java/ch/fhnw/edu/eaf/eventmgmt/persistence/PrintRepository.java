package ch.fhnw.edu.eaf.eventmgmt.persistence;

import ch.fhnw.edu.eaf.eventmgmt.domain.EventAttendeeFlat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.ServletContext;
import java.util.List;

@RestController
public class PrintRepository {

    @Autowired
    ServletContext context;

    @PersistenceContext
    private EntityManager em;

    //@PreAuthorize("hasAuthority('ADMINISTRATOR')")
    @RequestMapping(value = "${spring.data.rest.basePath}/print/{id:.+}", method = RequestMethod.GET)
    public ResponseEntity<EventAttendeeFlat[]> display(@PathVariable("id") Long id) {
        Query query = em.createNativeQuery("SELECT USER.FIRST_NAME, USER.LAST_NAME, USER.INTERNAL, EVENT_ATTENDEE.FOOD_TYPE, EVENT_ATTENDEE.DRINK FROM EVENT_ATTENDEE JOIN USER ON EVENT_ATTENDEE.USER_ID = USER.ID WHERE EVENT_ATTENDEE.EVENT_ID = " + id);
        List<Object[]> result = query.getResultList();
        EventAttendeeFlat[] eventAttendeeFlats = new EventAttendeeFlat[result.size()];
        for(int i = 0; i < result.size(); i++){
            EventAttendeeFlat eventAttendeeFlat = new EventAttendeeFlat();
            eventAttendeeFlat.firstName = (String) result.get(i)[0];
            eventAttendeeFlat.lastName = (String) result.get(i)[1];
            eventAttendeeFlat.internal = (boolean) result.get(i)[2];
            eventAttendeeFlat.foodType = (String) result.get(i)[3];
            eventAttendeeFlat.drink = (boolean) result.get(i)[4];
            eventAttendeeFlats[i] = eventAttendeeFlat;
        }
        return ResponseEntity.status(HttpStatus.OK).body(eventAttendeeFlats);
    }
}

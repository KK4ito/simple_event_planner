/*
package ch.fhnw.edu.eaf.eventmanagement;

import ch.fhnw.edu.eaf.eventmgmt.domain.Event;
import ch.fhnw.edu.eaf.eventmgmt.persistence.EventAttendeeRepository;
import ch.fhnw.edu.eaf.eventmgmt.persistence.EventRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@TestPropertySource(locations="classpath:application.properties")
public class MailHelperTest {

    @Value("${mail.invitation.text}")
    private String invitationText;

    @MockBean
    EventAttendeeRepository eventAttendeeRepository;

    @MockBean
    EventRepository eventRepository;

    @Before
    public void setUp() {
        Mockito.reset(eventAttendeeRepository);
        Mockito.reset(eventRepository);
    }

    @Test
    public void testPrepareTextInvitation() throws Exception {

    }



}
*/
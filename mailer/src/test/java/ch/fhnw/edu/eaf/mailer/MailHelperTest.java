package ch.fhnw.edu.eaf.mailer;

import ch.fhnw.edu.eaf.externalInterfaces.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@WebMvcTest(MailHelper.class)
@TestPropertySource(locations="classpath:application.properties")
public class MailHelperTest {

    @MockBean
    private customJavaMailSender javaMailSenderMock;

    private Body body;
    private long date;
    private DateFormat dateFormat;
    private DateFormat timeFormat;

    @Before
    public void setUp() {
        Mockito.reset(javaMailSenderMock);

        dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        timeFormat = new SimpleDateFormat("HH:mm");

        body = new Body();
        body.speaker = new User();
        body.speaker.internal = false;

        body.event = new Event();
        date = 1000000000;
        body.event.startTime = new Date(date);
        body.event.location = "6.2A07";
        body.event.name = "Testevent";

        body.attendees = new ArrayList<EventAttendee>();
        EventAttendee ea1 = new EventAttendee();
        ea1.foodType = FoodType.NORMAL;
        EventAttendee ea2 = new EventAttendee();
        ea2.foodType = FoodType.VEGI;
        EventAttendee ea3 = new EventAttendee();
        ea3.foodType = FoodType.NORMAL;

        body.attendees.add(ea1);
        body.attendees.add(ea2);
        body.attendees.add(ea3);
    }

    @Value("${mail.svgroup.text}")
    private String svGroupText;

    @Test
    public void testPrepareTextSvGroup() throws Exception {

        long d = date - (15 * 60000);
        String dateString = dateFormat.format(date);
        String timeString = timeFormat.format(d);

        String expectedLine1 = "Am CS-Seminar vom " + dateString + " haben sich 3 Teilnehmende angemeldet.";
        String expectedLine2 = "Wir bitten Sie, um " + timeString + " im Raum " + body.event.location + " folgende Verpflegung bereitzustellen:";
        String expectedLine3 = "4 Getränke (5dl PET-Flaschen)";
        String expectedLine4 = "2 Vegi-Sandwich";
        String expectedLine5 = "2 Fleisch-Sandwich";
        String expectedLine6 = "Freundliche Grüsse";
//        String expectedLine7 = "<koordinator>";

        String emptyLine = "";

        String expected = "Am CS-Seminar vom " + dateString + " haben sich 3 Teilnehmende angemeldet.\\\n" +
                "Wir bitten Sie, um " + timeString + " im Raum " + body.event.location + " folgende Verpflegung bereitzustellen:\\\n" +
                "4 Getränke (5dl PET-Flaschen)\\\n\\\n" +
                "2 Vegi-Sandwich\\\n" +
                "2 Fleisch-Sandwich\\\n" +
                "Freundliche Grüsse\\\n" +
                "<koordinator>";

        String result = MailHelper.prepareText(svGroupText, body);
        String[] r = result.split("\n");

        assertEquals(expectedLine1, r[0]);
        assertEquals(emptyLine, r[1]);
        assertEquals(expectedLine2, r[2]);
        assertEquals(emptyLine, r[3]);
        assertEquals(expectedLine3, r[4]);
        assertEquals(expectedLine4, r[5]);
        assertEquals(expectedLine5, r[6]);
        assertEquals(emptyLine, r[7]);
        assertEquals(expectedLine6, r[8]);
//        assertEquals(emptyLine, r[9]);
//        assertEquals(expectedLine7, r[10]);

    }
}

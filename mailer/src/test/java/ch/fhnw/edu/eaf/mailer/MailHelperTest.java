package ch.fhnw.edu.eaf.mailer;

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
/*
    @MockBean
    private customJavaMailSender javaMailSenderMock;


    @Value("${mail.svgroup.text}")
    private String svGroupText;

    @Value("${mail.invitation.text}")
    private String invitationText;

    @Value("${mail.referent.text}")
    private String referentText;

    @Value("${mail.raumkoordination.text}")
    private String raumkoordinationText;

    private Body bodyExternalSpeakerUnder40;
    private Body bodyInternalSpeakerUnder40;
    private int numOfVegiUnder40;
    private int numOfNormalUnder40;

    private Body bodyExternalSpeakerOver40;
    private Body bodyInternalSpeakerOver40;
    private int numOfVegiOver40;
    private int numOfNormalOver40;

    private int numOfDrinksUnder40;
    private int numOfDrinksOver40;

    private long date;
    private DateFormat dateFormat;
    private DateFormat timeFormat;
    private DateFormat dateTimeFormat;

    @Before
    public void setUp() {
        Mockito.reset(javaMailSenderMock);

        numOfDrinksUnder40 = 0;
        numOfDrinksOver40 = 0;
        numOfVegiUnder40 = 0;
        numOfVegiOver40 = 0;
        numOfNormalUnder40 = 0;
        numOfNormalOver40 = 0;

        dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        timeFormat = new SimpleDateFormat("HH:mm");
        dateTimeFormat = new SimpleDateFormat("HH:mm dd.MM.yyyy");

        bodyExternalSpeakerUnder40 = new Body();
        bodyExternalSpeakerUnder40.speaker = new User();
        bodyExternalSpeakerUnder40.speaker.internal = false;

        bodyExternalSpeakerOver40 = new Body();
        bodyExternalSpeakerOver40.speaker = new User();
        bodyExternalSpeakerOver40.speaker.internal = false;

        bodyInternalSpeakerUnder40 = new Body();
        bodyInternalSpeakerUnder40.speaker = new User();
        bodyInternalSpeakerUnder40.speaker.internal = true;

        bodyInternalSpeakerOver40 = new Body();
        bodyInternalSpeakerOver40.speaker = new User();
        bodyInternalSpeakerOver40.speaker.internal = true;

        Event event = new Event();
        date = 1000000000;
        event.startTime = new Date(date);
        event.closingTime = new Date(date);
        event.location = "6.2A07";
        event.name = "Testevent";

        bodyExternalSpeakerUnder40.event = event;
        bodyInternalSpeakerUnder40.event = event;
        bodyExternalSpeakerOver40.event = event;
        bodyInternalSpeakerOver40.event = event;

        bodyExternalSpeakerUnder40.attendees = new ArrayList<EventAttendee>();
        bodyExternalSpeakerOver40.attendees = new ArrayList<EventAttendee>();
        bodyInternalSpeakerUnder40.attendees = new ArrayList<EventAttendee>();
        bodyInternalSpeakerOver40.attendees = new ArrayList<EventAttendee>();
        for(int i=0; i < 20; i++) {
            EventAttendee ea = new EventAttendee();
            ea.foodType = FoodType.NORMAL;
            bodyExternalSpeakerUnder40.attendees.add(ea);
            bodyExternalSpeakerOver40.attendees.add(ea);
            bodyInternalSpeakerUnder40.attendees.add(ea);
            bodyInternalSpeakerOver40.attendees.add(ea);
            numOfNormalUnder40++;
            numOfNormalOver40++;
            numOfDrinksOver40++;
            numOfDrinksUnder40++;
        }
        for(int i=0; i < 10; i++) {
            EventAttendee ea = new EventAttendee();
            ea.foodType = FoodType.VEGI;
            bodyExternalSpeakerUnder40.attendees.add(ea);
            bodyInternalSpeakerUnder40.attendees.add(ea);
            bodyExternalSpeakerOver40.attendees.add(ea);
            bodyInternalSpeakerOver40.attendees.add(ea);
            numOfVegiUnder40++;
            numOfVegiOver40++;
            numOfDrinksOver40++;
            numOfDrinksUnder40++;
        }
        for(int i=0; i < 20; i++) {
            EventAttendee ea = new EventAttendee();
            ea.foodType = FoodType.VEGI;
            bodyExternalSpeakerOver40.attendees.add(ea);
            bodyInternalSpeakerOver40.attendees.add(ea);
            numOfVegiOver40++;
            numOfDrinksOver40++;
        }
        //Add one vegi sandwich for the speaker
        numOfVegiOver40++;
        numOfVegiUnder40++;
        numOfDrinksOver40++;
        numOfDrinksUnder40++;
    }

    @Test
    public void testPrepareTextSvGroupExternalOver40() throws Exception {

        long d = date - (15 * 60000);
        String dateString = dateFormat.format(date);
        String timeString = timeFormat.format(d);
        String numOfAttendees = Integer.toString(bodyExternalSpeakerOver40.attendees.size());

        String expectedLine1 = "Am CS-Seminar vom " + dateString + " haben sich " + numOfAttendees + " Teilnehmende angemeldet.";
        String expectedLine2 = "Wir bitten Sie, um " + timeString + " im Raum " + bodyExternalSpeakerOver40.event.location + " folgende Verpflegung bereitzustellen:";
        String expectedLine3 = Integer.toString(numOfDrinksOver40) + " Getränke (5dl PET-Flaschen)";
        String expectedLine4 = Integer.toString(numOfVegiOver40) + " Vegi-Sandwich";
        String expectedLine5 = Integer.toString(numOfNormalOver40) + " Fleisch-Sandwich";
        String expectedLine6 = "Freundliche Grüsse";
//        String expectedLine7 = "<koordinator>";

        String emptyLine = "";

        String result = MailHelper.prepareText(svGroupText, bodyExternalSpeakerOver40);
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

    @Test
    public void testPrepareTextSvGroupExternalUnder40() throws Exception {

        long d = date - (15 * 60000);
        String dateString = dateFormat.format(date);
        String timeString = timeFormat.format(d);
        String numOfAttendees = Integer.toString(bodyExternalSpeakerUnder40.attendees.size());

        String expectedLine1 = "Am CS-Seminar vom " + dateString + " haben sich " + numOfAttendees + " Teilnehmende angemeldet.";
        String expectedLine2 = "Wir bitten Sie, um " + timeString + " im Raum " + bodyExternalSpeakerUnder40.event.location + " folgende Verpflegung bereitzustellen:";
        String expectedLine3 = Integer.toString(numOfDrinksUnder40) + " Getränke (5dl PET-Flaschen)";
        String expectedLine4 = Integer.toString(numOfVegiUnder40) + " Vegi-Sandwich";
        String expectedLine5 = Integer.toString(numOfNormalUnder40) + " Fleisch-Sandwich";
        String expectedLine6 = "Freundliche Grüsse";
//        String expectedLine7 = "<koordinator>";

        String emptyLine = "";

        String result = MailHelper.prepareText(svGroupText, bodyExternalSpeakerUnder40);
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

    @Test
    public void testPrepareTextInvitationExternalOver40() throws Exception {

        long d = date - (15 * 60000);
        String dateString = dateFormat.format(date);
        String timeString = timeFormat.format(date);
        String deadlineString = dateTimeFormat.format(date);
        String eventLink = "";

        String expectedLine1 = "Liebe Studierende der Informatik/iCompetence und liebe Angehörige der Informatikinstitute,";
        String expectedLine2 = "Am nächsten " + dateString + " findet um " + timeString + " das CS-Seminar zum Thema " + bodyExternalSpeakerOver40.event.name + " im " + bodyExternalSpeakerOver40.event.location + " statt.";
        String expectedLine3 = "Bitte melden Sie sich für diesen Anlass bis " + deadlineString + " über folgenden Link an: " + eventLink;
        String expectedLine4 = "Beste Grüsse";
//        String expectedLine5 = "<koordinator>";

        String emptyLine = "";

        String result = MailHelper.prepareText(invitationText, bodyExternalSpeakerOver40);
        String[] r = result.split("\n");

        assertEquals(expectedLine1, r[0]);
        assertEquals(emptyLine, r[1]);
        assertEquals(expectedLine2, r[2]);
        assertEquals(expectedLine3, r[3]);
        assertEquals(emptyLine, r[4]);
        assertEquals(expectedLine4, r[5]);
//        assertEquals(emptyLine, r[6]);
//        assertEquals(expectedLine7, r[7]);

    }

    @Test
    public void testPrepareTextInvitationExternalUnder40() throws Exception {

        String dateString = dateFormat.format(date);
        String timeString = timeFormat.format(date);
        String deadlineString = dateTimeFormat.format(date);
        String eventLink = "";
        String koordinator = "";

        String expectedLine1 = "Liebe Studierende der Informatik/iCompetence und liebe Angehörige der Informatikinstitute,";
        String expectedLine2 = "Am nächsten " + dateString + " findet um " + timeString + " das CS-Seminar zum Thema " + bodyExternalSpeakerUnder40.event.name + " im " + bodyExternalSpeakerUnder40.event.location + " statt.";
        String expectedLine3 = "Bitte melden Sie sich für diesen Anlass bis " + deadlineString + " über folgenden Link an: " + eventLink;
        String expectedLine4 = "Beste Grüsse";
//        String expectedLine5 = koordinator;

        String emptyLine = "";

        String result = MailHelper.prepareText(invitationText, bodyExternalSpeakerUnder40);
        String[] r = result.split("\n");

        assertEquals(expectedLine1, r[0]);
        assertEquals(emptyLine, r[1]);
        assertEquals(expectedLine2, r[2]);
        assertEquals(expectedLine3, r[3]);
        assertEquals(emptyLine, r[4]);
        assertEquals(expectedLine4, r[5]);
//        assertEquals(emptyLine, r[6]);
//        assertEquals(expectedLine5, r[7]);

    }

    @Test
    public void testPrepareTextReferentExternalUnder40() throws Exception {

        long d = date - (15 * 60000);
        String dateString = dateFormat.format(date);
        String timeStringMinus15 = timeFormat.format(d);
        String timeString = timeFormat.format(date);
        String numOfAttendees = Integer.toString(bodyExternalSpeakerUnder40.attendees.size());
        String eventLink = "";
        String koordinator = "";

        String expectedLine1 = "Am CS-Seminar vom " + dateString + " haben sich " + numOfAttendees + " Teilnehmende angemeldet. Das CS-Seminar findet im Raum " + bodyExternalSpeakerUnder40.event.location + " statt.";
        String expectedLine2 = "Um " + timeStringMinus15 + " Uhr ist jemand vom Hausdienst da, der Ihnen wenn nötig behilflich ist, den Beamer einzurichten.";
        String expectedLine3 = "Wir freuen uns auf Ihr Referat mit Beginn um " + timeString + " zum Thema " + bodyExternalSpeakerUnder40.event.name + ". Nach dem Vortrag bitten wir Sie, die Folien über den Link " + eventLink + " auf unsere Plattform hochzuladen.";
        String expectedLine4 = "Freundliche Grüsse";
//        String expectedLine5 = "<koordinator>";

        String emptyLine = "";

        String result = MailHelper.prepareText(referentText, bodyExternalSpeakerUnder40);
        String[] r = result.split("\n");

        assertEquals(expectedLine1, r[0]);
        assertEquals(emptyLine, r[1]);
        assertEquals(expectedLine2, r[2]);
        assertEquals(emptyLine, r[3]);
        assertEquals(expectedLine3, r[4]);
        assertEquals(emptyLine, r[5]);
        assertEquals(expectedLine4, r[6]);

//        assertEquals(emptyLine, r[7]);
//        assertEquals(expectedLine5, r[8]);

    }

    @Test
    public void testPrepareTextReferentExternalOver40() throws Exception {

        long d = date - (15 * 60000);
        String dateString = dateFormat.format(date);
        String timeStringMinus15 = timeFormat.format(d);
        String timeString = timeFormat.format(date);
        String numOfAttendees = Integer.toString(bodyExternalSpeakerOver40.attendees.size());
        String eventLink = "";
        String koordinator = "";

        String expectedLine1 = "Am CS-Seminar vom " + dateString + " haben sich " + numOfAttendees + " Teilnehmende angemeldet. Das CS-Seminar findet im Raum " + bodyExternalSpeakerOver40.event.location + " statt.";
        String expectedLine2 = "Um " + timeStringMinus15 + " Uhr ist jemand vom Hausdienst da, der Ihnen wenn nötig behilflich ist, den Beamer einzurichten.";
        String expectedLine3 = "Wir freuen uns auf Ihr Referat mit Beginn um " + timeString + " zum Thema " + bodyExternalSpeakerOver40.event.name + ". Nach dem Vortrag bitten wir Sie, die Folien über den Link " + eventLink + " auf unsere Plattform hochzuladen.";
        String expectedLine4 = "Freundliche Grüsse";
//        String expectedLine5 = "<koordinator>";

        String emptyLine = "";

        String result = MailHelper.prepareText(referentText, bodyExternalSpeakerOver40);
        String[] r = result.split("\n");

        assertEquals(expectedLine1, r[0]);
        assertEquals(emptyLine, r[1]);
        assertEquals(expectedLine2, r[2]);
        assertEquals(emptyLine, r[3]);
        assertEquals(expectedLine3, r[4]);
        assertEquals(emptyLine, r[5]);
        assertEquals(expectedLine4, r[6]);

//        assertEquals(emptyLine, r[7]);
//        assertEquals(expectedLine5, r[8]);

    }

    @Test
    public void testPrepareTextReferentInternalUnder40() throws Exception {

        long d = date - (15 * 60000);
        String dateString = dateFormat.format(date);
        String timeStringMinus15 = timeFormat.format(d);
        String timeString = timeFormat.format(date);
        String numOfAttendees = Integer.toString(bodyInternalSpeakerUnder40.attendees.size());
        String eventLink = "";
        String koordinator = "";

        String expectedLine1 = "Am CS-Seminar vom " + dateString + " haben sich " + numOfAttendees + " Teilnehmende angemeldet. Das CS-Seminar findet im Raum " + bodyInternalSpeakerUnder40.event.location + " statt.";
        String expectedLine2 = "Wir freuen uns auf Ihr Referat mit Beginn um " + timeString + " zum Thema " + bodyInternalSpeakerUnder40.event.name + ". Nach dem Vortrag bitten wir Sie, die Folien über den Link " + eventLink + " auf unsere Plattform hochzuladen.";
        String expectedLine3 = "Freundliche Grüsse";
//        String expectedLine4 = "<koordinator>";

        String emptyLine = "";

        String result = MailHelper.prepareText(referentText, bodyInternalSpeakerUnder40);
        String[] r = result.split("\n");

        assertEquals(expectedLine1, r[0]);
        assertEquals(emptyLine, r[1]);
        assertEquals(expectedLine2, r[2]);
        assertEquals(emptyLine, r[3]);
        assertEquals(expectedLine3, r[4]);

//        assertEquals(emptyLine, r[5]);
//        assertEquals(expectedLine4, r[6]);

    }

    @Test
    public void testPrepareTextReferentInternalOver40() throws Exception {

        long d = date - (15 * 60000);
        String dateString = dateFormat.format(date);
        String timeStringMinus15 = timeFormat.format(d);
        String timeString = timeFormat.format(date);
        String numOfAttendees = Integer.toString(bodyInternalSpeakerOver40.attendees.size());
        String eventLink = "";
        String koordinator = "";

        String expectedLine1 = "Am CS-Seminar vom " + dateString + " haben sich " + numOfAttendees + " Teilnehmende angemeldet. Das CS-Seminar findet im Raum " + bodyInternalSpeakerOver40.event.location + " statt.";
        String expectedLine2 = "Wir freuen uns auf Ihr Referat mit Beginn um " + timeString + " zum Thema " + bodyInternalSpeakerOver40.event.name + ". Nach dem Vortrag bitten wir Sie, die Folien über den Link " + eventLink + " auf unsere Plattform hochzuladen.";
        String expectedLine3 = "Freundliche Grüsse";
//        String expectedLine4 = "<koordinator>";

        String emptyLine = "";

        String result = MailHelper.prepareText(referentText, bodyInternalSpeakerOver40);
        String[] r = result.split("\n");

        assertEquals(expectedLine1, r[0]);
        assertEquals(emptyLine, r[1]);
        assertEquals(expectedLine2, r[2]);
        assertEquals(emptyLine, r[3]);
        assertEquals(expectedLine3, r[4]);

//        assertEquals(emptyLine, r[5]);
//        assertEquals(expectedLine4, r[6]);

    }


    @Test
    public void testPrepareTextRaumkoordinationExternalOver40() throws Exception {

        long d = date - (15 * 60000);
        String dateString = dateFormat.format(date);
        String timeStringMinus15 = timeFormat.format(d);
        String numOfAttendees = Integer.toString(bodyExternalSpeakerOver40.attendees.size());
        String eventLink = "";
        String koordinator = "";

        String expectedLine1 = "Am CS-Seminar vom " + dateString + " hat es " + numOfAttendees + " Teilnehmende.";
        String expectedLine2 = "Um " + timeStringMinus15 + " sollte jemand zur Verfügung stehen, um dem externen Referenten beim Bedienen des Beamers behilflich zu sein.";
        String expectedLine3 = "Bitte Palette mit genügend Stühlen im Gang bereitstellen.";
        String expectedLine4 = "Freundliche Grüsse";
//        String expectedLine5 = "<koordinator>";

        String emptyLine = "";

        String result = MailHelper.prepareText(raumkoordinationText, bodyExternalSpeakerOver40);
        String[] r = result.split("\n");

        assertEquals(expectedLine1, r[0]);
        assertEquals(emptyLine, r[1]);
        assertEquals(expectedLine2, r[2]);
        assertEquals(emptyLine, r[3]);
        assertEquals(expectedLine3, r[4]);
        assertEquals(emptyLine, r[5]);
        assertEquals(expectedLine4, r[6]);

//        assertEquals(emptyLine, r[7]);
//        assertEquals(expectedLine5, r[8]);

    }

    @Test
    public void testPrepareTextRaumkoordinationExternalUnder40() throws Exception {

        long d = date - (15 * 60000);
        String dateString = dateFormat.format(date);
        String timeStringMinus15 = timeFormat.format(d);
        String numOfAttendees = Integer.toString(bodyExternalSpeakerUnder40.attendees.size());
        String eventLink = "";
        String koordinator = "";

        String expectedLine1 = "Am CS-Seminar vom " + dateString + " hat es " + numOfAttendees + " Teilnehmende.";
        String expectedLine2 = "Um " + timeStringMinus15 + " sollte jemand zur Verfügung stehen, um dem externen Referenten beim Bedienen des Beamers behilflich zu sein.";
        String expectedLine3 = "Freundliche Grüsse";
//        String expectedLine4 = "<koordinator>";

        String emptyLine = "";

        String result = MailHelper.prepareText(raumkoordinationText, bodyExternalSpeakerUnder40);
        String[] r = result.split("\n");

        assertEquals(expectedLine1, r[0]);
        assertEquals(emptyLine, r[1]);
        assertEquals(expectedLine2, r[2]);
        assertEquals(emptyLine, r[3]);
        assertEquals(expectedLine3, r[4]);

//        assertEquals(emptyLine, r[5]);
//        assertEquals(expectedLine4, r[6]);

    }

    @Test
    public void testPrepareTextRaumkoordinationInternalOver40() throws Exception {

        long d = date - (15 * 60000);
        String dateString = dateFormat.format(date);
        String timeStringMinus15 = timeFormat.format(d);
        String numOfAttendees = Integer.toString(bodyInternalSpeakerOver40.attendees.size());
        String eventLink = "";
        String koordinator = "";

        String expectedLine1 = "Am CS-Seminar vom " + dateString + " hat es " + numOfAttendees + " Teilnehmende.";
        String expectedLine2 = "Bitte Palette mit genügend Stühlen im Gang bereitstellen.";
        String expectedLine3 = "Freundliche Grüsse";
//        String expectedLine4 = "<koordinator>";

        String emptyLine = "";

        String result = MailHelper.prepareText(raumkoordinationText, bodyInternalSpeakerOver40);
        String[] r = result.split("\n");

        assertEquals(expectedLine1, r[0]);
        assertEquals(emptyLine, r[1]);
        assertEquals(expectedLine2, r[2]);
        assertEquals(emptyLine, r[3]);
        assertEquals(expectedLine3, r[4]);

//        assertEquals(emptyLine, r[5]);
//        assertEquals(expectedLine4, r[6]);

    }

    @Test
    public void testPrepareTextRaumkoordinationInternalUnder40() throws Exception {

        long d = date - (15 * 60000);
        String dateString = dateFormat.format(date);
        String timeStringMinus15 = timeFormat.format(d);
        String numOfAttendees = Integer.toString(bodyInternalSpeakerUnder40.attendees.size());
        String eventLink = "";
        String koordinator = "";

        String expectedLine1 = "Am CS-Seminar vom " + dateString + " hat es " + numOfAttendees + " Teilnehmende.";
        String expectedLine2 = "Freundliche Grüsse";
//        String expectedLine3 = "<koordinator>";

        String emptyLine = "";

        String result = MailHelper.prepareText(raumkoordinationText, bodyInternalSpeakerUnder40);
        String[] r = result.split("\n");

        assertEquals(expectedLine1, r[0]);
        assertEquals(emptyLine, r[1]);
        assertEquals(expectedLine2, r[2]);

//        assertEquals(emptyLine, r[3]);
//        assertEquals(expectedLine3, r[4]);

    }

*/
}

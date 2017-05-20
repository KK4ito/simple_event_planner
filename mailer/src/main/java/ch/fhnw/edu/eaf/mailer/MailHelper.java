package ch.fhnw.edu.eaf.mailer;

import ch.fhnw.edu.eaf.externalInterfaces.Body;
import ch.fhnw.edu.eaf.externalInterfaces.FoodType;
import org.stringtemplate.v4.ST;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MailHelper {

    static final long ONE_MINUTE_IN_MILLISECONDS=60000;

    /**
     * Parametrizes a given text with the passed parameters.
     * The parameters are passed in a map with the key being the placeholder in the text that is to be replaced by
     * the value.
     *
     * @param body          Text to process
     * @param data          Body-Wrapper-object containing all the data for an event
     * @return              Processed text
     */
    public static String prepareText(String body, Body data){
        ST template = new ST(body);

        Map<String, String> parameters = new HashMap<>();

        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        DateFormat dateTimeFormat = new SimpleDateFormat("HH:mm dd.MM.yyyy");
        String eventDate = dateFormat.format(data.event.startTime);
        String eventTime = "";
        String eventTimeMinus15 = "";
        String eventDeadline = "";
        Date eventDateMinus15Minutes = new Date(data.event.startTime.getTime() - (15 * ONE_MINUTE_IN_MILLISECONDS));
        eventTimeMinus15 = timeFormat.format(eventDateMinus15Minutes);
        Date eventDateTime = new Date(data.event.startTime.getTime());
        eventTime = timeFormat.format(eventDateTime);
        eventDeadline = dateTimeFormat.format(new Date(data.event.closingTime.getTime()));

        int numOfAttendees = data.attendees.size();
        String eventRoom = data.event.location;
        String name = data.event.name;

        int numOfMeatSandwich = 0;
        // Per default, the speaker receives a vegi-sandwich
        int numOfVegiSandwichPlus1 = 1;
        int numOfDrinksPlus1 = data.attendees.size() + 1;

        numOfMeatSandwich += (int)data.attendees.stream().filter(a -> a.foodType == FoodType.NORMAL).count();
        numOfVegiSandwichPlus1 += (int)data.attendees.stream().filter(a -> a.foodType == FoodType.VEGI).count();

        boolean internal = data.speaker.internal;

        //TODO: Build eventLink from eventID
        String koordinator = "";
        //TODO: get koordinator
        String eventLink = "";

        parameters.put("eventDate", eventDate);
        parameters.put("eventTimeMinus15", eventTimeMinus15);
        parameters.put("eventTime", eventTime);
        parameters.put("eventDeadline", eventDeadline);
        parameters.put("numOfAttendees", Integer.toString(numOfAttendees));
        parameters.put("eventRoom", eventRoom);
        parameters.put("name", name);
        parameters.put("numOfMeatSandwich", Integer.toString(numOfMeatSandwich));
        parameters.put("numOfVegiSandwichPlus1", Integer.toString(numOfVegiSandwichPlus1));
        parameters.put("numOfDrinksPlus1", Integer.toString(numOfDrinksPlus1));
        parameters.put("koordinator", koordinator);
        parameters.put("eventLink", eventLink);

        Iterator it = parameters.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            template.add(entry.getKey().toString(), entry.getValue());
        }
        // Add internal separately because it needs to be a boolean
        template.add("internal", internal);
        boolean numOfAttendeesOver40 = numOfAttendees > 40? true : false;
        template.add("numOfAttendeesOver40", numOfAttendeesOver40);
        String text = template.render();
        return text;
    }
}

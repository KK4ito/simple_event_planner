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

/**
 * Created by apple on 19.05.17.
 */
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
        String eventDate = dateFormat.format(data.getEvent().getStartTime());
        String eventTime = "";
        String eventTimeMinus15 = "";
        Date eventDateMinus15Minutes = new Date(data.getEvent().getStartTime().getTime() - (15 * ONE_MINUTE_IN_MILLISECONDS));
        eventTimeMinus15 = timeFormat.format(eventDateMinus15Minutes);
        Date eventDateTime = new Date(data.getEvent().getStartTime().getTime());
        eventTime = timeFormat.format(eventDateTime);

        int numOfAttendees = data.getAttendees().size();
        String eventRoom = data.getEvent().getLocation();
        String name = data.getEvent().getName();

        int numOfMeatSandwich = 0;
        // Per default, the speaker receives a vegi-sandwich
        int numOfVegiSandwichPlus1 = 1;
        int numOfDrinksPlus1 = data.getAttendees().size();

        numOfMeatSandwich += (int)data.getAttendees().stream().filter(a -> a.getFoodType() == FoodType.NORMAL).count();
        numOfVegiSandwichPlus1 += (int)data.getAttendees().stream().filter(a -> a.getFoodType() == FoodType.VEGI).count();

        boolean internal = data.getSpeaker().getInternal();

        //TODO: Build eventLink from eventID
        String koordinator = "";
        //TODO: get koordinator
        String eventLink = "";

        parameters.put("eventDate", eventDate);
        parameters.put("eventTimeMinus15", eventTimeMinus15);
        parameters.put("eventTime", eventTime);
        parameters.put("numOfAttendees", Integer.toString(numOfAttendees));
        parameters.put("eventRoom", eventRoom);
        parameters.put("name", name);
        parameters.put("numOfMeatSandwich", Integer.toString(numOfMeatSandwich));
        parameters.put("numOfVegiSandwichPlus1", Integer.toString(numOfVegiSandwichPlus1));
        parameters.put("internal", Boolean.toString(internal));
        parameters.put("koordinator", koordinator);
        parameters.put("eventLink", eventLink);

        Iterator it = parameters.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            template.add(entry.getKey().toString(), entry.getValue());
        }
        String text = template.render();
        return text;
    }
}

package ch.fhnw.edu.eaf.eventmgmt;

import ch.fhnw.edu.eaf.eventmgmt.domain.Event;
import ch.fhnw.edu.eaf.eventmgmt.domain.EventAttendee;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class MailHelper {

    public static Map<String, String> getParamsForInvitation(Event event){

        //Set various date formats
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        DateFormat dateDayFormat = new SimpleDateFormat("E");

        //Parse the needed dates
        String eventDate = dateFormat.format(event.getStartTime());
        String eventTime = timeFormat.format(event.getStartTime());
        String eventDateDay = dateDayFormat.format(event.getStartTime());

        String eventRoom = event.getLocation();
        String name = event.getName();

//        String koordinator = koordinator;
        //TODO: get koordinator
        String eventLink = "";

        Map<String, String> parameters = new HashMap<>();
        parameters.put("eventDate", eventDate);
        parameters.put("eventTime", eventTime);
        parameters.put("eventDateDay", eventDateDay);
        parameters.put("eventRoom", eventRoom);
        parameters.put("name", name);
//        parameters.put("koordinator", koordinator);
        parameters.put("eventLink", eventLink);

        return parameters;
    }
}

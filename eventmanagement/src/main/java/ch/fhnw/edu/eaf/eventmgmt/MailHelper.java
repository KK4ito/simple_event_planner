package ch.fhnw.edu.eaf.eventmgmt;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MailHelper {

    public static String getEventDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String eventDate = dateFormat.format(date);
        return eventDate;
    }

    public static String getEventTime(Date date) {
        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String eventTime = timeFormat.format(date);
        return eventTime;
    }

    public static String getEventDay(Date date) {
        DateFormat dateDayFormat = new SimpleDateFormat("EEEE");
        String eventDateDay = dateDayFormat.format(date);
        return eventDateDay;
    }

    public static String getEventDateTime(Date date) {
        DateFormat dateTimeFormat = new SimpleDateFormat("HH:mm dd.MM.yyyy");
        String eventDateTime = dateTimeFormat.format(date);
        return eventDateTime;
    }
}

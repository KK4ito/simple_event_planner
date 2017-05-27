package ch.fhnw.edu.wodss.eventmgmt.security;

import ch.fhnw.edu.wodss.eventmgmt.entities.Event;
import ch.fhnw.edu.wodss.eventmgmt.entities.EventAttendee;
import ch.fhnw.edu.wodss.eventmgmt.entities.User;
import org.pac4j.core.profile.CommonProfile;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import java.io.Serializable;
import java.util.Date;

/**
 * Determines whether or not a user has the required permissions or not for a given
 * object.
 *
 * Created by lukasschonbachler on 24.05.17.
 */
public class CustomPermissionEvaluator implements PermissionEvaluator {


    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {

        CommonProfile profile = ((CommonProfile) authentication.getPrincipal());
        String email = "";
        if(profile != null && profile.getUsername() != null) {
            email = profile.getUsername().trim();
        }

        //Various permission checks
        if ("EVENT_OWNER".equals(permission.toString())) {
            Event event = (Event) targetDomainObject;
            for (User user : event.getSpeakers()) {
                if (email.equalsIgnoreCase(user.getEmail().trim())) return true;
            }
            return false;
        }else if ("USER_OWNER".equals(permission.toString())) {
            User user = (User) targetDomainObject;
            return user.getEmail().equalsIgnoreCase(email);
        }else if ("EVENTATTENDEE_OWNER".equals(permission.toString())) {
            EventAttendee eventAttendee = (EventAttendee) targetDomainObject;
            return (eventAttendee.getUser().getEmail().equalsIgnoreCase(email));

        }else if ("EVENTATTENDEE_BUSINESS_LOGIC".equals(permission.toString())) {
            // Check if event closing time is in the future, otherwise deny
            EventAttendee eventAttendee = (EventAttendee) targetDomainObject;
            return eventAttendee.getEvent().getClosingTime().getTime() > new Date().getTime();
        }else if ("EVENT_BUSINESS_LOGIC".equals(permission.toString())) {
            // Check if event the dates are set correctly
            Event event = (Event) targetDomainObject;
            return event.getClosingTime().getTime() <= event.getStartTime().getTime() && event.getStartTime().getTime() < event.getEndTime().getTime();
        }

        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return true;
    }

}
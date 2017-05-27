package ch.fhnw.edu.eaf.eventmgmt.security;

import ch.fhnw.edu.eaf.eventmgmt.domain.Event;
import ch.fhnw.edu.eaf.eventmgmt.domain.EventAttendee;
import ch.fhnw.edu.eaf.eventmgmt.domain.User;
import org.pac4j.core.profile.CommonProfile;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import java.io.Serializable;

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
        }else if ("USER_OWNER".equals(permission.toString())) {
            User user = (User) targetDomainObject;
            if(user.getEmail().equalsIgnoreCase(email)) return true;
        }else if ("EVENTATTENDEE_OWNER".equals(permission.toString())) {
            EventAttendee eventAttendee = (EventAttendee) targetDomainObject;
            if(eventAttendee.getUser().getEmail().equalsIgnoreCase(email)) return true;
        }
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return true;
    }

}
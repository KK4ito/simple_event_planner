package ch.fhnw.edu.wodss.eventmgmt.security;

import org.pac4j.core.context.WebContext;
import org.pac4j.core.credentials.TokenCredentials;
import org.pac4j.core.credentials.authenticator.Authenticator;
import org.pac4j.core.exception.HttpAction;
import org.pac4j.core.profile.CommonProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Validates via cookie
 */
public class CookieAuthenticator implements Authenticator<TokenCredentials> {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * Validates a request of a logged in user.
     *
     * @param credentials
     * @param context
     * @throws HttpAction
     */
    @Override
    public void validate(final TokenCredentials credentials, final WebContext context) throws HttpAction {
        //Get the profile from the context.
        CommonProfile commonProfile = (CommonProfile) context.getSessionAttribute("profile");
        log.info("Validating profile " + commonProfile.toString());

        //If we found none, construct one
        if(commonProfile == null){
            commonProfile = new CommonProfile();
        }

        //Handler for the logout and reset-password-request.
        if(context.getPath().equals("/api/login/logout")){
            // Invalidate session and delete cookie
            context.setSessionAttribute("invalidated", true);
            context.setResponseHeader("Set-Cookie", "JSESSIONID=deleted; path=/; expires=Thu, 01 Jan 1970 00:00:00 GMT");
            log.info("Performing logout, deleting cookie");
        }
        credentials.setUserProfile(commonProfile);

    }
}

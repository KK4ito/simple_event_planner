package ch.fhnw.edu.eaf.eventmgmt.security;

import org.pac4j.core.context.WebContext;
import org.pac4j.core.credentials.TokenCredentials;
import org.pac4j.core.credentials.authenticator.Authenticator;
import org.pac4j.core.exception.HttpAction;
import org.pac4j.core.profile.CommonProfile;

public class CookieAuthenticator implements Authenticator<TokenCredentials> {

    @Override
    public void validate(final TokenCredentials credentials, final WebContext context) throws HttpAction {
        CommonProfile commonProfile = (CommonProfile) context.getSessionAttribute("profile");

        if(commonProfile == null){
            commonProfile = new CommonProfile();
        }

        commonProfile.removeAttribute("PASSWORD_RESET");
        if(context.getPath().equals("/api/login/logout")){
            // Invalidate session and delete cookie
            context.setSessionAttribute("invalidated", true);
            context.setResponseHeader("Set-Cookie", "JSESSIONID=deleted; path=/; expires=Thu, 01 Jan 1970 00:00:00 GMT");
        } else if(context.getPath().equals("api/requestPasswordReset")) {
            commonProfile.addPermission("PASSWORD_RESET");
        }
        credentials.setUserProfile(commonProfile);

    }
}

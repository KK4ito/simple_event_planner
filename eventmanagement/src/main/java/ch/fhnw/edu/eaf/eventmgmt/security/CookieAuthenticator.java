package ch.fhnw.edu.eaf.eventmgmt.security;

import ch.fhnw.edu.eaf.eventmgmt.domain.User;
import ch.fhnw.edu.eaf.eventmgmt.persistence.UserRepository;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.pac4j.core.context.Cookie;
import org.pac4j.core.context.Pac4jConstants;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.credentials.TokenCredentials;
import org.pac4j.core.credentials.UsernamePasswordCredentials;
import org.pac4j.core.credentials.authenticator.Authenticator;
import org.pac4j.core.exception.CredentialsException;
import org.pac4j.core.exception.HttpAction;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.util.CommonHelper;

import java.util.List;

public class CookieAuthenticator implements Authenticator<TokenCredentials> {

    CookieAuthenticator() {}

    @Override
    public void validate(final TokenCredentials credentials, final WebContext context) throws HttpAction {
        if (credentials == null) {
            throw new CredentialsException("credentials must not be null");
        }
        if (CommonHelper.isBlank(credentials.getToken())) {
            throw new CredentialsException("token must not be blank");
        }
        System.out.println("TOKEN");
        System.out.println(credentials.getToken());
        final String token = credentials.getToken();
        final CommonProfile profile = new CommonProfile(); // TODO WHAT IS THIS
        profile.setId(token);
        credentials.setUserProfile(profile);
    }

    protected void throwsException(final String message) {
        throw new CredentialsException(message);
    }
}

package ch.fhnw.edu.eaf.eventmgmt.security;

import org.pac4j.core.authorization.authorizer.*;
import org.pac4j.core.authorization.authorizer.csrf.CsrfAuthorizer;
import org.pac4j.core.authorization.authorizer.csrf.CsrfTokenGeneratorAuthorizer;
import org.pac4j.core.authorization.authorizer.csrf.DefaultCsrfTokenGenerator;
import org.pac4j.core.context.Pac4jConstants;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.exception.HttpAction;
import org.pac4j.core.profile.CommonProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lukasschonbachler on 23.03.17.
 */
public class SecureAuthorizer extends ProfileAuthorizer<CommonProfile> {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    final static XFrameOptionsHeader X_FRAME_OPTIONS_HEADER = new XFrameOptionsHeader();
    final static XSSProtectionHeader XSS_PROTECTION_HEADER = new XSSProtectionHeader();
    final static StrictTransportSecurityHeader STRICT_TRANSPORT_SECURITY_HEADER = new StrictTransportSecurityHeader();
    final static XContentTypeOptionsHeader X_CONTENT_TYPE_OPTIONS_HEADER = new XContentTypeOptionsHeader();
    final static CacheControlHeader CACHE_CONTROL_HEADER = new CacheControlHeader();

    @Override
    public boolean isAuthorized(final WebContext context, final List<CommonProfile> profiles) throws HttpAction {
        final List<Authorizer> authorizers = new ArrayList<>();

        //Set various authorizers.
        //These are basically correct headers to protect against xss etc.
        authorizers.add(CACHE_CONTROL_HEADER);
        authorizers.add(X_CONTENT_TYPE_OPTIONS_HEADER);
        authorizers.add(STRICT_TRANSPORT_SECURITY_HEADER);
        authorizers.add(X_FRAME_OPTIONS_HEADER);
        authorizers.add(XSS_PROTECTION_HEADER);

        return isAuthorized(context, profiles, authorizers);
    }

    private boolean isAuthorized(final WebContext context, final List<CommonProfile> profiles, final List<Authorizer> authorizers) throws HttpAction {
        // authorizations check comes after authentication and profile must not be null nor empty
        // check authorizations using authorizers: all must be satisfied
        for (Authorizer authorizer : authorizers) {
            final boolean isAuthorized = authorizer.isAuthorized(context, profiles);
            log.debug("Checking authorizer: {} -> {}", authorizer, isAuthorized);
            if (!isAuthorized) {
                final String parameterToken = context.getRequestParameter(Pac4jConstants.CSRF_TOKEN);
                final String headerToken = context.getRequestHeader(Pac4jConstants.CSRF_TOKEN);
                final String sessionToken = (String) context.getSessionAttribute(Pac4jConstants.CSRF_TOKEN);

                log.warn(authorizer.toString() + " denied request: pt: " + parameterToken + " / ht: " + headerToken + " / st: " +  sessionToken);
                return false;
            }
        }
        return isAnyAuthorized(context, profiles);
    }

    @Override
    public boolean isProfileAuthorized(final WebContext context, final CommonProfile profile) {
        if (profile == null) {
            log.error("Request for empty profile");
            return false;
        }

        // Check if session was invalidated -> abort
        Object attr = context.getSessionAttribute("invalidated");
        if(attr != null && (boolean) attr && !context.getPath().equals("/api/login/logout")){
            return false;
        }

        log.info("Profile found: " + profile.getUsername());
        return true;
    }
}
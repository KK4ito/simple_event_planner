package ch.fhnw.edu.eaf.eventmgmt;

import org.apache.commons.lang.StringUtils;
import org.pac4j.core.authorization.authorizer.ProfileAuthorizer;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.exception.HttpAction;
import org.pac4j.core.profile.CommonProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by lukasschonbachler on 23.03.17.
 */
public class CustomAuthorizer extends ProfileAuthorizer<CommonProfile> {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean isAuthorized(final WebContext context, final List<CommonProfile> profiles) throws HttpAction {
        log.info("isAuthorized");
        System.err.println("isAuthorized");
        return isAnyAuthorized(context, profiles);
    }

    @Override
    public boolean isProfileAuthorized(final WebContext context, final CommonProfile profile) {
        if (profile == null) {
            log.error("Profile null");
            System.err.println("Profile null");
            return false;
        }
        log.info("Profile found: " + profile.getUsername());
        System.err.println("Profile found: " + profile.getUsername());
        return StringUtils.startsWith(profile.getUsername(), "jle");
    }
}
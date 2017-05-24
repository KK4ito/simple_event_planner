package ch.fhnw.edu.eaf.eventmgmt.security;

import ch.fhnw.edu.eaf.eventmgmt.domain.User;
import ch.fhnw.edu.eaf.eventmgmt.persistence.UserRepository;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.pac4j.core.context.Pac4jConstants;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.credentials.UsernamePasswordCredentials;
import org.pac4j.core.credentials.authenticator.Authenticator;
import org.pac4j.core.exception.HttpAction;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.util.CommonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public class BasicAuthAuthenticator implements Authenticator<UsernamePasswordCredentials> {

    private final UserRepository userRepo;
    private final static Logger logger = LoggerFactory.getLogger(BasicAuthAuthenticator.class);

    private final String serviceUser;
    private final String servicePassword;

    BasicAuthAuthenticator(UserRepository userRepo, String servicesUser, String servicePassword){
        this.userRepo = userRepo;
        this.serviceUser = servicesUser;
        this.servicePassword = servicePassword;
    }

    @Override
    public void validate(UsernamePasswordCredentials credentials, final WebContext context) throws HttpAction {

        final CommonProfile profile = new CommonProfile();

        if (credentials != null) {
            String email = credentials.getUsername();
            String password = credentials.getPassword();
            if(email.equalsIgnoreCase(serviceUser) && password.equals(servicePassword)){
                profile.addAttribute(Pac4jConstants.USERNAME, serviceUser);
                profile.addRole("REGISTERED");
                profile.addRole("ADMINISTRATOR");
                profile.addRole("SERVICE");
                logger.debug(email + " received role REGISTERED");
            }else if (!CommonHelper.isBlank(email) &&  !CommonHelper.isBlank(password) && isValidPassword(email, password)) {
                profile.addAttribute(Pac4jConstants.USERNAME, email);
                List<User> users = userRepo.findByEmail(email);
                profile.addRole("REGISTERED");
                logger.debug(email + " received role REGISTERED");
                if(users.get(0).getRole() == 2) {
                    profile.addRole("ADMINISTRATOR");
                    logger.debug(email + " received role ADMINISTRATOR");
                }
                profile.addAttribute("user", users.get(0));
            }
        }

        credentials.setUserProfile(profile);
        context.setSessionAttribute("profile", profile);
    }

    protected boolean isValidPassword(String email, String password) {
        // Create instance
        Argon2 argon2 = Argon2Factory.create();

        // Read password from user
        char[] userPassword = password.toCharArray();

        try {
            List<User> users = userRepo.findByEmail(email);
            if(users.size() == 0) return false;
            User user = users.get(0);

            return argon2.verify(user.getPassword(), userPassword);
        } finally {
            // Wipe confidential data
            argon2.wipeArray(userPassword);
        }
    }
}

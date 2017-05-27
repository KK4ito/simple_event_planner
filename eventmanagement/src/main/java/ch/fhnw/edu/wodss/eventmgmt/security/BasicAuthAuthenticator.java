package ch.fhnw.edu.wodss.eventmgmt.security;

import ch.fhnw.edu.wodss.eventmgmt.entities.User;
import ch.fhnw.edu.wodss.eventmgmt.endpoints.UserRepository;
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

import java.util.List;

public class BasicAuthAuthenticator implements Authenticator<UsernamePasswordCredentials> {

    private final UserRepository userRepo;
    private final static Logger logger = LoggerFactory.getLogger(BasicAuthAuthenticator.class);

    private final String serviceUser;
    private final String servicePassword;

    /**
     * Construct the BasicAuthAuthenticator. This is done in the pac4j-Config where the necessary
     * parameters are injected.
     * Somehow, we can't reach the application.properties-file from here (via @Value(…), that's why
     * we inject it via constructor
     *
     * @param userRepo          The user-repo
     * @param servicesUser      The email-address of the service-user
     * @param servicePassword   The password of the service-user
     */
    public BasicAuthAuthenticator(UserRepository userRepo, String servicesUser, String servicePassword){
        this.userRepo = userRepo;
        this.serviceUser = servicesUser;
        this.servicePassword = servicePassword;
    }

    /**
     * Custom validation-logic.
     *
     * @param credentials
     * @param context
     * @throws HttpAction
     */
    @Override
    public void validate(UsernamePasswordCredentials credentials, final WebContext context) throws HttpAction {

        final CommonProfile profile = new CommonProfile();

        //Test if we have any credentials
        //If so, get various information from it (like email-address, password etc.)
        if (credentials != null) {
            String email = credentials.getUsername();
            String password = credentials.getPassword();
            //If the service-user tries to login, add his profile so he is logged in and can use the service
            if(email.equalsIgnoreCase(serviceUser) && password.equals(servicePassword)){
                profile.addAttribute(Pac4jConstants.USERNAME, serviceUser);
                profile.addRole("REGISTERED");
                profile.addRole("ADMINISTRATOR");
                profile.addRole("SERVICE");
                logger.debug(email + " received role REGISTERED");
            //If this is not the service user, the email and password is not blank and both are valid we
            //construct the correct profile
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

        //Set the user profile
        credentials.setUserProfile(profile);
        //The the profile into the session
        context.setSessionAttribute("profile", profile);
    }

    /**
     * Checks an email-password-combination if it is valid.
     *
     * Passwords are stored with argon2 in the db, so we use
     * argon2 to verify the correct password.
     *
     * @param email         The email of the user to check the password for
     * @param password      The password to validate
     * @return              True if the password is valid, false otherwise
     */
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

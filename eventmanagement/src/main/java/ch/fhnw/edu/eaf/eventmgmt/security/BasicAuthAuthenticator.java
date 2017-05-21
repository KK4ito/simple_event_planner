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

import java.util.List;

public class BasicAuthAuthenticator implements Authenticator<UsernamePasswordCredentials> {

    private final UserRepository userRepo;

    BasicAuthAuthenticator(UserRepository userRepo){
        this.userRepo = userRepo;
    }

    @Override
    public void validate(UsernamePasswordCredentials credentials, final WebContext context) throws HttpAction {

        final CommonProfile profile = new CommonProfile();

        if (credentials != null) {
            String username = credentials.getUsername();
            String password = credentials.getPassword();
            if (!CommonHelper.isBlank(username) &&  !CommonHelper.isBlank(password) && isValidPassword(username, password)) {
                profile.addAttribute(Pac4jConstants.USERNAME, username);
                List<User> users = userRepo.me(username);
                profile.addRole("REGISTERED");
                if(users.get(0).getRole() == 2) {
                    profile.addRole("ADMINISTRATOR");
                }
            }
        }

        credentials.setUserProfile(profile);
        context.setSessionAttribute("profile", profile);
    }

    protected boolean isValidPassword(String username, String password) {
        // Create instance
        Argon2 argon2 = Argon2Factory.create();

        // Read password from user
        char[] userPassword = password.toCharArray();

        try {
            List<User> users = userRepo.me(username);
            if(users.size() == 0) return false;
            User user = users.get(0);

            return argon2.verify(user.getPassword(), userPassword);
        } finally {
            // Wipe confidential data
            argon2.wipeArray(userPassword);
        }
    }
}

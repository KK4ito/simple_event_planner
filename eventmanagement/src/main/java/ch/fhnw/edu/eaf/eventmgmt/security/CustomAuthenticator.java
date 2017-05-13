package ch.fhnw.edu.eaf.eventmgmt.security;

import ch.fhnw.edu.eaf.eventmgmt.domain.User;
import ch.fhnw.edu.eaf.eventmgmt.persistence.UserRepository;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.pac4j.core.context.Pac4jConstants;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.credentials.UsernamePasswordCredentials;
import org.pac4j.core.credentials.authenticator.Authenticator;
import org.pac4j.core.exception.CredentialsException;
import org.pac4j.core.exception.HttpAction;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.util.CommonHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CustomAuthenticator implements Authenticator<UsernamePasswordCredentials> {

    private final UserRepository userRepo;

    CustomAuthenticator(UserRepository userRepo){
        this.userRepo = userRepo;
    }

    @Override
    public void validate(final UsernamePasswordCredentials credentials, final WebContext context) throws HttpAction {
        System.out.println("validation");
        if (credentials == null) {
            throwsException("No credential");
        }
        String username = credentials.getUsername();
        String password = credentials.getPassword();
        if (CommonHelper.isBlank(username)) {
            throwsException("Username cannot be blank");
        }
        if (CommonHelper.isBlank(password)) {
            throwsException("Password cannot be blank");
        }
        if (CommonHelper.areNotEquals(username, password)) {
            // TODO: throwsException("Username : '" + username + "' does not match password");
        }
        if (!isValidPassword(username, password)) {
            throwsException("Passwords do not match");
        }

        final CommonProfile profile = new CommonProfile();
        profile.setId(username);
        profile.addAttribute(Pac4jConstants.USERNAME, username);

        //profile.addRole("ADMIN");
        //profile.addPermission();

        credentials.setUserProfile(profile);
    }

    protected boolean isValidPassword(String username, String password) {
        // Create instance
        Argon2 argon2 = Argon2Factory.create();

        // Read password from user
        char[] userPassword = password.toCharArray();

        System.out.println(this.userRepo == null);
        System.out.println(this.userRepo.toString());

        try {

            List<User> users = userRepo.me(username);
            if(users.size() == 0) return false;
            User user = users.get(0);

            System.out.println("checking passwords");
            System.out.println(user.getPassword());
            System.out.println(userPassword);
            System.out.println(argon2.verify(user.getPassword(), userPassword));

            return argon2.verify(user.getPassword(), userPassword);
        } finally {
            // Wipe confidential data
            argon2.wipeArray(userPassword);
        }
    }
    protected void throwsException(final String message) {
        throw new CredentialsException(message);
    }
}

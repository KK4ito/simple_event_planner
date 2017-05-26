package ch.fhnw.edu.eaf.eventmgmt.persistence;

import ch.fhnw.edu.eaf.eventmgmt.domain.User;
import org.pac4j.core.context.Pac4jConstants;
import org.pac4j.core.profile.CommonProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class AAIRepository {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpServletRequest context;

    @RequestMapping(value = "${spring.data.rest.basePath}/aai/", method = RequestMethod.GET)
    @Transactional
    public void aaiAuth(@RequestHeader(value = "surname", required = true) String surname,
                        @RequestHeader(value = "givenname", required = true) String givenname,
                        @RequestHeader(value = "mail", required = true) String mail,
                        @RequestHeader(value = "homeorganization", required = true) String org,
                        @RequestHeader(value = "shib-session-id", required = true) String id,
                        @RequestHeader(value = "shib-session-index", required = true) String index,
                        @RequestHeader(value = "shib-identity-provider", required = true) String provider,
                        @RequestHeader(value = "shib-authentication-method", required = true) String method,
                        @RequestHeader(value = "shib-authentication-instant", required = true) String instant,
                        @RequestHeader(value = "shib-authncontext-class", required = true) String authncontext,
                        @RequestHeader(value = "x-forwarded-for", required = true) String originRemoteAddr,
                        HttpServletResponse response) throws Exception {
        if (!originRemoteAddr.contains("https://www.cs.technik.fhnw.ch/wodss17-5-aai/")) {
            throw new Exception(String.format("Unknown aai authentication provider: %s", originRemoteAddr));
        } else {
            List<User> users = userRepository.findByEmail(mail);
            if(users.size() > 0){
                // User already exists, load profile into session
                User user = users.get(0);
                final CommonProfile profile = new CommonProfile();
                profile.addAttribute(Pac4jConstants.USERNAME, user.getEmail());
                profile.addRole("REGISTERED");
                if(user.getRole() == 2) {
                    profile.addRole("ADMINISTRATOR");
                }
                profile.addAttribute("user", user);
                context.getSession().setAttribute("profile", profile);
            }else{
                // User does not exist, create him
                User user = new User();
                user.setFirstName(givenname);
                user.setLastName(surname);
                user.setEmail(mail);
                user.setInternal(true);
                user.setRole(User.RoleType.REGISTERED);
                userRepository.save(user);
            }
        }
        response.sendRedirect("https://www.cs.technik.fhnw.ch/wodss17-5/#/profile");
    }
}

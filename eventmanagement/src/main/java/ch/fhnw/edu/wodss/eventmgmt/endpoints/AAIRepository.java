package ch.fhnw.edu.wodss.eventmgmt.endpoints;

import ch.fhnw.edu.wodss.eventmgmt.entities.User;
import org.pac4j.core.context.Pac4jConstants;
import org.pac4j.core.profile.CommonProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Handles login via aai.
 */
@RestController
public class AAIRepository {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private HttpServletRequest context;

    /**
     * Handles a request that reaches us via the "aai-redirect" of the fhnw.
     * The method selects the name and email from the headers. If the user never logged in
     * to this website, a new user-object is created and saved to the db (without a password obviously -
     * since this is handled externally by switch). If the user logged in before, we get the profile
     * from the db.
     *
     * @param surname       The firstname of the authenticated user
     * @param givenname     The lastname of the authenticated user
     * @param mail          The mail-address of the authenticated user
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "${spring.data.rest.basePath}/aai/", method = RequestMethod.GET)
    @Transactional
    public void aaiAuth(@RequestHeader(value = "surname", required = true) String surname,
                        @RequestHeader(value = "givenname", required = true) String givenname,
                        @RequestHeader(value = "mail", required = true) String mail,
                        HttpServletResponse response) throws Exception {

        List<User> users = userRepository.findByEmail(mail);
        if (users.size() > 0) {
            // User already exists, load profile into session
            User user = users.get(0);
            final CommonProfile profile = new CommonProfile();
            profile.addAttribute(Pac4jConstants.USERNAME, user.getEmail());
            profile.addRole("REGISTERED");
            log.info("Existing User with role REGISTERED logged in: " + user.getFirstName() + " " + user.getLastName());
            if (user.getRole() == 2) {
                profile.addRole("ADMINISTRATOR");
                log.info("User: " + user.getFirstName() + " " + user.getLastName() + " received Role ADMINISTRATOR");
            }
            profile.addAttribute("user", user);
            context.getSession().setAttribute("profile", profile);
        } else {
            // User does not exist, create him
            final CommonProfile profile = new CommonProfile();
            profile.addAttribute(Pac4jConstants.USERNAME, mail.trim().toUpperCase());
            profile.addRole("REGISTERED");
            context.getSession().setAttribute("profile", profile);
            User user = new User();
            user.setFirstName(new String(org.apache.commons.io.Charsets.ISO_8859_1.encode(givenname).array()));
            user.setLastName(new String(org.apache.commons.io.Charsets.ISO_8859_1.encode(surname).array()));
            user.setEmail(mail);
            user.setInternal(true);
            user.setRole(User.RoleType.REGISTERED);
            em.persist(user);
            log.info("New user with role REGISTERED logged in: " + user.getFirstName() + " " + user.getLastName());

        }
        response.sendRedirect("https://www.cs.technik.fhnw.ch/wodss17-5/#/profile");
    }
}

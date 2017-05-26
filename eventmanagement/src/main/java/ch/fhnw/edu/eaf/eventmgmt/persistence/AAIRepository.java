package ch.fhnw.edu.eaf.eventmgmt.persistence;

import ch.fhnw.edu.eaf.eventmgmt.domain.User;
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

@RestController
public class AAIRepository {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private HttpServletRequest context;

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
            if (user.getRole() == 2) {
                profile.addRole("ADMINISTRATOR");
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
            user.setFirstName(givenname);

            String utf8String = new String(org.apache.commons.io.Charsets.ISO_8859_1.encode(surname).array());

            //byte[] utf8 = new String(latin1, "ISO-8859-1").getBytes("UTF-8");

            user.setLastName(utf8String);
            user.setEmail(mail);
            user.setInternal(true);
            user.setRole(User.RoleType.REGISTERED);
            em.persist(user);

        }

        response.sendRedirect("https://www.cs.technik.fhnw.ch/wodss17-5/#/profile");
    }
}

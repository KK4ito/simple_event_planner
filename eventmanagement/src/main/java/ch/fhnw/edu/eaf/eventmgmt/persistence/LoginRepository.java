package ch.fhnw.edu.eaf.eventmgmt.persistence;

import ch.fhnw.edu.eaf.eventmgmt.domain.AnswerWrapper;
import ch.fhnw.edu.eaf.eventmgmt.domain.Mail;
import ch.fhnw.edu.eaf.eventmgmt.domain.ResetPasswordAnswerMessage;
import ch.fhnw.edu.eaf.eventmgmt.domain.ResetPasswordWrapper;
import ch.fhnw.edu.eaf.eventmgmt.domain.User;
import org.pac4j.core.context.J2EContext;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.profile.CommonProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.stringtemplate.v4.ST;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;
import java.util.List;

@RestController
public class LoginRepository {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    static final long ONE_MINUTE_IN_MILLISECONDS=60000;

    @Autowired
    private HttpServletRequest context;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    RestTemplate restTemplate;

    @Value("${passwordReset.url}")
    private String passwordResetBaseUrl;

    @Value("${mail.resetPassword.text}")
    private String passwordResetText;

    @Value("${mailer.token}")
    private String mailerToken;

    @Value("${microservices.mailer}")
    private String mailer;

    @RequestMapping(value = "/api/login/login", method = RequestMethod.GET)
    public ResponseEntity<User> login(HttpServletRequest request, HttpServletResponse response) {
        final WebContext context = new J2EContext(request, response);
        String email = ((CommonProfile) context.getSessionAttribute("profile")).getUsername();
        List<User> users = userRepository.findByEmail(email);
        if (users.size() == 0) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        log.info("Login of user: " + users.get(0).getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(users.get(0));
    }

    @RequestMapping(value = "/api/login/logout", method = RequestMethod.GET)
    public void logout() {
    }

    @RequestMapping(value = "/api/login/requestPasswordReset", method = RequestMethod.POST)
    public ResponseEntity<ResetPasswordAnswerMessage> sendPasswordResetMail(@RequestBody ResetPasswordWrapper wrapper) {

        String email = wrapper.getEmail();
        //Check in db, is user present
        List<User> u = userRepository.findByEmail(email);

        if(u.size() == 0 || u.size() > 1) {
            return new ResponseEntity<>(new ResetPasswordAnswerMessage("No matching user found"), HttpStatus.NOT_ACCEPTABLE);
        }
        User user = u.get(0);

        if(user.isInternal()) {
            return new ResponseEntity<>(new ResetPasswordAnswerMessage("User authenticated via aai cannot reset password"), HttpStatus.NOT_ACCEPTABLE);
        }

        //generate token
        String token = "";
        SecureRandom random = new SecureRandom();
        token = new BigInteger(130, random).toString(32);

        //generate expirationtime
        Date timePlus60Minutes = new Date(new Date().getTime() + (60 * ONE_MINUTE_IN_MILLISECONDS));

        //send mail to user
        String url = passwordResetBaseUrl + token;

        String mailerUrl = "http://" + mailer + "/api/send";
        Mail mail = new Mail();
        mail.token = mailerToken;
        mail.to = user.getEmail();
        mail.subject = "Passwort zurücksetzen";


        ST template = new ST(passwordResetText, '$', '$');
        template.add("url", url);
        mail.body = template.render();
        mail.keys = new String[0];
        mail.values = new String[0];

        try {
            ResponseEntity<AnswerWrapper> result = restTemplate.postForEntity(mailerUrl, mail, AnswerWrapper.class);
            if(result.getStatusCode() == HttpStatus.NOT_ACCEPTABLE) {
                log.error("Mail to " + mail.to + " was not send: Token was invalid");
                return new ResponseEntity<>(new ResetPasswordAnswerMessage("Invalid token"), HttpStatus.NOT_ACCEPTABLE);
            } else if(result.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
                log.error("Mail to " + mail.to + " could not be send: Internal server error on mailerservice");
                return new ResponseEntity<>(new ResetPasswordAnswerMessage(), HttpStatus.FAILED_DEPENDENCY);
            } else if(result.getStatusCode() == HttpStatus.OK){

                userRepository.updateTokenAndExpirationDateByEmail(token, timePlus60Minutes, user.getEmail());

                log.info("Reset-Password-Mail to " + mail.to + " was send successfully.");
                log.info("Saved reset-token and expirationdate for user " + user.getFirstName() + " " + user.getLastName() + " with id: " + user.getId());
                return new ResponseEntity<>(new ResetPasswordAnswerMessage("OK"), HttpStatus.OK);
            };
        } catch(RestClientException e) {
            e.printStackTrace();
            return new ResponseEntity<ResetPasswordAnswerMessage>(new ResetPasswordAnswerMessage("Mail Server Error"), HttpStatus.FAILED_DEPENDENCY);
        }
        return new ResponseEntity<ResetPasswordAnswerMessage>(new ResetPasswordAnswerMessage("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping(value = "/api/login/resetPassword", method = RequestMethod.POST)
    public ResponseEntity<ResetPasswordAnswerMessage> resetPassword(@RequestBody ResetPasswordWrapper wrapper) {

        //Getting token
        String token = wrapper.getToken();

        List<User> users = userRepository.findByToken(token);
        if(users.size() == 0) {
            return new ResponseEntity<>(new ResetPasswordAnswerMessage("Invalid token"), HttpStatus.NOT_ACCEPTABLE);
        }
        if(users.size() > 1) {
            return new ResponseEntity<>(new ResetPasswordAnswerMessage("Invalid token: multiple instances"), HttpStatus.NOT_ACCEPTABLE);
        }
        if(users.size() == 1) {
            User user = users.get(0);
            //Check if token exists in user-db and did not expire
            Date currentDate = new Date();

            //Check if the passed token is equal to the token in the db
            //Check if the current date is not after the expiration date (this would mean the token expired)
            //Check if current Date minus 1 hour is not before the expiration date (this would mean there was
            //some malicious attempt)
            if(token.equals(user.getPasswordResetToken()) &&
                    !currentDate.after(user.getPasswordResetTokenExpirationDate()) &&
                    !currentDate.before(new Date(user.getPasswordResetTokenExpirationDate().getTime() - (60 * ONE_MINUTE_IN_MILLISECONDS)))) {
                //set ARGON2-newPassword, proper hashing is done in the setPassword-Method
                user.setPassword(wrapper.getPassword());
                userRepository.setTokenAndExpirationDateAndPasswordByEmail(user.getPassword(), user.getEmail());
                log.info("New password was set for: " + user.getFirstName() + " " + user.getLastName() + " with id: " + user.getId());

                //return success
                return new ResponseEntity<>(new ResetPasswordAnswerMessage("OK"), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(new ResetPasswordAnswerMessage("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

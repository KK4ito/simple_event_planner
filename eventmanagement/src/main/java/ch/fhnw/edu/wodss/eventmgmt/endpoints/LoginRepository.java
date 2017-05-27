package ch.fhnw.edu.wodss.eventmgmt.endpoints;

import ch.fhnw.edu.wodss.eventmgmt.models.AnswerWrapper;
import ch.fhnw.edu.wodss.eventmgmt.models.Mail;
import ch.fhnw.edu.wodss.eventmgmt.models.ResetPasswordAnswerMessage;
import ch.fhnw.edu.wodss.eventmgmt.models.ResetPasswordWrapper;
import ch.fhnw.edu.wodss.eventmgmt.entities.User;
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

/**
 * The login-repository.
 *
 * Provides various methods for login, logout, and reset the password.
 */
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

    /**
     * The base-url, used to send the resetPassword-URL that is sent to the user wishing
     * to reset his password.
     */
    @Value("${passwordReset.url}")
    private String passwordResetBaseUrl;

    /**
     * The text of the resetPassword-mail.
     */
    @Value("${mail.resetPassword.text}")
    private String passwordResetText;

    /**
     * The subject of the resetPassword-mail.
     */
    @Value("${mail.resetPassword.subject}")
    private String resetPasswordSubject;

    /**
     * The subject of the mail when a new user is created.
     */
    @Value("${mail.newUserCreated.subject}")
    private String newUserPasswordSubject;

    /**
     * The text of the mail when a new user is created.
     */
    @Value("${mail.newUserPassword.text}")
    private String newUserPasswordText;

    /**
     * The token of the mail-microservice, so we are actually able to send mails.
     */
    @Value("${mailer.token}")
    private String mailerToken;

    /**
     * The logical name of the mailer-microservice.
     */
    @Value("${microservices.mailer}")
    private String mailer;

    /**
     * Executes a login.
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/api/login/login", method = RequestMethod.GET)
    public ResponseEntity<User> login(HttpServletRequest request, HttpServletResponse response) {
        final WebContext context = new J2EContext(request, response);
        String email = ((CommonProfile) context.getSessionAttribute("profile")).getUsername();
        List<User> users = userRepository.findByEmail(email);
        if (users.size() == 0) {
            log.error("No user with email " + email + " found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        log.info("Login of user: " + users.get(0).getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(users.get(0));
    }

    @RequestMapping(value = "/api/login/logout", method = RequestMethod.GET)
    public void logout() {
    }

    /**
     * Handles a resetPassword-Request.
     *
     *
     * @param wrapper   ResetPasswordWrapper containing the email of the user requesting a to reset his password.
     * @return
     */
    @RequestMapping(value = "/api/login/requestPasswordReset", method = RequestMethod.POST)
    public ResponseEntity<ResetPasswordAnswerMessage> sendPasswordResetMail(@RequestBody ResetPasswordWrapper wrapper) {

        String email = wrapper.getEmail();
        //Check in db if is user present
        List<User> u = userRepository.findByEmail(email);

        //If we found more than one user with the mail we cannot reset the password.
        if(u.size() == 0 || u.size() > 1) {
            log.error("requestPasswordReset: No matching user for email " + email + " found");
            return new ResponseEntity<>(new ResetPasswordAnswerMessage("No matching user found"), HttpStatus.NOT_ACCEPTABLE);
        }
        User user = u.get(0);

        //If the user is internal we cannot reset his password
        if(user.isInternal()) {
            log.error("requestPasswordReset: aai-user tried to reset password: " + email);
            return new ResponseEntity<>(new ResetPasswordAnswerMessage("User authenticated via aai cannot reset password"), HttpStatus.NOT_ACCEPTABLE);
        }

        //Generate a reset token
        String token = "";
        SecureRandom random = new SecureRandom();
        token = new BigInteger(130, random).toString(32);

        //Generate/calculate the expirationtime of the reset-token.
        Date timePlus60Minutes = new Date(new Date().getTime() + (60 * ONE_MINUTE_IN_MILLISECONDS));

        //Send resetPassword-mail to user
        String url = passwordResetBaseUrl + token;

        String mailerUrl = "http://" + mailer + "/api/send";
        Mail mail = new Mail();
        mail.token = mailerToken;
        mail.to = user.getEmail();

        mail.subject = "";
        String mailText = "";

        if(wrapper.isResetPassword()) {
            mail.subject = resetPasswordSubject;
            mailText = passwordResetText;
        } else {
            mail.subject = newUserPasswordSubject;
            mailText = newUserPasswordText;
        }

        //Insert the correct values into the resetPassword-Mailtemplate
        ST template = new ST(mailText, '$', '$');
        template.add("url", url);
        mail.body = template.render();
        mail.keys = new String[0];
        mail.values = new String[0];

        try {
            //Send the resetPassword-Mail to the user.
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
            log.error("Mail-server experienced a problem");
            return new ResponseEntity<ResetPasswordAnswerMessage>(new ResetPasswordAnswerMessage("Mail Server Error"), HttpStatus.FAILED_DEPENDENCY);
        }
        log.error("requestPasswordReset: Internal server error");
        return new ResponseEntity<ResetPasswordAnswerMessage>(new ResetPasswordAnswerMessage("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Resets an old password.
     *
     * @param wrapper       Wrapper containing a resetPassword-token
     * @return
     */
    @RequestMapping(value = "/api/login/resetPassword", method = RequestMethod.POST)
    public ResponseEntity<ResetPasswordAnswerMessage> resetPassword(@RequestBody ResetPasswordWrapper wrapper) {

        //Getting the token from the wrapper
        String token = wrapper.getToken();

        //Find all users with the passed token.
        List<User> users = userRepository.findByToken(token);

        //We should only find one user with the token otherwise return an error-code.
        if(users.size() == 0) {
            log.error("No user found for the resetPasswordToken " + token);
            return new ResponseEntity<>(new ResetPasswordAnswerMessage("Invalid token"), HttpStatus.NOT_ACCEPTABLE);
        }
        if(users.size() > 1) {
            log.error("More than 1 user found for the resetPasswordToken " + token);
            return new ResponseEntity<>(new ResetPasswordAnswerMessage("Invalid token: multiple instances"), HttpStatus.NOT_ACCEPTABLE);
        }
        if(users.size() == 1) {
            User user = users.get(0);

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
        log.error("resetPassword: Internal server error");
        return new ResponseEntity<>(new ResetPasswordAnswerMessage("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

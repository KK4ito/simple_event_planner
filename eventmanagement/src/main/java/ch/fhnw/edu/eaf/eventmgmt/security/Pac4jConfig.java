package ch.fhnw.edu.eaf.eventmgmt.security;

import ch.fhnw.edu.eaf.eventmgmt.persistence.UserRepository;
import org.pac4j.core.authorization.authorizer.RequireAnyRoleAuthorizer;
import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.core.context.Cookie;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.context.session.SessionStore;
import org.pac4j.http.client.direct.CookieClient;
import org.pac4j.http.client.direct.DirectBasicAuthClient;
import org.pac4j.http.client.direct.ParameterClient;
import org.pac4j.jwt.credentials.authenticator.JwtAuthenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class Pac4jConfig{

    @Autowired
    private UserRepository userRepo;

    @Bean
    public Config Config() {

        System.out.println("CONFIG LOADED");
        // REST authent with JWT for a token passed in the url as the token parameter
        /*
        ParameterClient parameterClient = new ParameterClient("token", new JwtAuthenticator("wefweifwoeh"));
        parameterClient.setSupportGetRequest(true);
        parameterClient.setSupportPostRequest(false);
*/
        CookieClient cc = new CookieClient("yolo", new CookieAuthenticator());

        // basic auth
        final DirectBasicAuthClient directBasicAuthClient = new DirectBasicAuthClient(new CustomAuthenticator(this.userRepo));

        final Config config = new Config(new Clients(cc, directBasicAuthClient));
        config.addAuthorizer("custom", new CustomAuthorizer());
        //config.addAuthorizer("admin", new RequireAnyRoleAuthorizer("ROLE_ADMIN"));

        config.addMatcher("events",new CustomMatcher("/api/files/", false, false, false, false));
        config.addMatcher("events",new CustomMatcher("/api/events/", false, false, false, false));
        config.addMatcher("events",new CustomMatcher("/api/users/", true, false, false, false));

        return config;
    }
}
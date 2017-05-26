package ch.fhnw.edu.eaf.eventmgmt.security;

import ch.fhnw.edu.eaf.eventmgmt.persistence.UserRepository;
import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.http.client.direct.CookieClient;
import org.pac4j.http.client.direct.DirectBasicAuthClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class Pac4jConfig {

    @Autowired
    private UserRepository userRepo;

    @Value("${microservices.service.user}")
    private String serviceUser;

    @Value("${microservices.service.password}")
    private String servicePassword;

    @Bean
    public Config Config() {
        final CookieClient cookieClient = new CookieClient("JSESSIONID", new CookieAuthenticator());
        final DirectBasicAuthClient directBasicAuthClient = new DirectBasicAuthClient(new BasicAuthAuthenticator(this.userRepo, this.serviceUser, this.servicePassword));
        final Config config = new Config(new Clients(cookieClient, directBasicAuthClient));
        config.addAuthorizer("custom", new SecureAuthorizer());
        config.addMatcher("events", new CustomizablePathMatcher(new SecurePath[]{
                new SecurePath("/api/login/", true, true, true, true, false, true),
                new SecurePath("/api/users/search/role", true),
                new SecurePath("/api/events/search/closingEvents", true),
                new SecurePath("/api/users/search/attendees", false),
                new SecurePath("/api/eventAttendees/", false, true, true, true, true, true),
                new SecurePath("/api/files/", false, true, true, true, true, true),
                new SecurePath("/api/events/", false, true, true, true, true, true),
                new SecurePath("/api/users/", true, true, true, true, true, true),
        }
        ));

        return config;
    }
}
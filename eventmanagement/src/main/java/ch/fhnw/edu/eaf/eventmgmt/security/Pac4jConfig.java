package ch.fhnw.edu.eaf.eventmgmt.security;

import org.pac4j.core.authorization.authorizer.RequireAnyRoleAuthorizer;
import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.http.client.direct.DirectBasicAuthClient;
import org.pac4j.http.client.direct.ParameterClient;
import org.pac4j.jwt.credentials.authenticator.JwtAuthenticator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class Pac4jConfig{

    @Bean
    public Config Config() {

        // REST authent with JWT for a token passed in the url as the token parameter
        ParameterClient parameterClient = new ParameterClient("token", new JwtAuthenticator("wefweifwoeh"));
        parameterClient.setSupportGetRequest(true);
        parameterClient.setSupportPostRequest(false);

        // basic auth
        final DirectBasicAuthClient directBasicAuthClient = new DirectBasicAuthClient(new CustomAuthenticator());

        final Config config = new Config(new Clients(parameterClient, directBasicAuthClient));
        config.addAuthorizer("custom", new CustomAuthorizer());
        //config.addAuthorizer("admin", new RequireAnyRoleAuthorizer("ROLE_ADMIN"));

        config.addMatcher("events",new CustomMatcher("/api/files/", false, false, false, false));
        config.addMatcher("events",new CustomMatcher("/api/events/", false, false, false, false));
        config.addMatcher("events",new CustomMatcher("/api/users/", false, false, false, false));
        config.addMatcher("events",new CustomMatcher("/api/users/search/", false, false, false, false));

        return config;
    }
}
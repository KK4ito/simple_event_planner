package ch.fhnw.edu.eaf.eventmgmt;

import org.pac4j.core.authorization.authorizer.RequireAnyRoleAuthorizer;
import org.pac4j.core.client.Clients;
import org.pac4j.core.client.direct.AnonymousClient;
import org.pac4j.core.config.Config;
import org.pac4j.core.matching.ExcludedPathMatcher;
import org.pac4j.http.client.direct.DirectBasicAuthClient;
import org.pac4j.http.client.direct.ParameterClient;
import org.pac4j.http.credentials.authenticator.test.SimpleTestUsernamePasswordAuthenticator;
import org.pac4j.jwt.credentials.authenticator.JwtAuthenticator;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
public class Pac4jConfig extends Config{

    public Pac4jConfig() {
        super();

        // REST authent with JWT for a token passed in the url as the token parameter
        ParameterClient parameterClient = new ParameterClient("token", new JwtAuthenticator("wefweifwoeh"));
        parameterClient.setSupportGetRequest(true);
        parameterClient.setSupportPostRequest(false);

        // basic auth
        final DirectBasicAuthClient directBasicAuthClient = new DirectBasicAuthClient(new SimpleTestUsernamePasswordAuthenticator());

        this.addAuthorizer("custom", new CustomAuthorizer());
        this.addAuthorizer("admin", new RequireAnyRoleAuthorizer("ROLE_ADMIN"));

        final Clients clients = new Clients(parameterClient, directBasicAuthClient);
        this.setClients(clients);
        //this.addMatcher("excludedPath", new ExcludedPathMatcher("^/facebook/notprotected\\.html$"));

    }
}
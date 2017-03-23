package ch.fhnw.edu.eaf.eventmgmt.security;

import org.pac4j.core.authorization.authorizer.RequireAnyRoleAuthorizer;
import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.http.client.direct.DirectBasicAuthClient;
import org.pac4j.http.client.direct.ParameterClient;
import org.pac4j.jwt.credentials.authenticator.JwtAuthenticator;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
public class Pac4jConfig extends Config{

    public Pac4jConfig() {
        // REST authent with JWT for a token passed in the url as the token parameter
        ParameterClient parameterClient = new ParameterClient("token", new JwtAuthenticator("wefweifwoeh"));
        parameterClient.setSupportGetRequest(true);
        parameterClient.setSupportPostRequest(false);

        // basic auth
        final DirectBasicAuthClient directBasicAuthClient = new DirectBasicAuthClient(new CustomAuthenticator());

        this.authorizers.put("custom", new CustomAuthorizer());
        this.authorizers.put("admin", new RequireAnyRoleAuthorizer("ROLE_ADMIN"));

        this.clients = new Clients(parameterClient, directBasicAuthClient);
        //this.addMatcher("excludedPath", new ExcludedPathMatcher("^/facebook/notprotected\\.html$"));

    }
}
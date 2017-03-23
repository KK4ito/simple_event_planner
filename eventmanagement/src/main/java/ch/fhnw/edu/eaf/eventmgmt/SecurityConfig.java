package ch.fhnw.edu.eaf.eventmgmt;

import org.pac4j.springframework.security.web.SecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@ComponentScan("ch.fhnw.edu.eaf.eventmgmt")
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    private Pac4jConfig config;

    protected void configure(final HttpSecurity http) throws Exception {


        final SecurityFilter filter = new SecurityFilter(config, "DirectBasicAuthClient,ParameterClient");

        http.addFilterBefore(filter, BasicAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);
                //.antMatcher("/api/**")
                //.antMatcher("/rest-jwt/**")
                //.addFilterBefore(filter, BasicAuthenticationFilter.class)
                //.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);
    }

}
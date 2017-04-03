package ch.fhnw.edu.eaf.eventmgmt.security;

import org.pac4j.core.config.Config;
import org.pac4j.springframework.security.web.SecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
@ComponentScan
public class SecurityConfig extends WebSecurityConfigurerAdapter{
    @Autowired
    private Config config;

    protected void configure(final HttpSecurity http) throws Exception {

        // Glue code to automatically add Clients, Matchers and Authorizers
        final SecurityFilter filter = new SecurityFilter(config, listToCommaSeparatedString(config.getClients().findAllClients().stream().map(i -> i.getName()).collect(Collectors.toSet())));
        filter.setMatchers(mapToCommaSeparatedString(config.getMatchers()));
        filter.setAuthorizers(mapToCommaSeparatedString(config.getAuthorizers()));

        http
                .csrf().disable()
                .antMatcher("/**")
                .addFilterBefore(filter, BasicAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);
    }

    private String mapToCommaSeparatedString(Map<String, ?> map){
        return listToCommaSeparatedString(map.keySet());
    }

    private String listToCommaSeparatedString(Collection<?> list){
        String s = "";
        for(Object key : list){
            s += key.toString() + ",";
        }
        return s.substring(0, s.length() - 1);
    }

}
package ch.fhnw.edu.eaf.eventmgmt.security;

import org.pac4j.core.config.Config;
import org.pac4j.springframework.security.web.SecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by lukasschonbachler on 24.05.17.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
@ComponentScan
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler =
                new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(new CustomPermissionEvaluator());
        return expressionHandler;
    }

    @Configuration
    public static class SecurityConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        private Config config;

        @Override
        public void configure(WebSecurity web) throws Exception {
            web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
        }

        protected void configure(final HttpSecurity http) throws Exception {


            // Glue code to automatically add Clients, Matchers and Authorizers
            final SecurityFilter filter = new SecurityFilter(config, listToCommaSeparatedString(config.getClients().findAllClients().stream().map(i -> i.getName()).collect(Collectors.toSet())));
            filter.setMatchers(mapToCommaSeparatedString(config.getMatchers()));
            filter.setAuthorizers(mapToCommaSeparatedString(config.getAuthorizers()));

            http
                    .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
                    .authorizeRequests().antMatchers(HttpMethod.OPTIONS, "/**").permitAll().and()

                    .antMatcher("/**")
                    .addFilterAfter(filter, BasicAuthenticationFilter.class)
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
        }

        private String mapToCommaSeparatedString(Map<String, ?> map) {
            return listToCommaSeparatedString(map.keySet());
        }

        private String listToCommaSeparatedString(Collection<?> list) {
            String s = "";
            for (Object key : list) {
                s += key.toString() + ",";
            }
            return s.substring(0, s.length() - 1);
        }

    }
}
package ch.fhnw.edu.eaf.eventmgmt.security;

import org.pac4j.core.config.Config;
import org.pac4j.springframework.security.web.SecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan
public class SecurityConfig extends WebSecurityConfigurerAdapter {
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

        /*
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*"); // @Value: http://localhost:8080
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        */


        http
                /*
                .exceptionHandling().authenticationEntryPoint(new BasicAuthenticationEntryPoint() {
            @Override
            public void commence(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException authException) throws IOException, ServletException, IOException {
                System.out.println("Blaaaaa");
                if (HttpMethod.OPTIONS.matches(request.getMethod())) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, request.getHeader(HttpHeaders.ORIGIN));
                    response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, request.getHeader(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS));
                    response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, request.getHeader(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD));
                    response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
                } else {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
                }
            }
        }).and()
/*/
                /*
                .cors().configurationSource(new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                final CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(ImmutableList.of("*"));
                configuration.setAllowedMethods(ImmutableList.of("HEAD",
                        "GET", "POST", "PUT", "DELETE", "PATCH"));
                // setAllowCredentials(true) is important, otherwise:
                // The value of the 'Access-Control-Allow-Origin' header in the response must not be the wildcard '*' when the request's credentials mode is 'include'.
                configuration.setAllowCredentials(true);
                // setAllowedHeaders is important! Without it, OPTIONS preflight request
                // will fail with 403 Invalid CORS request
                configuration.setAllowedHeaders(ImmutableList.of("Authorization", "Cache-Control", "Content-Type"));
                //return configuration;

                final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;

            }
        }).and()
*/
                //Disable csrf of SpringSecurity because it is handled by pac4j (see Pac4jConfig.java)
                .csrf().disable()
                .authorizeRequests().antMatchers(HttpMethod.OPTIONS, "/**").permitAll().and()

                .antMatcher("/**")
                //.addFilterBefore(new CorsFilter(), Filter.class)
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
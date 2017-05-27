package ch.fhnw.edu.wodss.eventmgmt;

/**
 * Created by lukasschonbachler on 15.03.17.
 */

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.base.Predicates.*;


@Configuration
@EnableSwagger2
@Import({springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration.class})
public class SwaggerConfiguration {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .paths(paths())
                .build();
    }

    // TODO Maybe useful for user registration
    Predicate<String> startsWithProfile = new Predicate<String>() {
        @Override
        public boolean apply(String str) {
            return str.startsWith("/api/profile");
        }
    };

    private Predicate<String> paths() {
        return not(or(
                equalTo("/error"),
                equalTo("/api"),
                equalTo("/api/"),
                startsWithProfile
        ));
    }
}
package ch.fhnw.edu.eaf.eventmgmt;

import com.fasterxml.classmate.TypeResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableDiscoveryClient
//@EnableSwagger2
@SpringBootApplication
public class EventmanagementApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(EventmanagementApplication.class, args);
		//ApplicationContext ctx = SpringApplication.run(EventmanagementApplication.class, args);
	}
}

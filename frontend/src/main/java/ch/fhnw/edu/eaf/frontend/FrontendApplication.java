package ch.fhnw.edu.eaf.frontend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan
public class FrontendApplication {
	public static void main(String[] args) {
		SpringApplication.run(FrontendApplication.class, args);
	}
}

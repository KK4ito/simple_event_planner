package ch.fhnw.edu.wodss.eventmgmt;

import org.pac4j.core.config.Config;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@EnableDiscoveryClient
@SpringBootApplication
public class EventmanagementApplication {

	@Autowired
	private Config config;

	public static String BASE_URL_MAILER;

	public static void main(String[] args) {
			SpringApplication.run(EventmanagementApplication.class, args);
	}

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}

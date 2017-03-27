package ch.fhnw.edu.eaf.eventmgmt;

import org.pac4j.core.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class EventmanagementApplication {

	@Autowired
	private Config config;

	public static void main(String[] args) {
		SpringApplication.run(EventmanagementApplication.class, args);
	}

}

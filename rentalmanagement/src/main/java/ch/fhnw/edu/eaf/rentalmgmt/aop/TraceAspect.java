package ch.fhnw.edu.eaf.rentalmgmt.aop;

import java.io.IOException;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.fhnw.edu.eaf.rentalmgmt.domain.Rental;
import ch.fhnw.edu.eaf.rentalmgmt.web.dto.UserDTO;

@Aspect
@Component
public class TraceAspect {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Value(value = "${microservice.usermagagement:usermanagement}")
	private String userService;
	
	@Autowired
	private RestTemplate restTemplate;

	@Before("execution(* *..RentalController.create(..)) && args(rental)")
	public void logCreationOfRentalBefore(Rental rental) {
		UserDTO user = findUserForRental(rental);
		if (user != null) {
			log.debug("Trying to create new rental for " + user.getFirstName() + " "
					+ user.getLastName());
		} else {
			log.info("user not found");
		}
	}

	@AfterReturning(pointcut = "execution(public * *..RentalController.create(..))", returning = "response")
	public void logCreationOfRentalAfter(ResponseEntity<Rental> response) {
		Rental rental = response.getBody();
		UserDTO user = findUserForRental(rental);
		if (user != null) {
			log.debug("Successfully created rental[" + rental.getId() + "] for "
					+ user.getFirstName() + " " + user.getLastName());
		} else {
			log.info("user not found");
		}
	}

	@Around("within(ch.fhnw.edu.eaf.rentalmgmt.web..*)")
	public Object traceService(ProceedingJoinPoint pjp) throws Throwable {
		log.debug("Calling Service in " + pjp.getTarget());
		Object o = pjp.proceed();
		log.debug("Returning from Service called in " + pjp.getTarget());
		return o;
	}

	private UserDTO findUserForRental(Rental rental) {
		String url = "http://" + userService + "/users/" + rental.getUserId();
		ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.GET, null, JsonNode.class);
		ObjectMapper mapper = new ObjectMapper();
		try {
			UserDTO dto = mapper.readValue(response.getBody().toString(), UserDTO.class);
			return dto;
		} catch (IOException e) {
			return null;
		}
	}
}

package ch.fhnw.edu.eaf.rentalmgmt.job;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.fhnw.edu.eaf.rentalmgmt.domain.Rental;
import ch.fhnw.edu.eaf.rentalmgmt.persistence.RentalRepository;
import ch.fhnw.edu.eaf.rentalmgmt.web.dto.UserDTO;

@Component
public class RentalOverdueJob {
	private Log log = LogFactory.getLog(this.getClass());

	@Autowired
	private RentalRepository rentalRepository;

	@Value(value = "${microservice.usermagagement:usermanagement}")
	private String userService;

	@Autowired
	private RestTemplate restTemplate;

	@Scheduled(fixedRateString = "${job.update-rate:5000}")
	public void checkRentals() {
		List<Rental> rentals = rentalRepository.findAll();
		Date now = new Date();
		for (Rental rental : rentals) {
			if (rental.calcRemainingDaysOfRental(now) < 0) {
				UserDTO user = findUserForRental(rental);
				if (user != null) {
					log.debug("Notify user " + user.getLastName() + " " + user.getFirstName());
				} else {
					log.info("No user found");
				}
			}
		}
	}

	private UserDTO findUserForRental(Rental rental) {
		try {
			String url = "http://" + userService + "/users/" + rental.getUserId();
			ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.GET, null, JsonNode.class);
			ObjectMapper mapper = new ObjectMapper();
			UserDTO dto = mapper.readValue(response.getBody().toString(), UserDTO.class);
			return dto;
		} catch (IOException e) {
			log.error("Could not deserialize json response");
			return null;
		} catch (Exception e) {
			log.error("Could not connect to service '" + userService + "'");
			return null;
		}
	}
}

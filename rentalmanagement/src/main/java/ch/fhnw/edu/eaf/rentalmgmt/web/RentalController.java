package ch.fhnw.edu.eaf.rentalmgmt.web;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import ch.fhnw.edu.eaf.rentalmgmt.domain.Rental;
import ch.fhnw.edu.eaf.rentalmgmt.persistence.RentalRepository;
import ch.fhnw.edu.eaf.rentalmgmt.web.dto.MovieDTO;
import ch.fhnw.edu.eaf.rentalmgmt.web.dto.RentalDTO;
import ch.fhnw.edu.eaf.rentalmgmt.web.dto.UserDTO;

@RestController
@RequestMapping("/rentals")
public class RentalController {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Value(value = "${microservice.usermagagement:usermanagement}")
	private String userService;

	@Value(value = "${microservice.moviemagagement:moviemanagement}")
	private String movieService;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private RentalRepository rentalRepository;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<RentalDTO>> findAll() {
		List<RentalDTO> dtos = new ArrayList<RentalDTO>();
		Sort sort = new Sort(Direction.ASC, "id");
		List<Rental> rentals = rentalRepository.findAll(sort);
		log.debug("Found " + rentals.size() + " rentals");
		rentals.forEach(rental -> dtos.add(createRentalDTO(rental)));
		return new ResponseEntity<List<RentalDTO>>(dtos, HttpStatus.OK);
	}

	@Cacheable(value = "rentals", key = "#id")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<RentalDTO> findById(@PathVariable Long id) {
		Rental rental = rentalRepository.findOne(id);
		if (rental == null) {
			return new ResponseEntity<RentalDTO>(HttpStatus.NOT_FOUND);
		}
		RentalDTO rentalDTO = createRentalDTO(rental);
		log.debug("Found rental with id=" + rental.getId());
		return new ResponseEntity<RentalDTO>(rentalDTO, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Rental> create(@Valid @RequestBody Rental rental, BindingResult result) {
		if (result.hasErrors()) {
			return new ResponseEntity<Rental>(HttpStatus.PRECONDITION_FAILED);
		}
		rental = rentalRepository.save(rental);
		log.debug("Created rental with id=" + rental.getId());
		return new ResponseEntity<Rental>(rental, HttpStatus.CREATED);
	}

	@CacheEvict(value = "rentals", key = "#id", beforeInvocation = true)
	// Attention: If using @Cacheable here, the response must conform to
	// getById() where a RentalDTO is returned!
	// @Cacheable(value = "rentals", key = "#id")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Rental> update(@Valid @RequestBody Rental newRental, @PathVariable Long id) {
		Rental rental = rentalRepository.findOne(id);
		if (rental == null) {
			return new ResponseEntity<Rental>(HttpStatus.NOT_FOUND);
		}
		rental.setRentalDate(newRental.getRentalDate());
		rental.setRentalDays(newRental.getRentalDays());
		rentalRepository.save(rental);
		log.debug("Updated rental with id=" + rental.getId());
		return new ResponseEntity<Rental>(rental, HttpStatus.OK);
	}

	@CacheEvict(value = "rentals", key = "#id")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> delete(@PathVariable Long id) {
		rentalRepository.delete(id);
		log.debug("Deleted rental with id=" + id);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	private RentalDTO createRentalDTO(Rental rental) {
		RentalDTO rentalDTO = new RentalDTO();
		rentalDTO.setId(rental.getId());
		rentalDTO.setRentalDate(rental.getRentalDate());
		rentalDTO.setRentalDays(rental.getRentalDays());
		UserDTO userDTO = getUserForRental(rental);
		MovieDTO movieDTO = getMovieForRental(rental);
		rentalDTO.setUser(userDTO);
		rentalDTO.setMovie(movieDTO);
		return rentalDTO;
	}

	private MovieDTO getMovieForRental(Rental rental) {
		String url = "http://" + movieService + "/movies/" + rental.getMovieId();
		// ResponseEntity<JsonNode> response = restTemplate.exchange(url,
		// HttpMethod.GET, null, JsonNode.class);
		// ResponseEntity<JsonNode> response = restTemplate.getForEntity(url,
		// JsonNode.class);
		// ObjectMapper mapper = new ObjectMapper();
		// try {
		// MovieDTO dto = mapper.readValue(response.getBody().toString(),
		// MovieDTO.class);
		// return dto;
		// } catch (IOException e) {
		// return null;
		// }
		MovieDTO dto = restTemplate.getForObject(url, MovieDTO.class);
		return dto;
	}

	private UserDTO getUserForRental(Rental rental) {
		String url = "http://" + userService + "/users/" + rental.getUserId();
		// ResponseEntity<JsonNode> response = restTemplate.exchange(url,
		// HttpMethod.GET, null, JsonNode.class);
		// ResponseEntity<JsonNode> response = restTemplate.getForEntity(url,
		// JsonNode.class);
		// ObjectMapper mapper = new ObjectMapper();
		// try {
		// UserDTO dto = mapper.readValue(response.getBody().toString(),
		// UserDTO.class);
		// return dto;
		// } catch (IOException e) {
		// return null;
		// }
		try {
			UserDTO dto = restTemplate.getForObject(url, UserDTO.class);
			return dto;
		} catch (Exception e) {
			UserDTO dto = new UserDTO();
			dto.setId(rental.getUserId());
			return dto;
		}
	}

}
